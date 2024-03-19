package io.github.nahkd123.crystalize.model;

import java.util.Collections;
import java.util.List;

import org.joml.Vector3fc;

public record ElementGroup(String id, Vector3fc origin, Vector3fc rotation, List<Element> children) implements Element {
	public ElementGroup {
		children = Collections.unmodifiableList(children);
	}
}
