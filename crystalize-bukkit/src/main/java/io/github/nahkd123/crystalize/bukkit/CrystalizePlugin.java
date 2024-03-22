package io.github.nahkd123.crystalize.bukkit;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.nahkd123.crystalize.bukkit.config.CrystalizeConfig;
import io.github.nahkd123.crystalize.bukkit.model.ModelsManager;
import io.github.nahkd123.crystalize.bukkit.model.ModelsManagerImpl;
import io.github.nahkd123.crystalize.bukkit.model.RegisteredModel;
import io.github.nahkd123.crystalize.bukkit.pack.CrystalizeResourcePackBuilder;
import io.github.nahkd123.crystalize.bukkit.pack.PackBuildContextImpl;
import io.github.nahkd123.crystalize.bukkit.pack.ResourcePackBuilder;
import io.github.nahkd123.crystalize.bukkit.resource.CrystalizePluginModelResourceProvider;
import io.github.nahkd123.crystalize.bukkit.resource.ModelResourceManager;
import io.github.nahkd123.crystalize.bukkit.resource.ResourceProvider;

public class CrystalizePlugin extends JavaPlugin implements CrystalizeBukkit {
	private Path dataRoot;
	private boolean freeze = false;
	private List<ModelResourceManager> modelResources = new ArrayList<>();
	private Map<NamespacedKey, Supplier<? extends ResourcePackBuilder>> packBuilders = new HashMap<>();

	// Plugin & Config
	private ModelsManagerImpl modelsManager = null;
	private CrystalizePluginModelResourceProvider modelProvider = null;
	private Supplier<? extends ResourcePackBuilder> defaultPackBuilder = null;
	private CrystalizeConfig config = null;

	@Override
	public void onLoad() {
		dataRoot = getDataFolder().toPath();

		// Add providers
		// @formatter:off
		addProvider(() -> modelProvider = new CrystalizePluginModelResourceProvider(this));
		registerPackBuilder(new NamespacedKey(this, "default"), defaultPackBuilder = () -> new CrystalizeResourcePackBuilder(this));
		// @formatter:on
	}

	@Override
	public void onEnable() {
		modelsManager = new ModelsManagerImpl();

		freeze = true;
		modelResources = Collections.unmodifiableList(modelResources);
		packBuilders = Collections.unmodifiableMap(packBuilders);

		getLogger().info("Loading configuration...");
		if (!new File(getDataFolder(), "config.yml").exists()) {
			saveResource("config.yml", false);
			reloadConfig();
		}
		config = CrystalizeConfig.fromConfig(getConfig());

		getLogger().info("Initializing...");
		modelResources.forEach(ModelResourceManager::init);
		buildAll();

		// Print out info just in case
		getLogger().info("");
		getLogger().info("  Crystalize for Bukkit by @nahkd123");
		getLogger().info("  Version v" + getDescription().getVersion());
		getLogger().info("  Targets Bukkit v" + getDescription().getAPIVersion());
		getLogger().info("  Running on " + Bukkit.getVersion());
		getLogger().info("");
		getLogger().info("  Source Code:    https://github.com/nahkd123/crystalize");
		getLogger().info("  Issues Tracker: https://github.com/nahkd123/crystalize/issues");
		getLogger().info("");
		getLogger().info("  Model providers: (" + modelResources.size() + ")");
		modelResources.forEach(m -> getLogger().info("    " + m.getProvider().getProviderName()));
		getLogger().info("");
		getLogger().info("  Available pack builders: (" + packBuilders.size() + ")");
		packBuilders
			.forEach((k, v) -> getLogger().info("    " + k + (k.equals(config.pack().preferredBuilder())
				? " (preferred)"
				: "")));
		getLogger().info("");
	}

	public Path getDataRoot() { return dataRoot; }

	public void buildAll() {
		getLogger().info("Building...");
		ResourcePackBuilder packBuilder = getPreferredPackBuilder().get();
		modelsManager.rebuild(modelResources, packBuilder);

		if (!config.pack().ignore()) {
			PackBuildContextImpl ctx = new PackBuildContextImpl();
			ctx.addDestination(dataRoot.resolve("resource_pack.zip"));
			if (config.pack().autoCopy().enable()) ctx.addDestination(config.pack().autoCopy().getCopyPath());
			packBuilder.build(ctx);
		}

		getLogger().info("Build finished!");
		getLogger().info("  Loaded models: " + modelsManager.getAllModels().size());
	}

	@Override
	public <T extends ResourceProvider<List<RegisteredModel>, ModelResourceManager>> void addProvider(Supplier<T> provider) {
		if (freeze) throw new IllegalStateException("Cannot add new provider when frozen. "
			+ "Consider adding provider during load phase of your plugin!");
		modelResources.add(new ModelResourceManager(provider, () -> {
			getLogger().info("A model provider has been reloaded, rebuilding resource pack...");
			buildAll();
		}));
	}

	@Override
	public ModelsManager getModelsManager() { return modelsManager; }

	@Override
	public void registerPackBuilder(NamespacedKey id, Supplier<? extends ResourcePackBuilder> constructor) {
		if (freeze) throw new IllegalStateException("Cannot add new pack builder when frozen. "
			+ "Consider adding pack builder during load phase of your plugin!");
		packBuilders.put(id, constructor);
	}

	@Override
	public Supplier<? extends ResourcePackBuilder> getPreferredPackBuilder() {
		NamespacedKey id = config.pack().preferredBuilder();
		Supplier<? extends ResourcePackBuilder> supplier = packBuilders.getOrDefault(id, defaultPackBuilder);
		return supplier;
	}

	/**
	 * <p>
	 * Get the default model provider from Crystalize plugin. Can be used to trigger
	 * models reload with
	 * {@link CrystalizePluginModelResourceProvider#reloadModels()}.
	 * </p>
	 * <p>
	 * This was meant to be used within Crystalize.
	 * </p>
	 * 
	 * @return The model provider.
	 */
	public CrystalizePluginModelResourceProvider getCrystalizeModelProvider() { return modelProvider; }

	public void reloadConfiguration() {
		reloadConfig();
		config = CrystalizeConfig.fromConfig(getConfig());
		modelProvider.reloadModels();
	}

	/**
	 * <p>
	 * Get current plugin configuration.
	 * </p>
	 * 
	 * @return The current plugin configuration.
	 */
	public CrystalizeConfig getPluginConfig() { return config; }
}
