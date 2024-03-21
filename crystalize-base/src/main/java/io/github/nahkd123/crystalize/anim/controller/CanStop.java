package io.github.nahkd123.crystalize.anim.controller;

import io.github.nahkd123.crystalize.anim.AnimateMode;

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
	 * Check the stop state of this animation controller.
	 * </p>
	 * 
	 * @return The stop state.
	 */
	public boolean isStopped();
}
