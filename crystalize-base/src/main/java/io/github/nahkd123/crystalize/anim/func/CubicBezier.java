package io.github.nahkd123.crystalize.anim.func;

import java.text.DecimalFormat;

public record CubicBezier(float ax, float ay, float bx, float by, float cx, float cy, float dx, float dy) implements Parametric {
	@Override
	public void parametric(float t, float[] ds, int offset) {
		if (ds.length < offset + 2)
			throw new IndexOutOfBoundsException("length < offset + 2 (" + ds.length + " < " + offset + 2 + ")");

		float abx = ax + t * (bx - ax), aby = ay + t * (by - ay);
		float bcx = bx + t * (cx - bx), bcy = by + t * (cy - by);
		float cdx = cx + t * (dx - cx), cdy = cy + t * (dy - cy);
		float abcx = abx + t * (bcx - abx), abcy = aby + t * (bcy - aby);
		float bcdx = bcx + t * (cdx - bcx), bcdy = bcy + t * (cdy - bcy);
		ds[offset] = abcx + t * (bcdx - abcx);
		ds[offset + 1] = abcy + t * (bcdy - abcy);
	}

	private static final DecimalFormat FORMAT = new DecimalFormat("#,##0.##");

	@Override
	public String toString() {
		return "CubicBezier["
			+ "(" + FORMAT.format(ax) + "; " + FORMAT.format(ay) + "), "
			+ "(" + FORMAT.format(bx) + "; " + FORMAT.format(by) + "), "
			+ "(" + FORMAT.format(cx) + "; " + FORMAT.format(cy) + "), "
			+ "(" + FORMAT.format(dx) + "; " + FORMAT.format(dy) + ")"
			+ "]";
	}
}
