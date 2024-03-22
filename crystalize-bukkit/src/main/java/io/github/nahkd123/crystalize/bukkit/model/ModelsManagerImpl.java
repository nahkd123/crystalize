package io.github.nahkd123.crystalize.bukkit.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.NamespacedKey;

import io.github.nahkd123.crystalize.bukkit.pack.ResourcePackBuilder;
import io.github.nahkd123.crystalize.bukkit.resource.ModelResourceManager;
import io.github.nahkd123.crystalize.minecraft.build.MinecraftModelsBuilder;
import io.github.nahkd123.crystalize.minecraft.model.MinecraftModel;
import io.github.nahkd123.crystalize.minecraft.utils.ResourceLocation;
import io.github.nahkd123.crystalize.model.ElementGroup;
import io.github.nahkd123.crystalize.model.Model;
import io.github.nahkd123.crystalize.texture.Texture;

public class ModelsManagerImpl implements ModelsManager {
	private Map<NamespacedKey, ? extends RegisteredModel> models = Collections.emptyMap();
	private Map<NamespacedKey, ItemModel> fragmentModels = Collections.emptyMap();

	public ModelsManagerImpl() {}

	@SuppressWarnings("unchecked")
	public void rebuild(List<ModelResourceManager> modelsResources, ResourcePackBuilder packBuilder) {
		models = new HashMap<>();
		fragmentModels = new HashMap<>();

		for (ModelResourceManager manager : modelsResources) {
			for (RegisteredModel model : manager.getProvider().getResource()) {
				NamespacedKey modelId = model.id();
				Model template = model.template();

				for (Texture texture : template.textures()) {
					String filePath = "crystalize/" + modelId.getKey() + "/" + texture.id();
					NamespacedKey textureId = new NamespacedKey(modelId.getNamespace(), filePath);
					packBuilder.addTexture(textureId, texture);
				}

				List<Entry<ElementGroup, MinecraftModel>> fragments = MinecraftModelsBuilder.build(
					template,
					texture -> {
						String path = "crystalize/" + modelId.getKey() + "/" + texture.id();
						return new ResourceLocation(modelId.getNamespace(), path);
					});

				for (Entry<ElementGroup, MinecraftModel> fragment : fragments) {
					ElementGroup group = fragment.getKey();
					MinecraftModel built = fragment.getValue();
					String filePath = "crystalize/" + modelId.getKey() + "/" + group.id();
					NamespacedKey modelPath = new NamespacedKey(modelId.getNamespace(), filePath);
					ItemModel fragModel = packBuilder.addModel(modelPath, built);
					fragmentModels.put(modelPath, fragModel);
				}

				((Map<NamespacedKey, RegisteredModel>) models).put(modelId, model);
			}
		}

		models = Collections.unmodifiableMap(models);
		fragmentModels = Collections.unmodifiableMap(fragmentModels);
	}

	@Override
	public Map<NamespacedKey, ? extends RegisteredModel> getAllModels() { return models; }

	@Override
	public RegisteredModel getModel(NamespacedKey id) {
		return models.get(id);
	}

	@Override
	public ItemModel getItemModel(NamespacedKey modelPath) {
		return fragmentModels.get(modelPath);
	}
}
