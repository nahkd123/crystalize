package io.github.nahkd123.crystalize.anim.controller;

import io.github.nahkd123.crystalize.anim.AnimateMode;

/**
 * <p>
 * An animation controller that implements {@link CanStop} can be stopped by
 * using {@link #stop()}. The controller may choose to keep playing animation
 * until the end or interpolate from current animation state to initial state
 * (or zero state).
 * </p>
 * 
 * @see #stop()
 * @see #isStopped()
 */
public interface CanStop extends AnimationController {
	/**
	 * <p>
	 * Stop this animation controller. This is not the same as remove animation
	 * method from model implementation, where the animation is completely stopped
	 * regardless the {@link AnimateMode} option.
	 * </p>
	 * <p>
	 * For example, in {@link TemplatedAnimationController}, stopping with animate
	 * mode {@link AnimateMode.Looping#resumeOnStop()} will not remove the
	 * controller immediately; it will keep playing animation past looping end time
	 * until it reaches the end of animation.
	 * </p>
	 */
	public void stop();

	/**
	 * <p>
	 * Check the stop state of this animation controller. See {@link #stop()} for
	 * more info.
	 * </p>
	 * 
	 * @return The stop state.
	 */
	public boolean isStopped();
}
