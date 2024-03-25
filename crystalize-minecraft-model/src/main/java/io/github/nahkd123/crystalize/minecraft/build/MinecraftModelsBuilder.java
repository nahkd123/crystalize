package io.github.nahkd123.crystalize.minecraft.build;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import org.joml.Vector3f;

import io.github.nahkd123.crystalize.minecraft.model.ElementRotation;
import io.github.nahkd123.crystalize.minecraft.model.GuiLight;
import io.github.nahkd123.crystalize.minecraft.model.MinecraftModel;
import io.github.nahkd123.crystalize.minecraft.model.MinecraftModelElement;
import io.github.nahkd123.crystalize.minecraft.model.face.FaceInfo;
import io.github.nahkd123.crystalize.minecraft.model.face.ModelTexture;
import io.github.nahkd123.crystalize.minecraft.utils.ResourceLocation;
import io.github.nahkd123.crystalize.model.Cube;
import io.github.nahkd123.crystalize.model.Element;
import io.github.nahkd123.crystalize.model.ElementGroup;
import io.github.nahkd123.crystalize.model.Model;
import io.github.nahkd123.crystalize.model.face.CubeFace;
import io.github.nahkd123.crystalize.model.face.Face;
import io.github.nahkd123.crystalize.texture.Texture;
import io.github.nahkd123.crystalize.utils.UV;

public class MinecraftModelsBuilder {
	public static List<Map.Entry<ElementGroup, MinecraftModel>> build(Model template, Function<Texture, ResourceLocation> textureResolver) {
		Map<String, ResourceLocation> textures = new HashMap<>();
		Map<Texture, ModelTexture> textureVars = new HashMap<>();
		textures.put("missing", new ResourceLocation("missingno"));

		for (int i = 0; i < template.textures().size(); i++) {
			Texture texture = template.textures().get(i);
			textures.put(Integer.toString(i), textureResolver.apply(texture));
			textureVars.put(texture, new ModelTexture(Integer.toString(i)));
		}

		List<Map.Entry<ElementGroup, MinecraftModel>> fragments = new ArrayList<>();
		build(template.root(), textures, textureVars, fragments::add);
		return fragments;
	}

	public static void build(ElementGroup group, Map<String, ResourceLocation> textures, Map<Texture, ModelTexture> textureVars, Consumer<Map.Entry<ElementGroup, MinecraftModel>> consumer) {
		List<MinecraftModelElement> elements = new ArrayList<>();

		for (Element child : group.children()) {
			if (child instanceof ElementGroup childGroup) build(childGroup, textures, textureVars, consumer);

			if (child instanceof Cube cube) {
				Vector3f from = new Vector3f(cube.from()).add(8, 8, 8);
				Vector3f to = new Vector3f(cube.to()).add(8, 8, 8);
				Optional<ElementRotation> rot = ElementRotation.deriveFromEuler(cube.origin(), cube.rotation());
				if (rot.isEmpty()) continue; // TODO log misaligned or make a new model
				ElementRotation rotation = rot.get();
				Map<Face, FaceInfo> faces = FaceInfo.createMap();

				for (Map.Entry<Face, CubeFace> face : cube.faces().entrySet()) {
					ModelTexture texVar = textureVars.get(face.getValue().texture());
					UV sourceUv = face.getValue().uv();
					UV targetUv = sourceUv.scale(16f / face.getValue().texture().uvWidth()); // TODO investigate
					FaceInfo builtFace = new FaceInfo(targetUv, texVar, face.getValue().rotation(), 0);
					faces.put(face.getKey(), builtFace);
				}

				MinecraftModelElement element = new MinecraftModelElement(from, to, rotation, true, faces);
				elements.add(element);
			}
		}

		MinecraftModel built = new MinecraftModel(null, Collections.emptyMap(), textures, GuiLight.SIDE, elements, Collections.emptyList());
		consumer.accept(Map.entry(group, built));
	}
}
