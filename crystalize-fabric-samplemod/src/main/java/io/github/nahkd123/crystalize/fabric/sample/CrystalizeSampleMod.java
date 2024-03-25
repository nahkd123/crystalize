package io.github.nahkd123.crystalize.fabric.sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.JsonOps;

import eu.pb4.polymer.core.api.entity.PolymerEntityUtils;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import io.github.nahkd123.crystalize.blockbench.BlockbenchProject;
import io.github.nahkd123.crystalize.blockbench.build.BlockbenchModelBuilder;
import io.github.nahkd123.crystalize.fabric.CrystalizeFabric;
import io.github.nahkd123.crystalize.fabric.model.ModelsManager;
import io.github.nahkd123.crystalize.fabric.sample.command.CrystalizeCommand;
import io.github.nahkd123.crystalize.fabric.sample.entity.RoboticArmEntity;
import io.github.nahkd123.crystalize.fabric.sample.entity.TinyPotatogEntity;
import io.github.nahkd123.crystalize.model.Model;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.passive.FrogEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
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

		// You can also load models outside the mod as well! Just make sure to only
		// register before the server is started, otherwise some item models might be
		// missing on client.
		loadExternalModels(modelsManager);

		// We have language file stored in our "assets/" folder, so we need to add
		// assets from our mod to Polymer Resource Pack API.
		PolymerResourcePackUtils.addModAssets(MODID);

		// Register our test commands
		// CrystalizeCommand contains example on how to spawn a model, play animations
		// and use animation controllers.
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(CrystalizeCommand.command(modelsManager));
		});

		// Additionally, you can attach CrystalizeElementHolder to any custom Polymer
		// entity you want. You can obtain an instance of RegisteredModel when you've
		// registered your model by using ModelsManager#getModel(Identifier).

		// Now let's register our entity that uses our custom model!
		// Our entity is 100% server-side, which means client only need to accept server
		// resource pack to see your robotic arms!
		Registry.register(Registries.ENTITY_TYPE, new Identifier(MODID, "robotic_arm"), RoboticArmEntity.TYPE);
		PolymerEntityUtils.registerType(RoboticArmEntity.TYPE);

		Registry.register(Registries.ENTITY_TYPE, new Identifier(MODID, "tiny_potatog"), TinyPotatogEntity.TYPE);
		FabricDefaultAttributeRegistry.register(TinyPotatogEntity.TYPE, FrogEntity.createFrogAttributes());
		PolymerEntityUtils.registerType(TinyPotatogEntity.TYPE);
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

	private void loadExternalModels(ModelsManager modelsManager) {
		try {
			Path externModelsDir = FabricLoader.getInstance().getConfigDir().resolve(MODID).resolve("models");
			LOGGER.info("Loading our external models from {}", externModelsDir);
			if (Files.notExists(externModelsDir)) Files.createDirectories(externModelsDir);

			Files.list(externModelsDir).forEach(modelPath -> {
				String filename = modelPath.getFileName().toString();
				if (!filename.endsWith(".bbmodel")) return;

				// .substring() removes the .bbmodel (8 characters) from filename
				Identifier modelId = new Identifier(MODID, "external/" + filename.substring(0, filename.length() - 8));

				try (BufferedReader reader = Files.newBufferedReader(modelPath)) {
					JsonElement json = JsonParser.parseReader(reader);
					var result = BlockbenchProject.CODEC.decode(JsonOps.INSTANCE, json);
					if (result.error().isPresent()) throw new JsonSyntaxException(result.error().get().message());

					BlockbenchProject proj = result.get().left().get().getFirst();
					Model model = BlockbenchModelBuilder.build(proj);
					modelsManager.registerModel(modelId, model);
				} catch (IOException | JsonSyntaxException e) {
					e.printStackTrace();
					LOGGER.error("Failed to load external model: " + modelId);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.error("Failed to load models from our external folder. Let's ignore that.");
		}
	}

}
