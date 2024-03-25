package io.github.nahkd123.crystalize.bukkit.model;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public record ItemModel(Material material, int value) {
	public ItemStack createStack() {
		ItemStack stack = new ItemStack(material);
		ItemMeta meta = stack.getItemMeta();
		meta.setCustomModelData(value);
		stack.setItemMeta(meta);
		return stack;
	}
}
