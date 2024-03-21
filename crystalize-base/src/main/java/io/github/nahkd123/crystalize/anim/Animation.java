package io.github.nahkd123.crystalize.anim;

import java.util.Collections;
import java.util.List;

import io.github.nahkd123.crystalize.model.ElementGroup;

public record Animation(String id, AnimateMode mode, double duration, List<Animator> animators) {
	/**
	 * <p>
	 * Make a new animation.
	 * </p>
	 * 
	 * @param id        The ID of this animation.
	 * @param mode      The animation looping mode. Use
	 *                  {@link AnimateMode.Simple#ONE_SHOT} to animate the model
	 *                  once, {@link AnimateMode.Simple#HOLD_LAST} to keep the last
	 *                  transformation once the animation is finished playing (until
	 *                  you stop the animator), or {@link AnimateMode.Looping} to
	 *                  loop in defined range.
	 * @param duration  The animation duration in seconds.
	 * @param animators A list of animators that transforms {@link ElementGroup}.
	 *                  Each animator transforms a single {@link ElementGroup}.
	 */
	public Animation {
		animators = Collections.unmodifiableList(animators);
	}
}
