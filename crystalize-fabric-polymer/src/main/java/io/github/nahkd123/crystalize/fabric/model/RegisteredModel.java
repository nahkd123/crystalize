package io.github.nahkd123.crystalize.fabric.model;

import eu.pb4.polymer.resourcepack.api.PolymerModelData;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import io.github.nahkd123.crystalize.model.ElementGroup;
import io.github.nahkd123.crystalize.model.Model;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public interface RegisteredModel {
	public Identifier id();

	public Model template();

	default PolymerModelData getItemModelFor(ElementGroup group) {
		Identifier id = id();
		Identifier modelId = new Identifier(id.getNamespace(), "crystalize/" + id.getPath() + "/" + group.id());
		return PolymerResourcePackUtils.requestModel(Items.COMMAND_BLOCK, modelId);
	}

	default ItemStack getItemFor(ElementGroup group) {
		PolymerModelData modelData = getItemModelFor(group);
		ItemStack stack = new ItemStack(modelData.item());
		stack.set(DataComponentTypes.CUSTOM_MODEL_DATA, new CustomModelDataComponent(modelData.value()));
		return stack;
	}
}
