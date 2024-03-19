package io.github.nahkd123.crystalize.anim.func;

@FunctionalInterface
public interface Parametric extends Easing {
	public void parametric(float t, float[] ds, int offset);

	default float apply(float x, float epsilon) {
		x = Math.max(Math.min(x, 1), 0);
		float start = 0;
		float end = 1;
		float middle = 0.5f;
		float[] ds = new float[2];
		parametric(middle, ds, 0);

		while (Math.abs(ds[0] - x) > epsilon) {
			if (x > ds[0]) start = middle;
			else if (x < ds[0]) end = middle;
			middle = (start + end) / 2;
			parametric(middle, ds, 0);
		}

		return ds[1];
	}

	@Override
	default float apply(float x) {
		return apply(x, 1E-6f);
	}

	default Lagrange compile(int quality) {
		return new Lagrange.Builder().fitParametric(this, quality).build();
	}
}
