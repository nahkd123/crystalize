package io.github.nahkd123.crystalize.minecraft.atlas;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record UnstitchRegion(String sprite, double x, double y, double width, double height) {
	public static final Codec<UnstitchRegion> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.STRING.fieldOf("sprite").forGetter(UnstitchRegion::sprite),
		Codec.DOUBLE.fieldOf("x").forGetter(UnstitchRegion::x),
		Codec.DOUBLE.fieldOf("y").forGetter(UnstitchRegion::y),
		Codec.DOUBLE.fieldOf("width").forGetter(UnstitchRegion::width),
		Codec.DOUBLE.fieldOf("height").forGetter(UnstitchRegion::height))
		.apply(instance, UnstitchRegion::new));
}
