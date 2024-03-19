package io.github.nahkd123.crystalize.anim.func;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Lagrange interpolation. Usually created from {@link Lagrange.Builder} and
 * constructed from {@link CubicBezier} by using
 * {@link Lagrange.Builder#fitParametric(Parametric, int)}.
 * </p>
 */
public class Lagrange implements Easing {
	private float[] xs;
	private float[] constants;

	public Lagrange(float[] xs, float[] constants) {
		this.xs = xs;
		this.constants = constants;
	}

	public static class Builder {
		private List<float[]> nodes = new ArrayList<>();

		public Builder addNode(float[] node) {
			nodes.add(node);
			return this;
		}

		public Builder addNode(float x, float y) {
			nodes.add(new float[] { x, y });
			return this;
		}

		public Builder fitParametric(Parametric function, int nodes) {
			for (int i = 0; i < nodes; i++) {
				float t = ((float) i) / (nodes - 1);
				float[] xy = new float[2];
				function.parametric(t, xy, 0);
				addNode(xy);
			}

			return this;
		}

		public Lagrange build() {
			if (nodes.size() < 2) throw new IllegalStateException("Must have at least 2 nodes");
			float[] xs = new float[nodes.size()];
			float[] constants = new float[nodes.size()];

			for (int i = 0; i < nodes.size(); i++) {
				float[] xy = nodes.get(i);
				xs[i] = xy[0];
				float products = 1;

				for (int j = 0; j < nodes.size(); j++) {
					if (i == j) continue;
					products *= xy[0] - nodes.get(j)[0];
				}

				constants[i] = xy[1] / products;
			}

			return new Lagrange(xs, constants);
		}
	}

	@Override
	public float apply(float x) {
		float sum = 0;

		for (int i = 0; i < xs.length; i++) {
			float products = constants[i];

			for (int j = 0; j < xs.length; j++) {
				if (i == j) continue;
				products *= x - xs[j];
			}

			sum += products;
		}

		return sum;
	}
}
