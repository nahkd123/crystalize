package io.github.nahkd123.crystalize.fabric.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;

import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import io.github.nahkd123.crystalize.fabric.debug.CrystalizeDebugInitializer;
import io.github.nahkd123.crystalize.fabric.utils.ResourceWrapper;
import io.github.nahkd123.crystalize.minecraft.atlas.SpriteSingleSource;
import io.github.nahkd123.crystalize.minecraft.atlas.TextureAtlas;
import io.github.nahkd123.crystalize.minecraft.build.MinecraftModelsBuilder;
import io.github.nahkd123.crystalize.minecraft.model.MinecraftModel;
import io.github.nahkd123.crystalize.minecraft.utils.ResourceLocation;
import io.github.nahkd123.crystalize.model.Element;
import io.github.nahkd123.crystalize.model.ElementGroup;
import io.github.nahkd123.crystalize.model.Model;
import io.github.nahkd123.crystalize.texture.Texture;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class ModelsManagerImpl implements ModelsManager {
	private final Gson gson = new Gson();
	private final Map<Identifier, Model> templates = new HashMap<>();
	private final Map<Identifier, RegisteredModelImpl> registered = new HashMap<>();

	public ModelsManagerImpl() {
		PolymerResourcePackUtils.RESOURCE_PACK_CREATION_EVENT.register(builder -> {
			ResourceWrapper wrapper = new ResourceWrapper(builder, gson);

			for (RegisteredModelImpl info : registered.values()) {
				buildTexturesAndModels(wrapper, info);

				if (FabricLoader.getInstance().isDevelopmentEnvironment())
					CrystalizeDebugInitializer.LOGGER.info("Generated textures and models for {}", info.id());
			}
		});
	}

	private void buildTexturesAndModels(ResourceWrapper wrapper, RegisteredModelImpl info) {
		Identifier id = info.id();
		Model template = info.template();
		String baseTexturesPath = "assets/" + id.getNamespace() + "/textures/crystalize/" + id.getPath();

		for (Texture texture : template.textures()) {
			String texturePath = baseTexturesPath + "/" + texture.id() + ".png";
			wrapper.saveRaw(texturePath, texture.data().getBytes());

			// TODO generate .mcmeta
			// may requires 3rd party library to figure out the real image size
			// but if blockbench does provide us this information, that would be very epic

			String spriteSourcePath = "crystalize/" + id.getPath() + "/" + texture.id();
			ResourceLocation spriteSourceResLoc = new ResourceLocation(id.getNamespace(), spriteSourcePath);
			SpriteSingleSource spriteSource = new SpriteSingleSource(spriteSourceResLoc, null);
			TextureAtlas atlas = new TextureAtlas(Collections.singletonList(spriteSource));

			// Polymer does merge atlas
			wrapper.saveJson("assets/minecraft/atlases/blocks.json", atlas, TextureAtlas.CODEC);
		}

		String baseModelPath = "assets/" + id.getNamespace() + "/models/crystalize/" + id.getPath();
		List<Entry<ElementGroup, MinecraftModel>> fragments = MinecraftModelsBuilder.build(
			template,
			texture -> {
				String path = "crystalize/" + id.getPath() + "/" + texture.id();
				return new ResourceLocation(id.getNamespace(), path);
			});

		for (Entry<ElementGroup, MinecraftModel> fragment : fragments) {
			ElementGroup group = fragment.getKey();
			MinecraftModel built = fragment.getValue();
			String modelPath = baseModelPath + "/" + group.id() + ".json";
			wrapper.saveJson(modelPath, built, MinecraftModel.CODEC);
		}
	}

	@Override
	public RegisteredModel registerModel(Identifier id, Model template) {
		if (templates.putIfAbsent(id, template) != null)
			throw new IllegalStateException("registerModel() can only be used once for each ID");

		RegisteredModelImpl registeredVariant = new RegisteredModelImpl(id, template);
		registered.put(id, registeredVariant);
		registerFragment(template.root(), id);

		if (FabricLoader.getInstance().isDevelopmentEnvironment())
			CrystalizeDebugInitializer.LOGGER.info("Registered model: {}", id);
		return registeredVariant;
	}

	private void registerFragment(ElementGroup fragment, Identifier id) {
		Identifier modelId = new Identifier(id.getNamespace(), "crystalize/" + id.getPath() + "/" + fragment.id());
		PolymerResourcePackUtils.requestModel(Items.COMMAND_BLOCK, modelId);

		for (Element child : fragment.children()) {
			if (!(child instanceof ElementGroup childGroup)) continue;
			registerFragment(childGroup, id);
		}
	}

	@Override
	public Map<Identifier, Model> getRegisteredModels() { return Collections.unmodifiableMap(templates); }

	@Override
	public RegisteredModel getModel(Identifier id) {
		return registered.get(id);
	}
}
