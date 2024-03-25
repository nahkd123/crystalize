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
		public void transformApply(Vector3fc source, Vector3f target) {
			target.add(source);
		}
	},

	ROTATION {
		@Override
		public void transformApply(Vector3fc source, Vector3f target) {
			target.add(source);
		}
	},

	SCALE {
		@Override
		public void transformApply(Vector3fc source, Vector3f target) {
			target.mul(source);
		}
	};

	public abstract void transformApply(Vector3fc source, Vector3f target);
}
