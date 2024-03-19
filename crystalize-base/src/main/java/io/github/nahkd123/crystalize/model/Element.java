package io.github.nahkd123.crystalize.model;

import org.joml.Vector3fc;

public interface Element {
	/**
	 * <p>
	 * The origin (as known as pivot point) of this element in model local space.
	 * This value is relative to parent element.
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

	/*
	 * Transformations of the current element should only be applied to display
	 * entity of current element (a.k.a it will not affect the children display
	 * entity directly). Ok but tbh this is really hard to explain.
	 */
}
