package io.github.nahkd123.crystalize.bukkit;

import java.util.List;
import java.util.function.Supplier;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.nahkd123.crystalize.bukkit.model.ModelsManager;
import io.github.nahkd123.crystalize.bukkit.model.RegisteredModel;
import io.github.nahkd123.crystalize.bukkit.pack.ResourcePackBuilder;
import io.github.nahkd123.crystalize.bukkit.resource.ModelResourceManager;
import io.github.nahkd123.crystalize.bukkit.resource.ResourceProvider;

public interface CrystalizeBukkit {
	public <T extends ResourceProvider<List<RegisteredModel>, ModelResourceManager>> void addProvider(Supplier<T> provider);

	public ModelsManager getModelsManager();

	/**
	 * <p>
	 * Register the resource pack builder. This method should only be called during
	 * loading phase of your plugin. Please note that the user will have to pick
	 * preferred pack builder in Crystalize configuration if they want to use your
	 * builder.
	 * </p>
	 * 
	 * @param id          The ID of the builder.
	 * @param constructor The constructor that creates a brand new builder with
	 *                    initial states.
	 */
	public void registerPackBuilder(NamespacedKey id, Supplier<? extends ResourcePackBuilder> constructor);

	public Supplier<? extends ResourcePackBuilder> getPreferredPackBuilder();

	/**
	 * <p>
	 * Get the main Crystalize Bukkit API instance. Usually the Crystalize plugin
	 * instance (which implements {@link CrystalizeBukkit}) but that might be
	 * changed in the future.
	 * </p>
	 * 
	 * @return The Bukkit API instance.
	 */
	public static CrystalizeBukkit getInstance() { return JavaPlugin.getPlugin(CrystalizePlugin.class); }
}
