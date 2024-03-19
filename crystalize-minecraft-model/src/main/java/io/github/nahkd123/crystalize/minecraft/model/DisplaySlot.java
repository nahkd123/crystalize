package io.github.nahkd123.crystalize.minecraft.model;

import com.mojang.serialization.Codec;

import io.github.nahkd123.crystalize.utils.Codecs;

public enum DisplaySlot {
	FIRSTPERSON_LEFTHAND,
	FIRSTPERSON_RIGHTHAND,
	THIRDPERSON_LEFTHAND,
	THIRDPERSON_RIGHTHAND,
	GUI,
	HEAD,
	GROUND,
	FIXED;

	public static final Codec<DisplaySlot> CODEC = Codecs.ofEnum(DisplaySlot.class, true);
}
