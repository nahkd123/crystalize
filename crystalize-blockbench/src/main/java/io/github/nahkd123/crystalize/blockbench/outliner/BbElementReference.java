package io.github.nahkd123.crystalize.blockbench.outliner;

import java.util.UUID;

import com.mojang.serialization.Codec;

import io.github.nahkd123.crystalize.utils.Codecs;

public record BbElementReference(UUID target) implements Outliner {
	public static final Codec<BbElementReference> CODEC = Codecs.UUID.xmap(
		BbElementReference::new,
		BbElementReference::target);

	@Override
	public String toString() {
		return "BbElementReference[" + target + "]";
	}
}
