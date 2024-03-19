package io.github.nahkd123.crystalize.fabric.anim;

import io.github.nahkd123.crystalize.fabric.model.BonePart;

public interface AnimationController {
	public AnimateResult animate(float deltaTime, BonePart part);
}
