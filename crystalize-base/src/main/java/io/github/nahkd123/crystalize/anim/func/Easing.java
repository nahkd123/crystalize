package io.github.nahkd123.crystalize.anim.func;

/**
 * <p>
 * Easing function, with a {@code y = f(x)} graph.
 * </p>
 */
@FunctionalInterface
public interface Easing {
	public float apply(float x);
}
