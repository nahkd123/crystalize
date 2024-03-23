package io.github.nahkd123.crystalize.anim.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.github.nahkd123.crystalize.anim.AnimateMode;
import io.github.nahkd123.crystalize.anim.Animation;
import io.github.nahkd123.crystalize.anim.Animator;
import io.github.nahkd123.crystalize.utils.Transformation;

/**
 * <p>
 * Control the bones from predefined animation template.
 * </p>
 * 
 * @see #getMode()
 * @see HasTimeScale#setTimeScale(float)
 * @see CanStop#stop()
 */
public class TemplatedAnimationController implements AnimationController, HasTimeScale, CanStop {
	private Animation animation;
	private AnimateMode mode;
	private boolean stop = false;
	private float timeScale;
	private float time = 0f;
	private Map<String, Animator> animators = new HashMap<>();

	/**
	 * <p>
	 * Create a new animation controller from animation template.
	 * </p>
	 * 
	 * @param animation    The template.
	 * @param timeScale    The time scale. Use {@code 1.0} for normal speed.
	 * @param modeOverride Override the animate mode. Use {@code null} to use
	 *                     animate mode from {@link Animation#mode()}.
	 */
	public TemplatedAnimationController(Animation animation, float timeScale, AnimateMode modeOverride) {
		Objects.requireNonNull(animation, "animation can't be null");
		this.animation = animation;
		this.timeScale = timeScale;
		this.mode = modeOverride != null ? modeOverride : animation.mode();
		animation.animators().forEach(animator -> animators.put(animator.getGroupId(), animator));
	}

	@Override
	public AnimateResult updateTimeRelative(float deltaTime, AnimatableBone root) {
		time += deltaTime * timeScale;

		if (mode == AnimateMode.Simple.ONE_SHOT) {
			if (stop || time > animation.duration()) return AnimateResult.REMOVE_CONTROLLER;
			return AnimateResult.CONTINUE;
		}

		if (mode == AnimateMode.Simple.HOLD_LAST) {
			if (stop) return AnimateResult.REMOVE_CONTROLLER;
			time = (float) Math.min(time, animation.duration());
			return AnimateResult.CONTINUE;
		}

		if (mode instanceof AnimateMode.Looping looping) {
			if (stop) {
				if (!looping.resumeOnStop() || time > animation.duration()) return AnimateResult.REMOVE_CONTROLLER;
				return AnimateResult.CONTINUE;
			}

			double loopProgressed = time - looping.fromTime();
			if (loopProgressed < 0f) return AnimateResult.CONTINUE; // Before looping region

			double loopDuration = looping.toTime() - looping.fromTime();
			double tMinus = Math.floor(loopProgressed / loopDuration) * loopDuration;
			time -= tMinus;

			return AnimateResult.CONTINUE;
		}

		// TODO logger
		return AnimateResult.REMOVE_CONTROLLER;
	}

	@Override
	public void animate(AnimatableBone part) {
		Animator animator = animators.get(part.getAnimatorId());

		if (animator != null) {
			Transformation tf = animator.getAt(time);
			part.getTranslation().add(tf.translate().x(), tf.translate().y(), -tf.translate().z());
			part.getRotation().add(-tf.rotate().z(), -tf.rotate().y(), tf.rotate().x());
			part.getScale().mul(tf.scale());
		}
	}

	/**
	 * <p>
	 * Get the current animation mode. See {@link AnimateMode} for more information.
	 * </p>
	 * 
	 * @return The animation mode.
	 */
	public AnimateMode getMode() { return mode; }

	@Override
	public boolean isStopped() { return stop; }

	@Override
	public void stop() {
		stop = true;
	}

	@Override
	public float getTimeScale() { return timeScale; }

	@Override
	public void setTimeScale(float timeScale) { this.timeScale = timeScale; }

	@Override
	public String toString() {
		return "TemplatedAnimationController[" + animation.id() + ", " + mode + "]";
	}
}
