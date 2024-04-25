package io.github.nahkd123.crystalize.minecraft.atlas;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.github.nahkd123.crystalize.minecraft.utils.ResourceLocation;

public record SpriteSingleSource(ResourceLocation resource, ResourceLocation mappedName) implements SpriteSource {
	public static final MapCodec<SpriteSingleSource> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		ResourceLocation.CODEC.fieldOf("resource").forGetter(SpriteSingleSource::resource),
		ResourceLocation.CODEC.optionalFieldOf("sprite", null).forGetter(SpriteSingleSource::mappedName))
		.apply(instance, SpriteSingleSource::new));

	@Override
	public MapCodec<? extends SpriteSource> getCodec() { return CODEC; }
}
