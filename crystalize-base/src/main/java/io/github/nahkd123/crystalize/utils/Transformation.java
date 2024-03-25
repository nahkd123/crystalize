package io.github.nahkd123.crystalize.utils;

import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 * <p>
 * The part transformation info. Translations and rotations are additive; scales
 * are multiplicative.
 * </p>
 */
public record Transformation(Vector3fc translate, Vector3fc rotate, Vector3fc scale) {

	private static final Vector3f ZERO = new Vector3f(0, 0, 0);
	private static final Vector3f ONE = new Vector3f(1, 1, 1);

	/**
	 * <p>
	 * The identity transformation, which doesn't do anything to model parts when
	 * applying. Translation and rotation are zero vectors; scale is a vector full
	 * of 1s.
	 * </p>
	 */
	public static final Transformation IDENTITY = new Transformation(ZERO, ZERO, ONE);

	public Transformation then(Vector3fc translate, Vector3fc rotate, Vector3fc scale) {
		// @formatter:off
		return new Transformation(
			this.translate.add(translate, new Vector3f()),
			this.rotate.add(rotate, new Vector3f()),
			this.scale.mul(scale, new Vector3f()));
		// @formatter:on
	}

	public Transformation then(Transformation another) {
		return then(another.translate, another.rotate, another.scale);
	}
}
