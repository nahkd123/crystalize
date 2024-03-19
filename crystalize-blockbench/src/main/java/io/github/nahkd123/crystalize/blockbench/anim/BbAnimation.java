package io.github.nahkd123.crystalize.blockbench.anim;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.github.nahkd123.crystalize.blockbench.outliner.BbElementGroup;
import io.github.nahkd123.crystalize.utils.Codecs;

public record BbAnimation(String name, LoopMode loopMode, double length, Map<UUID, BbAnimator> animators) {
	/**
	 * @param name      The name of animation. For example: {@code animation.idle},
	 *                  {@code animation.attacking}.
	 * @param loopMode  The animation looping mode. {@link LoopMode#ONCE} will play
	 *                  the animation once, {@link LoopMode#LOOP} will keep looping
	 *                  the animation until you decided to pause/stop the animator.
	 * @param length    The animation duration in seconds.
	 * @param animators A map of group animators, where the key corresponding to the
	 *                  {@link BbElementGroup#uuid()}.
	 */
	public BbAnimation {
		animators = Collections.unmodifiableMap(animators);
	}

	public static final Codec<BbAnimation> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.STRING.fieldOf("name").forGetter(BbAnimation::name),
		LoopMode.CODEC.fieldOf("loop").forGetter(BbAnimation::loopMode),
		Codec.DOUBLE.fieldOf("length").forGetter(BbAnimation::length),
		Codec.unboundedMap(Codecs.UUID, BbAnimator.CODEC).fieldOf("animators").forGetter(BbAnimation::animators))
		.apply(instance, BbAnimation::new));

	@Override
	public String toString() {
		return "Animation[\n"
			+ "  name = " + name + "\n"
			+ "  loopMode = " + loopMode + "\n"
			+ "  length = " + length + "\n"
			+ "  animators = [\n"
			+ animators.entrySet().stream()
				.flatMap(e -> Stream.of((e.getKey() + " = " + e.getValue()).split("\n")))
				.map(s -> "    " + s)
				.collect(Collectors.joining("\n"))
			+ "\n  ]\n"
			+ "]";
	}
}
