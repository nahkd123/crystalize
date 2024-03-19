package io.github.nahkd123.crystalize.minecraft.model;

import com.mojang.serialization.Codec;

import io.github.nahkd123.crystalize.utils.Codecs;

public enum GuiLight {
	SIDE,
	FRONT;

	public static final Codec<GuiLight> CODEC = Codecs.ofEnum(GuiLight.class, true);
}
