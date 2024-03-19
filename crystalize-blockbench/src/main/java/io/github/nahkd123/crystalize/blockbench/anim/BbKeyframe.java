package io.github.nahkd123.crystalize.blockbench.anim;

import java.util.Arrays;

import org.joml.Vector3f;
import org.joml.Vector3fc;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.github.nahkd123.crystalize.utils.Codecs;

public record BbKeyframe(String channel, double time, Vector3fc data, InterpolationMode interpolation, Vector3fc bezierLeftTime, Vector3fc bezierLeftValue, Vector3fc bezierRightTime, Vector3fc bezierRightValue) {

	public static final Codec<BbKeyframe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.STRING.fieldOf("channel").forGetter(BbKeyframe::channel),
		Codec.DOUBLE.fieldOf("time").forGetter(BbKeyframe::time),
		Codecs.fixedList(Codecs.VECTOR_3F_MAP, 1)
			.xmap(list -> (Vector3fc) list.get(0), vector -> Arrays.asList(new Vector3f(vector)))
			.fieldOf("data_points")
			.forGetter(BbKeyframe::data),
		InterpolationMode.CODEC.fieldOf("interpolation").forGetter(BbKeyframe::interpolation),
		Codecs.VECTOR_3FC.fieldOf("bezier_left_time").forGetter(BbKeyframe::bezierLeftTime),
		Codecs.VECTOR_3FC.fieldOf("bezier_left_value").forGetter(BbKeyframe::bezierLeftValue),
		Codecs.VECTOR_3FC.fieldOf("bezier_right_time").forGetter(BbKeyframe::bezierRightTime),
		Codecs.VECTOR_3FC.fieldOf("bezier_right_value").forGetter(BbKeyframe::bezierRightValue))
		.apply(instance, BbKeyframe::new));

	@Override
	public String toString() {
		return "["
			+ channel + ", "
			+ "t = " + time + "s, "
			+ "value = " + data + ", "
			+ interpolation.toString().toLowerCase() + ", "
			+ "bezierPrev = [" + bezierLeftTime + ", " + bezierLeftValue + "], "
			+ "bezierNext = [" + bezierRightTime + ", " + bezierRightValue + "]"
			+ "]";
	}
}
