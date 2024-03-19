package io.github.nahkd123.crystalize.blockbench.outliner;

import org.joml.Vector3f;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.github.nahkd123.crystalize.utils.Codecs;

public interface Outliner {
	public static final Codec<Outliner> CODEC = Codecs.<Outliner>recursive(thiz -> Codec
		.either(
			BbElementReference.CODEC,
			RecordCodecBuilder.<BbElementGroup>create(instance -> instance.group(
				Codecs.UUID.fieldOf("uuid").forGetter(BbElementGroup::uuid),
				Codecs.VECTOR_3FC.fieldOf("origin").forGetter(BbElementGroup::origin),
				Codecs.VECTOR_3FC
					.optionalFieldOf("rotation", new Vector3f(0, 0, 0))
					.forGetter(BbElementGroup::rotation),
				Codec.BOOL.fieldOf("visibility").forGetter(BbElementGroup::visibility),
				thiz.listOf().fieldOf("children").forGetter(BbElementGroup::children))
				.apply(instance, BbElementGroup::new)))
		.xmap(
			either -> either.left().map(v -> (Outliner) v).orElseGet(() -> either.right().get()),
			obj -> obj instanceof BbElementReference ref
				? Either.left(ref)
				: obj instanceof BbElementGroup src ? Either.right(src)
				: null));
}
