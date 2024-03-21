package io.github.nahkd123.crystalize.anim;

public interface AnimateMode {
	public static enum Simple implements AnimateMode {
		/**
		 * <p>
		 * Play the animation once, remove the animation controller once finished.
		 * </p>
		 */
		ONE_SHOT,
		/**
		 * <p>
		 * Play the animation and keep the transformations after the animation is
		 * finished.
		 * </p>
		 */
		HOLD_LAST;
	}

	/**
	 * <p>
	 * Animate mode where the animation is looped between {@code fromTime} to
	 * {@code toTime}. If {@code resumeOnStop} is {@code true}, the controller will
	 * play the rest of animation when a stop request is triggered (ignores end
	 * time).
	 * </p>
	 */
	public record Looping(double fromTime, double toTime, boolean resumeOnStop) implements AnimateMode {
	}
}
