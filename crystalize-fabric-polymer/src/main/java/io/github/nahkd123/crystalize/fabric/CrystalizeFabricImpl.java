package io.github.nahkd123.crystalize.fabric;

import io.github.nahkd123.crystalize.fabric.model.ModelsManager;
import io.github.nahkd123.crystalize.fabric.model.ModelsManagerImpl;

public class CrystalizeFabricImpl implements CrystalizeFabric {
	protected static final CrystalizeFabric INSTANCE = new CrystalizeFabricImpl();

	private ModelsManager modelsManager = new ModelsManagerImpl();

	@Override
	public ModelsManager getModelsManager() { return modelsManager; }
}
