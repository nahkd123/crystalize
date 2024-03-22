package io.github.nahkd123.crystalize.bukkit.model;

import org.bukkit.NamespacedKey;

import io.github.nahkd123.crystalize.model.Model;

public record RegisteredModelImpl(NamespacedKey id, Model template) implements RegisteredModel {
}
