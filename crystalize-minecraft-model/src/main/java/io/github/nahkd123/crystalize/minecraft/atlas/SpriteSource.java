package io.github.nahkd123.crystalize.minecraft.atlas;

import java.util.Map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

import io.github.nahkd123.crystalize.utils.MapUtils;

public interface SpriteSource {
	public MapCodec<? extends SpriteSource> getCodec();

	public static final Map<String, MapCodec<? extends SpriteSource>> ID_TO_CODEC = Map.of(
		"directory", SpriteDirectorySource.CODEC,
		"single", SpriteSingleSource.CODEC,
		"filter", SpriteFilterSource.CODEC,
		"paletted_permutations", SpritePalettedPermutationsSource.CODEC);

	public static final Map<MapCodec<? extends SpriteSource>, String> CODEC_TO_ID = MapUtils.reverse(ID_TO_CODEC);

	public static final Codec<SpriteSource> CODEC = Codec.STRING.dispatch(
		"type",
		e -> CODEC_TO_ID.get(e.getCodec()),
		ID_TO_CODEC::get);
}
