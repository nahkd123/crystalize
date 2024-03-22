package io.github.nahkd123.crystalize.bukkit.resource;

/**
 * <p>
 * A resource provider. Crystalize uses this to avoid using Bukkit event, as it
 * is quite annoying to set it up. Resource provider can be reloaded
 * independently from each other. For example: When user added something to the
 * models list, Crystalize's resource provider will be reloaded, without
 * touching other resource providers (a.k.a other resource providers don't have
 * to be reloaded while Crystalize is reloading).
 * </p>
 * <p>
 * When your resource provider is reloaded (let's say someone used reload
 * command from your plugin, {@link ResourceManager#markReloaded()} must be
 * called. You can get the object from {@link #init(ResourceManager)}.
 * </p>
 * 
 * @param <T> The resource type.
 */
public interface ResourceProvider<T, M extends ResourceManager<T>> {
	default void init(M manager) {}

	public String getProviderName();

	/**
	 * <p>
	 * Get loaded resource. This method shouldn't try to load resource; consider
	 * loading inside {@link #init(ResourceManager)} or make your own reload method.
	 * </p>
	 * 
	 * @return The loaded resource.
	 */
	public T getResource();
}
