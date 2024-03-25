package io.github.nahkd123.crystalize.fabric.sample;

import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;

import io.github.nahkd123.crystalize.blockbench.BlockbenchProject;
import io.github.nahkd123.crystalize.blockbench.build.BlockbenchModelBuilder;
import io.github.nahkd123.crystalize.fabric.CrystalizeFabric;
import io.github.nahkd123.crystalize.fabric.model.ModelsManager;
import io.github.nahkd123.crystalize.fabric.sample.command.CrystalizeCommand;
import io.github.nahkd123.crystalize.model.Model;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

public class CrystalizeSampleMod implements ModInitializer {
	public static final String MODID = "crystalize-samplemod";
	public static final Logger LOGGER = LoggerFactory.getLogger("crystalize/samplemod");

	@Override
	public void onInitialize() {
		LOGGER.info("hi there!");

		// Load all Blockbench projects as models from "models/" in our sample mod
		// Formatter is turned off to prevent auto-wrapping in my IDE
		// @formatter:off
		ModelsManager modelsManager = CrystalizeFabric.getInstance().getModelsManager();
		modelsManager.registerModel(new Identifier(MODID, "bones_chain"), load("models/bones_chain.bbmodel").get());
		modelsManager.registerModel(new Identifier(MODID, "robotic_arm"), load("models/robotic_arm.bbmodel").get());
		modelsManager.registerModel(new Identifier(MODID, "taterinator"), load("models/Taterinator.bbmodel").get());
		modelsManager.registerModel(new Identifier(MODID, "tiny_potatog"), load("models/tiny_potatog.bbmodel").get());
		// @formatter:on

		// Register our test commands
		// CrystalizeCommand contains example on how to spawn a model, play animations
		// and use animation controllers.
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(CrystalizeCommand.command(modelsManager));
		});

		// Additionally, you can attach CrystalizeElementHolder to any custom Polymer
		// entity you want. You can obtain an instance of RegisteredModel when you've
		// registered your model by using ModelsManager#getModel(Identifier).
	}

	/*
	 * This method load a model from mod with ID assigned to MODID constant (which
	 * is "crystalize-samplemod").
	 */
	private Optional<Model> load(String resourcePath) {
		return FabricLoader.getInstance().getModContainer(MODID)
			.flatMap(mod -> mod.findPath(resourcePath))
			.map(path -> {
				try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
					return JsonParser.parseReader(reader);
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}
			})
			.flatMap(json -> BlockbenchProject.CODEC.decode(JsonOps.INSTANCE, json).resultOrPartial(LOGGER::error))
			.map(pair -> BlockbenchModelBuilder.build(pair.getFirst()));
	}
}
