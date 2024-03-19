package io.github.nahkd123.crystalize.fabric;

import io.github.nahkd123.crystalize.fabric.model.ModelsManager;

public interface CrystalizeFabric {
	public ModelsManager getModelsManager();

	public static CrystalizeFabric getInstance() { return CrystalizeFabricImpl.INSTANCE; }
}
