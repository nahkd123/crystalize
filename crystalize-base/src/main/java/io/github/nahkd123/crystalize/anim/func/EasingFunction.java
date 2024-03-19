package io.github.nahkd123.crystalize.anim.func;

public enum EasingFunction implements Easing {
	LINEAR {
		@Override
		public float apply(float x) {
			return Math.max(Math.min(x, 1), 0);
		}
	},

	STEP {
		@Override
		public float apply(float x) {
			return 0;
		}
	},

	SINE_IN_OUT {
		@Override
		public float apply(float x) {
			return (float) ((Math.sin(x * Math.PI - Math.PI / 2) + 1) / 2);
		}
	}
}
