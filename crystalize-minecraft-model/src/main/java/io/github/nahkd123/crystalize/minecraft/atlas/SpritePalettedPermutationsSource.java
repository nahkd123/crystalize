package io.github.nahkd123.crystalize.minecraft.atlas;

import java.util.List;
import java.util.Map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.github.nahkd123.crystalize.minecraft.utils.ResourceLocation;

public record SpritePalettedPermutationsSource(List<ResourceLocation> textures, ResourceLocation paletteKey, Map<String, ResourceLocation> permutations) implements SpriteSource {

	public static final MapCodec<SpritePalettedPermutationsSource> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
		ResourceLocation.CODEC.listOf().fieldOf("textures").forGetter(SpritePalettedPermutationsSource::textures),
		ResourceLocation.CODEC.fieldOf("palette_key").forGetter(SpritePalettedPermutationsSource::paletteKey),
		Codec.unboundedMap(Codec.STRING, ResourceLocation.CODEC)
			.fieldOf("permutations")
			.forGetter(SpritePalettedPermutationsSource::permutations))
		.apply(i, SpritePalettedPermutationsSource::new));

	@Override
	public MapCodec<? extends SpriteSource> getCodec() { return CODEC; }
}
