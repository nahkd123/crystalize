package io.github.nahkd123.crystalize.minecraft.atlas;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record SpriteDirectorySource(String source, String prefix) implements SpriteSource {
	public static final Codec<SpriteDirectorySource> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.STRING.fieldOf("source").forGetter(SpriteDirectorySource::source),
		Codec.STRING.fieldOf("prefix").forGetter(SpriteDirectorySource::prefix))
		.apply(instance, SpriteDirectorySource::new));

	@Override
	public Codec<? extends SpriteSource> getCodec() { return CODEC; }
}
