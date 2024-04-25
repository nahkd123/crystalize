package io.github.nahkd123.crystalize.blockbench.model;

import java.util.Map;
import java.util.UUID;

import org.joml.Vector3f;
import org.joml.Vector3fc;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.github.nahkd123.crystalize.blockbench.model.face.FaceTexture;
import io.github.nahkd123.crystalize.model.face.Face;
import io.github.nahkd123.crystalize.utils.Codecs;

public record BbCubeElement(UUID uuid, Vector3fc origin, Vector3fc rotation, Vector3fc from, Vector3fc to, Map<Face, FaceTexture> faces) implements BbElement {

	public static final MapCodec<BbCubeElement> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		Codecs.UUID.fieldOf("uuid").forGetter(BbCubeElement::uuid),
		Codecs.VECTOR_3FC.fieldOf("origin").forGetter(BbCubeElement::origin),
		Codecs.VECTOR_3FC.optionalFieldOf("rotation", new Vector3f(0, 0, 0)).forGetter(BbCubeElement::rotation),
		Codecs.VECTOR_3FC.fieldOf("from").forGetter(BbCubeElement::from),
		Codecs.VECTOR_3FC.fieldOf("to").forGetter(BbCubeElement::to),
		FaceTexture.FACES_CODEC.fieldOf("faces").forGetter(BbCubeElement::faces))
		.apply(instance, BbCubeElement::new));

	@Override
	public MapCodec<? extends BbElement> getCodec() { return CODEC; }
}
