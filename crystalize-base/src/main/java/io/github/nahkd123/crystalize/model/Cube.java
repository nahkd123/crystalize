package io.github.nahkd123.crystalize.model;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import org.joml.Vector3fc;

import io.github.nahkd123.crystalize.model.face.CubeFace;
import io.github.nahkd123.crystalize.model.face.Face;

public record Cube(Vector3fc origin, Vector3fc rotation, Vector3fc from, Vector3fc to, Map<Face, CubeFace> faces) implements Element {
	public Cube {
		Objects.requireNonNull(origin, "origin can't be null");
		Objects.requireNonNull(rotation, "rotation can't be null");
		Objects.requireNonNull(from, "from can't be null");
		Objects.requireNonNull(to, "to can't be null");
		faces = faces != null ? Collections.unmodifiableMap(faces) : Collections.emptyMap();
	}

	public Cube(Vector3fc origin, Vector3fc rotation, Vector3fc from, Vector3fc to) {
		this(origin, rotation, from, to, null);
	}
}
