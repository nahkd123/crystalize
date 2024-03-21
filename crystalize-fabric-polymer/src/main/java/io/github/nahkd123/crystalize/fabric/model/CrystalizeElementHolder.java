package io.github.nahkd123.crystalize.fabric.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.joml.Vector3f;

import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import io.github.nahkd123.crystalize.anim.controller.AnimateResult;
import io.github.nahkd123.crystalize.anim.controller.AnimationController;
import io.github.nahkd123.crystalize.model.Element;
import io.github.nahkd123.crystalize.model.ElementGroup;
import io.github.nahkd123.crystalize.model.Model;
import net.minecraft.util.Identifier;

public class CrystalizeElementHolder extends ElementHolder {
	private RegisteredModel base;
	private BonePart root;
	private Set<AnimationController> animationControllers = new HashSet<>();
	private Set<AnimationController> pendingRemoval = new HashSet<>();
	private boolean updatingAnimations = false;
	private long lastNano;

	public final Vector3f modelTranslation = new Vector3f();
	public final Vector3f modelRotation = new Vector3f();
	// TODO model scale here?

	public CrystalizeElementHolder(RegisteredModel base) {
		this.base = base;
		this.root = createBone(getTemplate().root(), null);
		this.root.updateTree();
		this.lastNano = System.nanoTime();
	}

	public RegisteredModel getBase() { return base; }

	public Identifier getModelId() { return base.id(); }

	public Model getTemplate() { return base.template(); }

	public BonePart getRoot() { return root; }

	public Collection<AnimationController> getAnimationControllers() {
		return Collections.unmodifiableCollection(animationControllers);
	}

	public void addAnimation(AnimationController controller) {
		animationControllers.add(controller);
	}

	public void removeAnimation(AnimationController controller) {
		if (updatingAnimations) pendingRemoval.add(controller);
		else animationControllers.remove(controller);
	}

	private BonePart createBone(ElementGroup template, BonePart parent) {
		ItemDisplayElement display = new ItemDisplayElement(base.getItemFor(template));
		BonePart part = new BonePart(this, parent, template, display);

		for (Element child : template.children()) {
			if (child instanceof ElementGroup childGroup) {
				BonePart childBone = createBone(childGroup, part);
				part.getChildren().add(childBone);
			}
		}

		addElement(display);
		addElement(part.getDebugAxes());
		return part;
	}

	protected void updateAnimations(float deltaTime) {
		if (updatingAnimations) return;
		updatingAnimations = true;

		try {
			animationControllers.forEach(controller -> {
				AnimateResult result = controller.updateTimeRelative(deltaTime, root);

				switch (result) {
				case REMOVE_CONTROLLER -> removeAnimation(controller);
				default -> {}
				}
			});
		} finally {
			updatingAnimations = false;
		}
	}

	@Override
	protected void onTick() {
		updateAnimations((float) ((System.nanoTime() - lastNano) / 1_000_000000d));
		lastNano = System.nanoTime();
		root.updateTree();

		// Cleanup
		pendingRemoval.forEach(animationControllers::remove);
		pendingRemoval.clear();
	}
}
