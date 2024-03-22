package io.github.nahkd123.crystalize.bukkit.resource;

import java.util.List;

import io.github.nahkd123.crystalize.bukkit.model.RegisteredModel;

/**
 * <p>
 * Model resource provider provides models for Crystalize to build resource pack
 * and animating model. You can start implementing your own provider by
 * extending {@link SimpleModelResourceProvider}, or implementing
 * {@link ModelResourceProvider}.
 * </p>
 * 
 * @see SimpleModelResourceProvider
 */
public interface ModelResourceProvider extends ResourceProvider<List<RegisteredModel>, ModelResourceManager> {
}
