package io.github.nahkd123.crystalize.fabric;

import io.github.nahkd123.crystalize.fabric.model.ModelsManager;

/**
 * <p>
 * Main Crystalize for Fabric API interface.
 * </p>
 * 
 * @see #getInstance()
 */
public interface CrystalizeFabric {
	/**
	 * <p>
	 * Get the models manager.
	 * </p>
	 * 
	 * @return The models manager.
	 */
	public ModelsManager getModelsManager();

	public static CrystalizeFabric getInstance() { return CrystalizeFabricImpl.INSTANCE; }
}
