package io.github.nahkd123.crystalize.minecraft.atlas;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.github.nahkd123.crystalize.minecraft.utils.ResourceLocation;

public record SpriteSingleSource(ResourceLocation resource, ResourceLocation mappedName) implements SpriteSource {
	public static final Codec<SpriteSingleSource> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		ResourceLocation.CODEC.fieldOf("resource").forGetter(SpriteSingleSource::resource),
		ResourceLocation.CODEC.optionalFieldOf("sprite", null).forGetter(SpriteSingleSource::mappedName))
		.apply(instance, SpriteSingleSource::new));

	@Override
	public Codec<? extends SpriteSource> getCodec() { return CODEC; }
}
