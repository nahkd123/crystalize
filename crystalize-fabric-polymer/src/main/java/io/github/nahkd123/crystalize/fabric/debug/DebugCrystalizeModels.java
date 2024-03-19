package io.github.nahkd123.crystalize.fabric.debug;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.UUID;

import org.joml.Vector3f;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;

import io.github.nahkd123.crystalize.blockbench.BlockbenchProject;
import io.github.nahkd123.crystalize.blockbench.build.BlockbenchModelBuilder;
import io.github.nahkd123.crystalize.fabric.CrystalizeFabric;
import io.github.nahkd123.crystalize.fabric.model.ModelsManager;
import io.github.nahkd123.crystalize.model.ElementGroup;
import io.github.nahkd123.crystalize.model.Model;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class DebugCrystalizeModels {
	// @formatter:off
	public static final Model BONES_4 = new Model(
		new ElementGroup(
			"bone1",
			new Vector3f(0f, 0f, 0f),
			new Vector3f(0f, 0f, 0f),
			Collections.singletonList(new ElementGroup(
				"bone2",
				new Vector3f(1f, 1f, 0f),
				new Vector3f(0f, MathHelper.PI / 6, 0f),
				Collections.singletonList(new ElementGroup(
					"bone3",
					new Vector3f(1f, 1f, 0f),
					new Vector3f(0f, MathHelper.PI / 6, 0f),
					Collections.singletonList(new ElementGroup(
						"bone4",
						new Vector3f(1f, 1f, 0f),
						new Vector3f(0f, MathHelper.PI / 6, 0f),
						Collections.emptyList()))))))),
		Collections.emptyList(),
		Collections.emptyList());
	// @formatter:on

	public static void initializeModels() {
		ModelsManager manager = CrystalizeFabric.getInstance().getModelsManager();

		try {
			manager.registerModel(new Identifier("crystalize", "debug/taterinator"), loadModel("Taterinator.bbmodel"));
			manager.registerModel(new Identifier("crystalize", "debug/bones_chain"), loadModel("bones_chain.bbmodel"));
		} catch (IOException e) {
			CrystalizeDebugInitializer.LOGGER.warn("Failed to load the potato: {}", e.getMessage());
		}

		manager.registerModel(new Identifier("crystalize", "debug/bones_4"), BONES_4);
	}

	private static Model loadModel(String modelPath) throws IOException {
		ModContainer mod = FabricLoader.getInstance().getModContainer("crystalize").get();
		Path path = mod.findPath(modelPath).get();
		JsonElement json = JsonParser.parseString(Files.readString(path, StandardCharsets.UTF_8));
		BlockbenchProject bb = BlockbenchProject.CODEC
			.decode(JsonOps.INSTANCE, json)
			.resultOrPartial(CrystalizeDebugInitializer.LOGGER::warn)
			.map(Pair::getFirst)
			.get();
		UUID rootUuid = UUID.nameUUIDFromBytes("root".getBytes(StandardCharsets.UTF_8));
		return BlockbenchModelBuilder.build(rootUuid, bb);
	}
}
