package io.github.nahkd123.crystalize.minecraft.atlas;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record SpriteUnstitchSource(String resource, double divisorX, double divisorY, List<UnstitchRegion> regions) implements SpriteSource {

	public static final Codec<SpriteUnstitchSource> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.STRING.fieldOf("resource").forGetter(SpriteUnstitchSource::resource),
		Codec.DOUBLE.fieldOf("divisor_x").forGetter(SpriteUnstitchSource::divisorX),
		Codec.DOUBLE.fieldOf("divisor_y").forGetter(SpriteUnstitchSource::divisorY),
		UnstitchRegion.CODEC.listOf().fieldOf("regions").forGetter(SpriteUnstitchSource::regions))
		.apply(instance, SpriteUnstitchSource::new));

	@Override
	public Codec<? extends SpriteSource> getCodec() { return CODEC; }
}
