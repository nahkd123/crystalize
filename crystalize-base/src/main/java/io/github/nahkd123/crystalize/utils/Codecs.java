package io.github.nahkd123.crystalize.utils;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import org.joml.Vector3f;
import org.joml.Vector3fc;

import com.google.common.base.Suppliers;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class Codecs {
	public static <T extends Enum<T>> Codec<T> ofEnum(Class<T> type, boolean ignoreCases) {
		return Codec.STRING.comapFlatMap(
			s -> {
				try {
					if (ignoreCases) s = s.toUpperCase();
					return DataResult.success(Enum.valueOf(type, s));
				} catch (IllegalArgumentException e) {
					return DataResult.error(e::getMessage);
				}
			},
			e -> {
				String s = e.toString();
				if (ignoreCases) s = s.toLowerCase();
				return s;
			});
	}

	public static <T> Codec<List<T>> fixedList(Codec<T> base, int size) {
		return base.listOf().comapFlatMap(
			list -> {
				return list.size() != size
					? DataResult.error(() -> "Length of the list must be " + size)
					: DataResult.success(list);
			},
			list -> list);
	}

	public static <U> Codec<U> recursive(Function<Codec<U>, Codec<U>> a) {
		return new Codec<U>() {
			private Supplier<Codec<U>> supplier = Suppliers.memoize(() -> a.apply(this));

			@Override
			public <T> DataResult<T> encode(U input, DynamicOps<T> ops, T prefix) {
				return supplier.get().encode(input, ops, prefix);
			}

			@Override
			public <T> DataResult<Pair<U, T>> decode(DynamicOps<T> ops, T input) {
				return supplier.get().decode(ops, input);
			}
		};
	}

	public static final Codec<UUID> UUID = Codec.STRING.comapFlatMap(
		s -> {
			try {
				return DataResult.success(java.util.UUID.fromString(s));
			} catch (IllegalArgumentException e) {
				return DataResult.error(e::getMessage);
			}
		},
		uuid -> uuid.toString());

	public static final Codec<Vector3f> VECTOR_3F_LIST = fixedList(Codec.FLOAT, 3).xmap(
		list -> new Vector3f(list.get(0), list.get(1), list.get(2)),
		vector -> Arrays.asList(vector.x, vector.y, vector.z));

	private static final Codec<Float> DOUBLE_OR_STRING = Codec.either(
		Codec.FLOAT,
		Codec.STRING.comapFlatMap(
			s -> {
				try {
					return DataResult.success(Float.parseFloat(s));
				} catch (NumberFormatException e) {
					return DataResult.error(e::getMessage);
				}
			},
			d -> Double.toString(d)))
		.xmap(
			either -> either.left().orElseGet(() -> either.right().get()),
			d -> Either.left(d));

	public static final Codec<Vector3f> VECTOR_3F_MAP = RecordCodecBuilder.create(instance -> instance.group(
		DOUBLE_OR_STRING.fieldOf("x").forGetter(Vector3f::x),
		DOUBLE_OR_STRING.fieldOf("y").forGetter(Vector3f::y),
		DOUBLE_OR_STRING.fieldOf("z").forGetter(Vector3f::z))
		.apply(instance, Vector3f::new));

	public static final Codec<Vector3f> VECTOR_3F = Codec.either(VECTOR_3F_LIST, VECTOR_3F_MAP)
		.xmap(
			either -> either.left().orElseGet(() -> either.right().get()),
			vector -> Either.left(vector));

	public static final Codec<Vector3fc> VECTOR_3FC = Codecs.VECTOR_3F_LIST.xmap(v -> (Vector3fc) v, Vector3f::new);
}
