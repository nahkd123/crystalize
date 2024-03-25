package io.github.nahkd123.crystalize.minecraft.model.override;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.github.nahkd123.crystalize.minecraft.utils.ResourceLocation;

public record ModelOverride(PredicateType type, double value, ResourceLocation model) {
	public ModelOverride(Pair<PredicateType, Double> pair, ResourceLocation model) {
		this(pair.getFirst(), pair.getSecond(), model);
	}

	public Pair<PredicateType, Double> pair() {
		return Pair.of(type, value);
	}

	public static final Codec<ModelOverride> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.unboundedMap(PredicateType.CODEC, Codec.DOUBLE)
			.comapFlatMap(
				map -> {
					if (map.size() != 1) return DataResult.error(() -> "MapLike must have exactly 1 entry");
					Entry<PredicateType, Double> e = new ArrayList<>(map.entrySet()).get(0);
					return DataResult.success(Pair.of(e.getKey(), e.getValue()));
				},
				pair -> Map.of(pair.getFirst(), pair.getSecond()))
			.fieldOf("predicate").forGetter(ModelOverride::pair),
		ResourceLocation.CODEC.fieldOf("model").forGetter(ModelOverride::model))
		.apply(instance, ModelOverride::new));
}
