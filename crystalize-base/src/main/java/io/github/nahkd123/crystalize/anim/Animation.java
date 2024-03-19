package io.github.nahkd123.crystalize.anim;

import java.util.Collections;
import java.util.List;

import io.github.nahkd123.crystalize.model.ElementGroup;

public record Animation(String id, boolean loop, double duration, List<Animator> animators) {
	/**
	 * <p>
	 * Make a new animation.
	 * </p>
	 * 
	 * @param id        The ID of this animation.
	 * @param loop      The animation loop mode. {@code true} will keep the animator
	 *                  looping until you decided to stop or pause.
	 * @param duration  The animation duration in seconds.
	 * @param animators A list of animators that transforms {@link ElementGroup}.
	 *                  Each animator transforms a single {@link ElementGroup}.
	 */
	public Animation {
		animators = Collections.unmodifiableList(animators);
	}
}
