package io.github.nahkd123.crystalize.minecraft.model;

import com.mojang.serialization.Codec;

import io.github.nahkd123.crystalize.utils.Codecs;

public enum Axis {
	X,
	Y,
	Z;

	public static final Codec<Axis> CODEC = Codecs.ofEnum(Axis.class, true);
}
