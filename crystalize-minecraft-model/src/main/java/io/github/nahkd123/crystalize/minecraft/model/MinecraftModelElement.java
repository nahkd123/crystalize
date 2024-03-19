package io.github.nahkd123.crystalize.minecraft.model;

import java.util.Map;

import org.joml.Vector3fc;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.github.nahkd123.crystalize.minecraft.model.face.FaceInfo;
import io.github.nahkd123.crystalize.model.face.Face;
import io.github.nahkd123.crystalize.utils.Codecs;

public record MinecraftModelElement(Vector3fc from, Vector3fc to, ElementRotation rotation, boolean shade, Map<Face, FaceInfo> faces) {
	public static final Codec<MinecraftModelElement> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codecs.VECTOR_3FC.fieldOf("from").forGetter(MinecraftModelElement::from),
		Codecs.VECTOR_3FC.fieldOf("to").forGetter(MinecraftModelElement::to),
		ElementRotation.CODEC
			.optionalFieldOf("rotation", ElementRotation.IDENTITY)
			.forGetter(MinecraftModelElement::rotation),
		Codec.BOOL.optionalFieldOf("shade", true).forGetter(MinecraftModelElement::shade),
		Codec.unboundedMap(Face.CODEC, FaceInfo.CODEC).fieldOf("faces").forGetter(MinecraftModelElement::faces))
		.apply(instance, MinecraftModelElement::new));
}
