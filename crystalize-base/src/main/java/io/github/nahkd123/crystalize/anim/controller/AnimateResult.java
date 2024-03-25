package io.github.nahkd123.crystalize.anim.controller;

/**
 * <p>
 * Animate result controls how the animation controllers collection handle the
 * controllers for the next animation tick.
 * </p>
 */
public enum AnimateResult {
	/**
	 * <p>
	 * Keep the controller in the animation controllers collection in next animation
	 * tick.
	 * </p>
	 */
	CONTINUE,

	/**
	 * <p>
	 * Remove this controller from the animation controllers collection after
	 * current animation tick.
	 * </p>
	 */
	REMOVE_CONTROLLER;
}
