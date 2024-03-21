package io.github.nahkd123.crystalize.anim;

/**
 * <p>
 * The animate mode. There are 3 different modes: 2 of which is defined in
 * {@link Simple}, and a {@link Looping} mode for looping the animation between
 * 2 looping points.
 * </p>
 * 
 * @see Simple
 * @see Looping
 */
public interface AnimateMode {
	/**
	 * <p>
	 * Simple animate mode. No need to specify additional parameters.
	 * </p>
	 * 
	 * @see #ONE_SHOT
	 * @see #HOLD_LAST
	 */
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
