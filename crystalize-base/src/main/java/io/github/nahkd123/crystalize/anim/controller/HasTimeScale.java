package io.github.nahkd123.crystalize.anim.controller;

/**
 * <p>
 * An animation controller that implements {@link HasTimeScale} can be used to
 * control the animation speed.
 * </p>
 * 
 * @see #setTimeScale(float)
 * @see #getTimeScale()
 */
public interface HasTimeScale extends AnimationController {
	/**
	 * <p>
	 * Get the time scale.
	 * </p>
	 * 
	 * @return The time scale.
	 * @see #getTimeScale()
	 */
	public float getTimeScale();

	/**
	 * <p>
	 * Dynamically adjust the time scale of this animation controller. At value
	 * {@code 1.0}, the animation plays at {@code 1x} speed. With {@code 2.0}, the
	 * animation play twice as fast as original and so on.
	 * </p>
	 * 
	 * @param timeScale The time scale.
	 * @see #getTimeScale()
	 */
	public void setTimeScale(float timeScale);
}
