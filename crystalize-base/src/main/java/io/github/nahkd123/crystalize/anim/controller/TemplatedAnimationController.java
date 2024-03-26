package io.github.nahkd123.crystalize.anim.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.joml.Vector3f;

import io.github.nahkd123.crystalize.anim.AnimateMode;
import io.github.nahkd123.crystalize.anim.Animation;
import io.github.nahkd123.crystalize.anim.Animator;
import io.github.nahkd123.crystalize.anim.Channel;
import io.github.nahkd123.crystalize.anim.TimelineGroup;

/**
 * <p>
 * Control the bones from predefined animation template.
 * </p>
 * 
 * @see #getMode()
 * @see HasTimeScale#setTimeScale(float)
 * @see CanStop#stop()
 * @see #getInfluence()
 */
public class TemplatedAnimationController implements AnimationController, HasTimeScale, CanStop {
	private Animation animation;
	private AnimateMode mode;
	private boolean stop = false;
	private float timeScale;
	private float influence;
	private float time = 0f;
	private Map<String, Animator> animators = new HashMap<>();

	/**
	 * <p>
	 * Create a new animation controller from animation template.
	 * </p>
	 * 
	 * @param animation    The template.
	 * @param timeScale    The time scale. Use {@code 1.0} for normal speed.
	 * @param influence    How much influence this controller animates your bones.
	 *                     {@code 1.0} means 100% influence, while {@code 0.0}
	 *                     influences nothing (everything stays the same).
	 * @param modeOverride Override the animate mode. Use {@code null} to use
	 *                     animate mode from {@link Animation#mode()}.
	 */
	public TemplatedAnimationController(Animation animation, float timeScale, float influence, AnimateMode modeOverride) {
		Objects.requireNonNull(animation, "animation can't be null");
		this.animation = animation;
		this.timeScale = timeScale;
		this.influence = influence;
		this.mode = modeOverride != null ? modeOverride : animation.mode();
		animation.animators().forEach(animator -> animators.put(animator.getGroupId(), animator));
	}

	/**
	 * <p>
	 * Create a new animation controller from animation template, using default
	 * options (100% speed/1x time scale; 100% influence).
	 * </p>
	 * 
	 * @param animation The template.
	 */
	public TemplatedAnimationController(Animation animation) {
		this(animation, 1f, 1f, null);
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
			Vector3f v = new Vector3f();

			for (Channel channel : Channel.values()) {
				TimelineGroup ch = animator.getChannel(channel);
				Vector3f target = part.getFromChannel(channel);
				channel.transformApply(ch.interpolate(time, v), target, influence);
			}
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

	/**
	 * <p>
	 * Get the influence of this animation controller on model parts. At value
	 * {@code 1.0}, this controller applies 100% of its animation to the parts. At
	 * value {@code 0.0}, this controller will not apply animation on any parts.
	 * </p>
	 * <p>
	 * The influence value accept negative values.
	 * </p>
	 * 
	 * @return The influence value.
	 * @see #setInfluence(float)
	 */
	public float getInfluence() { return influence; }

	public void setInfluence(float influence) { this.influence = influence; }

	@Override
	public String toString() {
		return "TemplatedAnimationController[" + animation.id() + ", " + mode + "]";
	}
}
