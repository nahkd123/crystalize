package io.github.nahkd123.crystalize.blockbench.model;

import java.util.UUID;

import org.joml.Vector3fc;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.github.nahkd123.crystalize.utils.Codecs;

public record BbLocatorElement(UUID uuid, Vector3fc origin, Vector3fc rotation) implements BbElement {

	public static final Codec<BbLocatorElement> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codecs.UUID.fieldOf("uuid").forGetter(BbLocatorElement::uuid),
		Codecs.VECTOR_3FC.fieldOf("position").forGetter(BbLocatorElement::origin),
		Codecs.VECTOR_3FC.fieldOf("rotation").forGetter(BbLocatorElement::rotation))
		.apply(instance, BbLocatorElement::new));

	@Override
	public Codec<? extends BbElement> getCodec() { return CODEC; }
}
