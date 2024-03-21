package io.github.nahkd123.crystalize.fabric.anim;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.jetbrains.annotations.Nullable;

import io.github.nahkd123.crystalize.anim.AnimateMode;
import io.github.nahkd123.crystalize.anim.Animation;
import io.github.nahkd123.crystalize.anim.Animator;
import io.github.nahkd123.crystalize.fabric.CrystalizeMod;
import io.github.nahkd123.crystalize.fabric.model.BonePart;
import io.github.nahkd123.crystalize.utils.Transformation;

public class TemplatedAnimationController implements AnimationController {
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
	 * @param timeScale    The timescale. Use {@code 1.0} for normal speed.
	 * @param modeOverride Override the animate mode. Use {@code null} to use
	 *                     animate mode from {@link Animation#mode()}.
	 */
	public TemplatedAnimationController(Animation animation, float timeScale, @Nullable AnimateMode modeOverride) {
		Objects.requireNonNull(animation, "animation can't be null");
		this.animation = animation;
		this.timeScale = timeScale;
		this.mode = modeOverride != null ? modeOverride : animation.mode();
		animation.animators().forEach(animator -> animators.put(animator.getGroupId(), animator));
	}

	@Override
	public void animate(BonePart part) {
		Animator animator = animators.get(part.getTemplate().id());

		if (animator != null) {
			Transformation tf = animator.getAt(time);
			part.boneTranslation.add(tf.translate().x(), tf.translate().y(), -tf.translate().z());
			part.boneRotation.add(-tf.rotate().z(), -tf.rotate().y(), tf.rotate().x());
			part.boneScale.mul(tf.scale());
		}
	}

	@Override
	public AnimateResult updateTimeRelative(float deltaTime) {
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

		CrystalizeMod.LOGGER.warn("Unknown animate mode: {}", mode);
		return AnimateResult.REMOVE_CONTROLLER;
	}

	public void stop() {
		stop = true;
	}

	public AnimateMode getMode() { return mode; }

	public boolean isStop() { return stop; }

	public float getTimeScale() { return timeScale; }

	public void setTimeScale(float timeScale) { this.timeScale = timeScale; }
}
