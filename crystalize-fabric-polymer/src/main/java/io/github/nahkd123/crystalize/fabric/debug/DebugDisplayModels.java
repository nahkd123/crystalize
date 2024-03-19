package io.github.nahkd123.crystalize.fabric.debug;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import org.joml.Vector3f;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;

import eu.pb4.polymer.resourcepack.api.PolymerModelData;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import io.github.nahkd123.crystalize.minecraft.model.ElementRotation;
import io.github.nahkd123.crystalize.minecraft.model.GuiLight;
import io.github.nahkd123.crystalize.minecraft.model.MinecraftModel;
import io.github.nahkd123.crystalize.minecraft.model.MinecraftModelElement;
import io.github.nahkd123.crystalize.minecraft.model.face.FaceInfo;
import io.github.nahkd123.crystalize.minecraft.model.face.ModelTexture;
import io.github.nahkd123.crystalize.minecraft.utils.ResourceLocation;
import io.github.nahkd123.crystalize.utils.UV;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtInt;
import net.minecraft.util.Identifier;

public class DebugDisplayModels {
	// @formatter:off
	public static final MinecraftModel AXES = new MinecraftModel(
		null,
		Collections.emptyMap(),
		Map.of(
			"0", new ResourceLocation("minecraft", "block/gold_block"),
			"x", new ResourceLocation("minecraft", "block/red_concrete"),
			"y", new ResourceLocation("minecraft", "block/green_concrete"),
			"z", new ResourceLocation("minecraft", "block/blue_concrete")),
		GuiLight.SIDE,
		Arrays.asList(
			new MinecraftModelElement(
				new Vector3f(-2, -2, -2).add(8, 8, 8),
				new Vector3f(2, 2, 2).add(8, 8, 8),
				ElementRotation.IDENTITY,
				true,
				FaceInfo.allFaces(new UV(0, 0, 16, 16), new ModelTexture("0"), 0, 0)),
			new MinecraftModelElement(
				new Vector3f(2, -1, -1).add(8, 8, 8),
				new Vector3f(16, 1, 1).add(8, 8, 8),
				ElementRotation.IDENTITY,
				true,
				FaceInfo.allFaces(new UV(0, 0, 16, 16), new ModelTexture("x"), 0, 0)),
			new MinecraftModelElement(
				new Vector3f(-1, 2, -1).add(8, 8, 8),
				new Vector3f(1, 16, 1).add(8, 8, 8),
				ElementRotation.IDENTITY,
				true,
				FaceInfo.allFaces(new UV(0, 0, 16, 16), new ModelTexture("y"), 0, 0)),
			new MinecraftModelElement(
				new Vector3f(-1, -1, 2).add(8, 8, 8),
				new Vector3f(1, 1, 16).add(8, 8, 8),
				ElementRotation.IDENTITY,
				true,
				FaceInfo.allFaces(new UV(0, 0, 16, 16), new ModelTexture("z"), 0, 0))));
	// @formatter:on

	private static final Gson GSON = new Gson();
	private static PolymerModelData tinyCubeModel = null;

	private static byte[] toBytes(MinecraftModel model) {
		DataResult<JsonElement> result = MinecraftModel.CODEC.encodeStart(JsonOps.INSTANCE, model);
		JsonElement element = result.resultOrPartial(null).get();
		return GSON.toJson(element).getBytes(StandardCharsets.UTF_8);
	}

	public static void initializeModels() {
		tinyCubeModel = PolymerResourcePackUtils.requestModel(Items.BARRIER, new Identifier("crystalize", "axes"));

		PolymerResourcePackUtils.RESOURCE_PACK_CREATION_EVENT.register(builder -> {
			builder.addData("assets/crystalize/models/axes.json", toBytes(AXES));
		});
	}

	public static ItemStack getAxesModel() {
		ItemStack stack = new ItemStack(tinyCubeModel.item());
		stack.setSubNbt("CustomModelData", NbtInt.of(tinyCubeModel.value()));
		return stack;
	}
}
