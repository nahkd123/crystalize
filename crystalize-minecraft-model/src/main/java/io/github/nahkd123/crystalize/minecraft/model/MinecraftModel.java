package io.github.nahkd123.crystalize.minecraft.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.github.nahkd123.crystalize.minecraft.model.override.ModelOverride;
import io.github.nahkd123.crystalize.minecraft.utils.ResourceLocation;

public record MinecraftModel(ResourceLocation parent, Map<DisplaySlot, DisplayTransformation> display, Map<String, ResourceLocation> textures, GuiLight guiLight, List<MinecraftModelElement> elements, List<ModelOverride> overrides) {

	public static final Codec<MinecraftModel> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		ResourceLocation.CODEC.optionalFieldOf("parent", null).forGetter(MinecraftModel::parent),
		Codec.unboundedMap(DisplaySlot.CODEC, DisplayTransformation.CODEC)
			.optionalFieldOf("display", Collections.emptyMap())
			.forGetter(MinecraftModel::display),
		Codec.unboundedMap(Codec.STRING, ResourceLocation.CODEC)
			.optionalFieldOf("textures", Collections.emptyMap())
			.forGetter(MinecraftModel::textures),
		GuiLight.CODEC.optionalFieldOf("gui_light", GuiLight.SIDE).forGetter(MinecraftModel::guiLight),
		MinecraftModelElement.CODEC.listOf()
			.optionalFieldOf("elements", Collections.emptyList())
			.forGetter(MinecraftModel::elements),
		ModelOverride.CODEC.listOf()
			.optionalFieldOf("overrides", Collections.emptyList())
			.forGetter(MinecraftModel::overrides))
		.apply(instance, MinecraftModel::new));
}
