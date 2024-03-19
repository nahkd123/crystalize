package io.github.nahkd123.crystalize.minecraft.model;

import org.joml.Vector3f;
import org.joml.Vector3fc;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.github.nahkd123.crystalize.utils.Codecs;

public record DisplayTransformation(Vector3fc translation, Vector3fc rotation, Vector3fc scale) {
	public static final Codec<DisplayTransformation> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codecs.VECTOR_3FC
			.optionalFieldOf("translation", new Vector3f(0, 0, 0))
			.forGetter(DisplayTransformation::translation),
		Codecs.VECTOR_3FC
			.optionalFieldOf("rotation", new Vector3f(0, 0, 0))
			.forGetter(DisplayTransformation::rotation),
		Codecs.VECTOR_3FC
			.optionalFieldOf("scale", new Vector3f(1, 1, 1))
			.forGetter(DisplayTransformation::scale))
		.apply(instance, DisplayTransformation::new));
}
