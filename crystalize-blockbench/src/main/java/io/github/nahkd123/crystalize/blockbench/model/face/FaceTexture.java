package io.github.nahkd123.crystalize.blockbench.model.face;

import java.util.Map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.github.nahkd123.crystalize.model.face.Face;
import io.github.nahkd123.crystalize.utils.UV;

public record FaceTexture(UV uv, int textureIndex) {
	public static final Codec<FaceTexture> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		UV.CODEC.fieldOf("uv").forGetter(FaceTexture::uv),
		Codec.INT.optionalFieldOf("texture", -1).forGetter(FaceTexture::textureIndex))
		.apply(instance, FaceTexture::new));

	public static final Codec<Map<Face, FaceTexture>> FACES_CODEC = Codec.unboundedMap(Face.CODEC, FaceTexture.CODEC);
}
