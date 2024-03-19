package io.github.nahkd123.crystalize.minecraft.model.face;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.github.nahkd123.crystalize.model.face.Face;
import io.github.nahkd123.crystalize.utils.UV;

public record FaceInfo(UV uv, ModelTexture texture, int rotation, int tintIndex) {

	public static final FaceInfo MISSING = new FaceInfo(new UV(0, 0, 0, 0), new ModelTexture("missing"), 0, 0);

	public FaceInfo {
		Objects.requireNonNull(uv, "uv can't be null");
		Objects.requireNonNull(texture, "texture can't be null");
		if ((rotation % 90) != 0) throw new IllegalArgumentException("rotation must be a multiple of 90");
	}

	public static final Codec<FaceInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		UV.CODEC.fieldOf("uv").forGetter(FaceInfo::uv),
		ModelTexture.CODEC.fieldOf("texture").forGetter(FaceInfo::texture),
		Codec.INT.optionalFieldOf("rotation", 0).forGetter(FaceInfo::rotation),
		Codec.INT.optionalFieldOf("tintIndex", 0).forGetter(FaceInfo::tintIndex))
		.apply(instance, FaceInfo::new));

	public static Map<Face, FaceInfo> createMap() {
		Map<Face, FaceInfo> map = new EnumMap<>(Face.class);
		for (Face f : Face.values()) map.put(f, MISSING);
		return map;
	}

	public static Map<Face, FaceInfo> allFaces(UV uv, ModelTexture texture, int rotation, int tintIndex) {
		FaceInfo info = new FaceInfo(uv, texture, rotation, tintIndex);
		Map<Face, FaceInfo> map = new EnumMap<>(Face.class);
		for (Face f : Face.values()) map.put(f, info);
		return map;
	}
}
