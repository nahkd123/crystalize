package io.github.nahkd123.crystalize.fabric.model;

import java.util.Map;

import io.github.nahkd123.crystalize.fabric.CrystalizeFabric;
import io.github.nahkd123.crystalize.model.Model;
import net.minecraft.util.Identifier;

/**
 * <p>
 * Models manager allows you to register Crystalize model, or get existing
 * registered model.
 * </p>
 * 
 * @see CrystalizeFabric#getModelsManager()
 * @see #registerModel(Identifier, Model)
 * @see #getModel(Identifier)
 */
public interface ModelsManager {
	/**
	 * <p>
	 * Register the model. This method should only called once during mod
	 * initialization.
	 * </p>
	 * 
	 * @param id       The ID of the model.
	 * @param template The model template data.
	 * @return The registered model variant.
	 */
	public RegisteredModel registerModel(Identifier id, Model template);

	public Map<Identifier, ? extends RegisteredModel> getRegisteredModels();

	public RegisteredModel getModel(Identifier id);
}
