package io.github.nahkd123.crystalize.blockbench.build;

import static io.github.nahkd123.crystalize.anim.Subchannel.X;
import static io.github.nahkd123.crystalize.anim.Subchannel.Y;
import static io.github.nahkd123.crystalize.anim.Subchannel.Z;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.joml.Vector3f;
import org.joml.Vector3fc;

import io.github.nahkd123.crystalize.anim.AnimateMode;
import io.github.nahkd123.crystalize.anim.Animation;
import io.github.nahkd123.crystalize.anim.Animator;
import io.github.nahkd123.crystalize.anim.Channel;
import io.github.nahkd123.crystalize.anim.Keyframe;
import io.github.nahkd123.crystalize.anim.TimelineGroup;
import io.github.nahkd123.crystalize.anim.func.Easing;
import io.github.nahkd123.crystalize.anim.func.EasingFunction;
import io.github.nahkd123.crystalize.blockbench.anim.BbAnimation;
import io.github.nahkd123.crystalize.blockbench.anim.BbAnimator;
import io.github.nahkd123.crystalize.blockbench.anim.BbKeyframe;
import io.github.nahkd123.crystalize.blockbench.anim.InterpolationMode;
import io.github.nahkd123.crystalize.blockbench.anim.LoopMode;

public class BlockbenchAnimationBuilder {
	private static final Vector3f ZERO = new Vector3f();
	private static final Vector3f ONE = new Vector3f(1, 1, 1);

	public static Animation buildAnimation(BbAnimation source) {
		AnimateMode mode = source.loopMode() == LoopMode.ONCE
			? AnimateMode.Simple.ONE_SHOT
			: source.loopMode() == LoopMode.HOLD ? AnimateMode.Simple.HOLD_LAST
			: new AnimateMode.Looping(0, source.length(), false);
		double duration = source.length();
		List<Animator> animators = new ArrayList<>();
		for (Map.Entry<UUID, BbAnimator> e : source.animators().entrySet())
			animators.add(buildAnimator(e.getKey(), e.getValue()));
		return new Animation(source.name(), mode, duration, animators);
	}

	public static Animator buildAnimator(UUID target, BbAnimator source) {
		String groupId = target.toString();
		Animator animator = new Animator(groupId);

		List<BbKeyframe> sourceKeyframes = new ArrayList<>();
		sourceKeyframes.addAll(source.keyframes());
		Collections.sort(sourceKeyframes, Comparator.comparingDouble(k -> k.time()));

		// @formatter:off
		BbKeyframe lastPosition = new BbKeyframe(
			"position", 0d, ZERO, InterpolationMode.STEP,
			ZERO, ZERO,
			ZERO, ZERO);
		BbKeyframe lastRotation = new BbKeyframe(
			"rotation", 0d, ZERO, InterpolationMode.STEP,
			ZERO, ZERO,
			ZERO, ZERO);
		BbKeyframe lastScale = new BbKeyframe(
			"scale", 0d, ONE, InterpolationMode.STEP,
			ONE, ONE,
			ONE, ONE);
		// @formatter:on

		for (BbKeyframe sourceKf : sourceKeyframes) {
			Channel channel = switch (sourceKf.channel()) {
			case "position", "translation" -> Channel.TRANSLATION;
			case "rotation" -> Channel.ROTATION;
			case "scale" -> Channel.SCALE;
			default -> null;
			};

			if (channel == null) continue;
			BbKeyframe last = switch (channel) {
			case TRANSLATION -> lastPosition;
			case ROTATION -> lastRotation;
			case SCALE -> lastScale;
			};
			TimelineGroup group = animator.getChannel(channel);
			Easing easing = switch (sourceKf.interpolation()) {
			case STEP -> EasingFunction.STEP;
			case LINEAR -> EasingFunction.LINEAR;
			case CATMULLROM -> EasingFunction.SINE_IN_OUT;
			case BEZIER -> EasingFunction.SINE_IN_OUT; // TODO convert easings
			default -> EasingFunction.LINEAR;
			};

			setKeyframe((float) sourceKf.time(), sourceKf.data(), easing, group);

			switch (channel) {
			case TRANSLATION -> lastPosition = sourceKf;
			case ROTATION -> lastRotation = sourceKf;
			case SCALE -> lastScale = sourceKf;
			}
		}

		return animator;
	}

	private static void setKeyframe(float time, Vector3fc data, Easing easing, TimelineGroup group) {
		float x = mapFor(group.getChannel(), data.x());
		float y = mapFor(group.getChannel(), data.y());
		float z = mapFor(group.getChannel(), data.z());

		switch (group.getChannel()) {
		case TRANSLATION -> { z = -z; }
		case ROTATION -> {
			float auxX = x, auxY = y, auxZ = z;
			x = -auxZ;
			y = -auxY;
			z = auxX;
		}
		default -> {}
		}

		group.getSubchannel(X).setKeyframe(new Keyframe(time, x, easing));
		group.getSubchannel(Y).setKeyframe(new Keyframe(time, y, easing));
		group.getSubchannel(Z).setKeyframe(new Keyframe(time, z, easing));
	}

	private static float mapFor(Channel channel, float x) {
		return switch (channel) {
		case TRANSLATION -> x / 16f;
		case ROTATION -> (float) Math.toRadians(x);
		default -> x;
		};
	}
}
