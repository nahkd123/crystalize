package io.github.nahkd123.crystalize.model;

import org.joml.Vector3fc;

public interface Element {
	/**
	 * <p>
	 * The origin (as known as pivot point) of this element in model local space.
	 * This value is relative to parent element. The world origin value can be
	 * obtained by scaling this value by {@code 1/16}.
	 * </p>
	 * <p>
	 * This element will rotate around its origin.
	 * </p>
	 * 
	 * @return The origin of this element.
	 */
	public Vector3fc origin();

	/**
	 * <p>
	 * The rotation of this element around its origin, represented as euler angles
	 * in radians.
	 * </p>
	 * 
	 * @return The rotation of this element.
	 */
	public Vector3fc rotation();
}
