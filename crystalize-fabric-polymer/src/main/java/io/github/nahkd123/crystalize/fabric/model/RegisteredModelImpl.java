package io.github.nahkd123.crystalize.fabric.model;

import io.github.nahkd123.crystalize.model.Model;
import net.minecraft.util.Identifier;

public record RegisteredModelImpl(Identifier id, Model template) implements RegisteredModel {
}
