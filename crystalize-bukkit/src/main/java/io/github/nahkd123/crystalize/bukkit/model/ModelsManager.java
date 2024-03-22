package io.github.nahkd123.crystalize.bukkit.model;

import java.util.Map;

import org.bukkit.NamespacedKey;

public interface ModelsManager {
	public Map<NamespacedKey, ? extends RegisteredModel> getAllModels();

	public RegisteredModel getModel(NamespacedKey id);

	public ItemModel getItemModel(NamespacedKey modelPath);
}
