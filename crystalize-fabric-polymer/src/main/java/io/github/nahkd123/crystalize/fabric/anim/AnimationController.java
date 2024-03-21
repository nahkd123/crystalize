package io.github.nahkd123.crystalize.fabric.anim;

import io.github.nahkd123.crystalize.fabric.model.BonePart;

public interface AnimationController {
	public AnimateResult updateTimeRelative(float deltaTime);

	public void animate(BonePart part);
}
