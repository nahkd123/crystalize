package io.github.nahkd123.crystalize.minecraft.atlas;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record SpriteDirectorySource(String source, String prefix) implements SpriteSource {
	public static final MapCodec<SpriteDirectorySource> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		Codec.STRING.fieldOf("source").forGetter(SpriteDirectorySource::source),
		Codec.STRING.fieldOf("prefix").forGetter(SpriteDirectorySource::prefix))
		.apply(instance, SpriteDirectorySource::new));

	@Override
	public MapCodec<? extends SpriteSource> getCodec() { return CODEC; }
}
