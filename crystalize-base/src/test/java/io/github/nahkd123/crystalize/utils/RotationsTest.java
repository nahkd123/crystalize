package io.github.nahkd123.crystalize.utils;

import org.joml.Random;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.junit.jupiter.api.Test;

class RotationsTest {
	@Test
	void test() {
		Vector3f dir = new Vector3f();
		Vector3f outDir = new Vector3f();
		Vector2f angles = new Vector2f();
		Random rng = new Random();

		for (int i = 0; i < 10; i++) {
			dir.x = rng.nextFloat() * 2f - 1f;
			dir.y = rng.nextFloat() * 2f - 1f;
			dir.z = rng.nextFloat() * 2f - 1f;
			Rotations.directionToAngles(dir, angles);
			Rotations.anglesToDirection(angles, outDir);
			System.out.println(dir + "; " + outDir);
		}
	}
}
