package io.github.nahkd123.crystalize.bukkit.pack;

import org.bukkit.NamespacedKey;

import io.github.nahkd123.crystalize.bukkit.model.ItemModel;
import io.github.nahkd123.crystalize.minecraft.model.MinecraftModel;
import io.github.nahkd123.crystalize.texture.Texture;

/**
 * <p>
 * Resource pack builders are used to build resource packs (obviously). This was
 * created since there is no universal API for building resource pack on Bukkit
 * platform (Fabric, however, have such API: Polymer Resource Pack).
 * </p>
 * <p>
 * Any plugin can register its {@link ResourcePackBuilder} to mix with other
 * assets, like custom items or custom blocks.
 * </p>
 */
public interface ResourcePackBuilder {
	/**
	 * <p>
	 * Add a new model to the resource pack.
	 * </p>
	 * 
	 * @param modelPath The path to model (without {@code .json} extension).
	 * @param model     The model to add.
	 * @return New {@link ItemModel}.
	 */
	public ItemModel addModel(NamespacedKey modelPath, MinecraftModel model);

	/**
	 * <p>
	 * Add a new texture to the resource pack.
	 * </p>
	 * 
	 * @param texture The path to texture (without {@code .png} extension).
	 */
	public void addTexture(NamespacedKey texturePath, Texture texture);

	/**
	 * <p>
	 * Build the resource pack. This will be called by Crystalize plugin once all
	 * assets added to the builder.
	 * </p>
	 * 
	 * @param context The pack build context.
	 */
	public void build(PackBuildContext context);
}
