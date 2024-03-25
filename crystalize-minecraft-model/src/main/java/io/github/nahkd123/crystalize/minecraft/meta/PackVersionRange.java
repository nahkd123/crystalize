package io.github.nahkd123.crystalize.minecraft.meta;

import static io.github.nahkd123.crystalize.minecraft.meta.PackVersions.v1_20_0;
import static io.github.nahkd123.crystalize.minecraft.meta.PackVersions.v1_20_4;

import java.util.Arrays;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.github.nahkd123.crystalize.utils.Codecs;

public record PackVersionRange(int minInclusive, int maxInclusive) {
	public static final PackVersionRange v1_20 = new PackVersionRange(v1_20_0, v1_20_4);

	public static final Codec<PackVersionRange> MAPLIKE_CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.INT.fieldOf("min_inclusive").forGetter(PackVersionRange::minInclusive),
		Codec.INT.fieldOf("max_inclusive").forGetter(PackVersionRange::maxInclusive))
		.apply(instance, PackVersionRange::new));

	public static final Codec<PackVersionRange> LIST_CODEC = Codecs.fixedList(Codec.INT, 2).xmap(
		list -> new PackVersionRange(list.get(0), list.get(1)),
		range -> Arrays.asList(range.minInclusive, range.maxInclusive));

	public static final Codec<PackVersionRange> CODEC = Codec.either(MAPLIKE_CODEC, LIST_CODEC)
		.xmap(
			either -> either.left().or(either::right).get(),
			Either::left);
}
