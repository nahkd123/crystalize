package io.github.nahkd123.crystalize.blockbench.anim;

import com.mojang.serialization.Codec;

import io.github.nahkd123.crystalize.utils.Codecs;

public enum LoopMode {
	ONCE,
	LOOP;

	public static final Codec<LoopMode> CODEC = Codecs.ofEnum(LoopMode.class, true);
}
