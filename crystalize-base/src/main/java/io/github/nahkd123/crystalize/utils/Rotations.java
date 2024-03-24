package io.github.nahkd123.crystalize.utils;

import org.joml.Math;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class Rotations {
	private static final float TWO_PI = (float) (Math.PI * 2f);
	private static final float HALF_PI = (float) (Math.PI / 2f);
	public static final Vector3fc UP = new Vector3f(0, 1, 0);

	public static Vector2f directionToAngles(Vector3fc direction, Vector2f out) {
		float x = direction.x();
		float z = direction.z();

		if (x == 0 && z == 0) {
			out.y = direction.y() > 0 ? -HALF_PI : HALF_PI;
			return out;
		}

		float theta = Math.atan2(-x, z);
		out.x = (theta + TWO_PI) % TWO_PI;

		float xz = Math.sqrt(x * x + z * z);
		out.y = (float) java.lang.Math.atan(-direction.y() / xz);
		return out;
	}

	public static Vector3f anglesToDirection(Vector2fc angles, Vector3f out) {
		float rotX = angles.x();
		float rotY = angles.y();
		out.y = -Math.sin(rotY);

		float xz = Math.cos(rotY);
		out.x = -xz * Math.sin(rotX);
		out.z = xz * Math.cos(rotX);
		return out;
	}
}
