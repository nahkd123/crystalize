package io.github.nahkd123.crystalize.bukkit.resource;

import java.util.List;
import java.util.function.Supplier;

import org.bukkit.NamespacedKey;

import io.github.nahkd123.crystalize.bukkit.model.RegisteredModel;
import io.github.nahkd123.crystalize.bukkit.model.RegisteredModelImpl;
import io.github.nahkd123.crystalize.model.Model;

public class ModelResourceManager implements ResourceManager<List<RegisteredModel>> {
	private ResourceProvider<List<RegisteredModel>, ModelResourceManager> provider;
	private Runnable reloadTrigger;

	public ModelResourceManager(Supplier<? extends ResourceProvider<List<RegisteredModel>, ModelResourceManager>> supplier, Runnable reloadTrigger) {
		this.reloadTrigger = reloadTrigger;
		this.provider = supplier.get();
	}

	public void init() {
		provider.init(this);
	}

	@Override
	public void markReloaded() {
		reloadTrigger.run();
	}

	public RegisteredModel register(NamespacedKey id, Model model) {
		// TODO check for ID conflicts
		return new RegisteredModelImpl(id, model);
	}

	@Override
	public ResourceProvider<List<RegisteredModel>, ? extends ResourceManager<List<RegisteredModel>>> getProvider() {
		return provider;
	}
}
