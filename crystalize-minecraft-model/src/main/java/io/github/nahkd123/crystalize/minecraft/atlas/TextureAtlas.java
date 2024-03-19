package io.github.nahkd123.crystalize.minecraft.atlas;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record TextureAtlas(List<SpriteSource> sources) {
	public static final Codec<TextureAtlas> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		SpriteSource.CODEC.listOf().fieldOf("sources").forGetter(TextureAtlas::sources))
		.apply(instance, TextureAtlas::new));
}
