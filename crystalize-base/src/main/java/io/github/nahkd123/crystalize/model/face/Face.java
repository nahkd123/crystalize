package io.github.nahkd123.crystalize.model.face;

import com.mojang.serialization.Codec;

import io.github.nahkd123.crystalize.utils.Codecs;

public enum Face {
	NORTH,
	EAST,
	SOUTH,
	WEST,
	UP,
	DOWN;

	public static final Codec<Face> CODEC = Codecs.ofEnum(Face.class, true);
}
