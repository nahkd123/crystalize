package io.github.nahkd123.crystalize.minecraft.model;

import java.util.Optional;
import java.util.OptionalDouble;

import org.joml.Vector3f;
import org.joml.Vector3fc;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.github.nahkd123.crystalize.utils.Codecs;

public record ElementRotation(Vector3fc pivot, Axis axis, double angle, boolean rescale) {

	public static final ElementRotation IDENTITY = new ElementRotation(new Vector3f(0, 0, 0), Axis.X, 0, false);

	public static final Codec<ElementRotation> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codecs.VECTOR_3FC.fieldOf("origin").forGetter(ElementRotation::pivot),
		Axis.CODEC.fieldOf("axis").forGetter(ElementRotation::axis),
		Codec.DOUBLE.fieldOf("angle").forGetter(ElementRotation::angle),
		Codec.BOOL.optionalFieldOf("rescale", false).forGetter(ElementRotation::rescale))
		.apply(instance, ElementRotation::new));

	public static Optional<ElementRotation> deriveFromEuler(Vector3fc pivot, Vector3fc euler) {
		double ax = Math.toDegrees(euler.x());
		double ay = Math.toDegrees(euler.y());
		double az = Math.toDegrees(euler.z());
		if (count(1E-6, ax, ay, az) > 1) return Optional.empty();

		double angle = pick(1E-6, ax, ay, az);
		OptionalDouble snapped = snapAngle(angle, 1E-6);
		if (snapped.isEmpty()) return Optional.empty();

		Axis axis = Math.abs(az) > Math.abs(ax) && Math.abs(az) > Math.abs(ay) ? Axis.Z
			: Math.abs(ay) > Math.abs(ax) && Math.abs(ay) > Math.abs(az) ? Axis.Y
			: Axis.X;
		return Optional.of(new ElementRotation(pivot, axis, snapped.getAsDouble(), false));
	}

	private static int count(double epsilon, double... ds) {
		int c = 0;
		for (int i = 0; i < ds.length; i++) c += Math.abs(ds[i]) < epsilon ? 0 : 1;
		return c;
	}

	private static double pick(double epsilon, double... ds) {
		for (int i = 0; i < ds.length; i++) if (Math.abs(ds[i]) > epsilon) return ds[i];
		return ds[0];
	}

	private static OptionalDouble snapAngle(double source, double delta) {
		for (double d = -45; d <= 45; d += 22.5) if (Math.abs(source - d) <= delta) return OptionalDouble.of(d);
		return OptionalDouble.empty();
	}
}
