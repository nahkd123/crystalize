package io.github.nahkd123.crystalize.bukkit.model;

import org.bukkit.NamespacedKey;

import io.github.nahkd123.crystalize.model.ElementGroup;
import io.github.nahkd123.crystalize.model.Model;

public interface RegisteredModel {
	public NamespacedKey id();

	public Model template();

	default NamespacedKey getItemModelPathFor(ElementGroup group) {
		NamespacedKey id = id();
		return new NamespacedKey(id.getNamespace(), "crystalize/" + id.getKey() + "/" + group.id());
	}
}
