package io.github.nahkd123.crystalize.minecraft.atlas;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record SpriteFilterSource(String namespacePattern, String pathPattern) implements SpriteSource {
	public static final MapCodec<SpriteFilterSource> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		Codec.STRING.optionalFieldOf("namespace", null).forGetter(SpriteFilterSource::namespacePattern),
		Codec.STRING.optionalFieldOf("path", null).forGetter(SpriteFilterSource::pathPattern))
		.apply(instance, SpriteFilterSource::new));

	@Override
	public MapCodec<? extends SpriteSource> getCodec() { return CODEC; }
}
