package io.github.nahkd123.crystalize.anim;

import java.util.Objects;

import io.github.nahkd123.crystalize.anim.func.Easing;
import io.github.nahkd123.crystalize.anim.func.EasingFunction;

/**
 * <p>
 * A keyframe in timeline. The easing function in this keyframe is for
 * interpolating between previous keyframe and this keyframe. At {@code t = 0},
 * the value is equals to previous keyframe's value, and at {@code t = 1}, the
 * value is equals to this keyframe's value.
 * </p>
 */
public record Keyframe(float time, float value, Easing easing) implements Comparable<Keyframe> {
	/**
	 * @param time   The time where value is equals to {@link #value()}.
	 * @param value  The keyframe value.
	 * @param easing The easing function. At progress {@code 0.0}, the value is
	 *               equals to previous, at {@code 1.0}, the value is equals to
	 *               {@link #value()}.
	 */
	public Keyframe {
		Objects.requireNonNull(easing, "easing must not be null");
	}

	public Keyframe(float time, float value) {
		this(time, value, EasingFunction.LINEAR);
	}

	@Override
	public int compareTo(Keyframe o) {
		return Float.compare(time, o.time);
	}

	@Override
	public String toString() {
		return "(" + time + " => " + value + ", " + easing + ")";
	}
}
