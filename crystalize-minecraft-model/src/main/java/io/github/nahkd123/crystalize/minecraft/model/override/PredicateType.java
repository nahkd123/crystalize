package io.github.nahkd123.crystalize.minecraft.model.override;

import com.mojang.serialization.Codec;

import io.github.nahkd123.crystalize.utils.Codecs;

public enum PredicateType {
	ANGLE,
	BLOCKING,
	BROKEN,
	CAST,
	COOLDOWN,
	DAMAGE,
	DAMAGED,
	LEFTHANDED,
	PULL,
	PULLING,
	CHARGED,
	FIREWORK,
	THROWING,
	TIME,
	CUSTOM_MODEL_DATA,
	LEVEL,
	FILLED,
	TOOTING,
	TRIM_TYPE,
	BRUSHING;

	public static final Codec<PredicateType> CODEC = Codecs.ofEnum(PredicateType.class, true);
}
