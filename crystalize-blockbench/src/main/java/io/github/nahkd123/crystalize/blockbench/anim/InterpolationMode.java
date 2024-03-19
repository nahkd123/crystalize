package io.github.nahkd123.crystalize.blockbench.anim;

import com.mojang.serialization.Codec;

import io.github.nahkd123.crystalize.utils.Codecs;

public enum InterpolationMode {
	LINEAR,
	CATMULLROM,
	BEZIER,
	STEP;

	public static final Codec<InterpolationMode> CODEC = Codecs.ofEnum(InterpolationMode.class, true);
}
