package io.github.nahkd123.crystalize.blockbench.anim;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record BbAnimator(boolean globalRotation, List<BbKeyframe> keyframes) {

	public BbAnimator {
		keyframes = Collections.unmodifiableList(keyframes);
	}

	public static final Codec<BbAnimator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.BOOL.optionalFieldOf("rotation_global", false).forGetter(BbAnimator::globalRotation),
		BbKeyframe.CODEC.listOf().fieldOf("keyframes").forGetter(BbAnimator::keyframes))
		.apply(instance, BbAnimator::new));

	@Override
	public String toString() {
		return "BbAnimator[\n"
			+ "  globalRotation = " + globalRotation + "\n"
			+ "  keyframes = [\n"
			+ keyframes.stream()
				.flatMap(kf -> Stream.of(kf.toString().split("\n")))
				.map(s -> "    " + s)
				.collect(Collectors.joining("\n"))
			+ "\n  ]\n"
			+ "]";
	}
}
