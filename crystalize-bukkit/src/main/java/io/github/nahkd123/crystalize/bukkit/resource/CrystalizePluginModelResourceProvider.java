package io.github.nahkd123.crystalize.bukkit.resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.bukkit.NamespacedKey;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import io.github.nahkd123.crystalize.bukkit.CrystalizePlugin;
import io.github.nahkd123.crystalize.bukkit.model.RegisteredModel;
import io.github.nahkd123.crystalize.model.Model;

public class CrystalizePluginModelResourceProvider extends SimpleModelResourceProvider {
	private CrystalizePlugin plugin;
	private Path modelsDir;
	private List<RegisteredModel> models = Collections.emptyList();

	public CrystalizePluginModelResourceProvider(CrystalizePlugin plugin) {
		super(plugin.getLogger(), "Crystalize");
		this.plugin = plugin;
		this.modelsDir = plugin.getDataRoot().resolve("models");

		try {
			Files.createDirectories(modelsDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init(ModelResourceManager manager) {
		super.init(manager);
		loadModels();
	}

	@Override
	public List<RegisteredModel> getResource() { return Collections.unmodifiableList(models); }

	public void loadModels() {
		Logger logger = plugin.getLogger();
		logger.info("Loading models...");
		models = new ArrayList<>();
		loadModelsDir(modelsDir, null);
		logger.info("Loaded " + models.size() + " models from " + modelsDir);
	}

	public void reloadModels() {
		loadModels();
		getManager().markReloaded();
	}

	private void loadModelsDir(Path dir, String namespace) {
		Logger logger = plugin.getLogger();

		try {
			Files.list(dir).forEach(child -> {
				if (Files.isDirectory(child)) {
					loadModelsDir(child, child.getFileName().toString());
				} else {
					String key = removeExtension(child.getFileName().toString());
					NamespacedKey modelId = namespace != null
						? new NamespacedKey(namespace, key)
						: new NamespacedKey(plugin, key);
					logger.info("Loading " + modelId + " from " + child + "...");
					Optional<Model> modelOpt = loadModelFile(child, modelId);

					if (modelOpt.isPresent()) {
						RegisteredModel registered = getManager().register(modelId, modelOpt.get());
						models.add(registered);
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Optional<Model> loadModelFile(Path file, NamespacedKey id) {
		try (InputStream stream = Files.newInputStream(file)) {
			// TODO switch model based on file format
			return readBlockbenchModel(stream, file.toString());
		} catch (JsonIOException | JsonSyntaxException | IOException e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}

	private String removeExtension(String s) {
		String[] split = s.split("\\.");
		return s.substring(0, s.length() - split[split.length - 1].length() - 1);
	}
}
