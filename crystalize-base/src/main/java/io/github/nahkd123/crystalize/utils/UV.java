package io.github.nahkd123.crystalize.utils;

import java.util.Arrays;

import com.mojang.serialization.Codec;

public record UV(float fromX, float fromY, float toX, float toY) {

	public static final Codec<UV> CODEC = Codecs.fixedList(Codec.FLOAT, 4)
		.xmap(
			list -> new UV(list.get(0), list.get(1), list.get(2), list.get(3)),
			uv -> Arrays.asList(uv.fromX, uv.fromY, uv.toX, uv.toY));

	public UV flipX() {
		return new UV(toX, fromY, fromX, toY);
	}

	public UV flipY() {
		return new UV(fromX, toY, toX, fromY);
	}

	public UV scale(float factor) {
		return new UV(fromX * factor, fromY * factor, toX * factor, toY * factor);
	}
}
