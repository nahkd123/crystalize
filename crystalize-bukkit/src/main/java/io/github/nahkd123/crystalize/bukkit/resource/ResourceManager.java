package io.github.nahkd123.crystalize.bukkit.resource;

public interface ResourceManager<T> {
	public void markReloaded();

	public ResourceProvider<T, ? extends ResourceManager<T>> getProvider();
}
