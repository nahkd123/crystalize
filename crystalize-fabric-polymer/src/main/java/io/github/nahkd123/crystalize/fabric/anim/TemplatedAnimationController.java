package io.github.nahkd123.crystalize.fabric.anim;

import java.util.HashMap;
import java.util.Map;

import org.joml.Quaternionf;

import io.github.nahkd123.crystalize.anim.Animation;
import io.github.nahkd123.crystalize.anim.Animator;
import io.github.nahkd123.crystalize.fabric.model.BonePart;
import io.github.nahkd123.crystalize.utils.Transformation;

public class TemplatedAnimationController implements AnimationController {
	private Animation animation;
	private boolean loop;
	private boolean stop = false;
	private float timeScale;
	private float time = 0f;
	private Map<String, Animator> animators = new HashMap<>();

	public TemplatedAnimationController(Animation animation, float timeScale, Boolean loopOverride) {
		this.animation = animation;
		this.timeScale = timeScale;
		this.loop = loopOverride != null ? loopOverride : animation.loop();
		animation.animators().forEach(animator -> animators.put(animator.getGroupId(), animator));
	}

	@Override
	public AnimateResult animate(float deltaTime, BonePart part) {
		if (stop) return AnimateResult.REMOVE_CONTROLLER;
		float last = time;
		time += deltaTime * timeScale;
		AnimateResult result = AnimateResult.CONTINUE;

		if (!loop && time >= animation.duration()) {
			result = AnimateResult.REMOVE_CONTROLLER;
			stop = true;
		} else if (loop) time = time % ((float) animation.duration());

		Animator animator = animators.get(part.getTemplate().id());

		if (animator != null) {
			Transformation tf = animator.getAt(last);
			part.boneTranslation.add(tf.translate());
			part.boneRotation.mul(new Quaternionf().rotateXYZ(tf.rotate().x(), tf.rotate().y(), tf.rotate().z()));
			part.boneScale.mul(tf.scale());
		}

		return result;
	}

	public void stop() {
		stop = true;
	}

	public boolean isLoop() { return loop; }

	public void setLoop(boolean loop) { this.loop = loop; }

	public boolean isStop() { return stop; }

	public float getTimeScale() { return timeScale; }

	public void setTimeScale(float timeScale) { this.timeScale = timeScale; }
}
