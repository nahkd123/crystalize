package io.github.nahkd123.crystalize.anim;

import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 * <p>
 * The animation channel.
 * </p>
 */
public enum Channel {
	TRANSLATION {
		@Override
		public void transformApply(Vector3fc source, Vector3f target, float influence) {
			target.add(source.x() * influence, source.y() * influence, source.z() * influence);
		}
	},

	ROTATION {
		@Override
		public void transformApply(Vector3fc source, Vector3f target, float influence) {
			target.add(source.x() * influence, source.y() * influence, source.z() * influence);
		}
	},

	SCALE {
		@Override
		public void transformApply(Vector3fc source, Vector3f target, float influence) {
			target.mul(
				scaleInfluence(source.x(), influence),
				scaleInfluence(source.y(), influence),
				scaleInfluence(source.z(), influence));
		}
	};

	public abstract void transformApply(Vector3fc source, Vector3f target, float influence);

	private static float scaleInfluence(float source, float influence) {
		return (source - 1f) * influence + 1f;
	}
}
