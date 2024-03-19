package io.github.nahkd123.crystalize.fabric.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import io.github.nahkd123.crystalize.fabric.anim.AnimationController;
import io.github.nahkd123.crystalize.model.Element;
import io.github.nahkd123.crystalize.model.ElementGroup;
import io.github.nahkd123.crystalize.model.Model;
import net.minecraft.util.Identifier;

public class CrystalizeElementHolder extends ElementHolder {
	private RegisteredModel base;
	private BonePart root;
	private Set<AnimationController> animationControllers = new HashSet<>();
	private Set<AnimationController> pendingRemoval = new HashSet<>();
	private boolean updatingTree = false;
	private long lastNano;

	public CrystalizeElementHolder(RegisteredModel base) {
		this.base = base;
		this.root = createBone(getTemplate().root(), null);
		this.root.updateTree(0f);
		this.lastNano = System.nanoTime();
	}

	public RegisteredModel getBase() { return base; }

	public Identifier getModelId() { return base.id(); }

	public Model getTemplate() { return base.template(); }

	public BonePart getRoot() { return root; }

	public Collection<AnimationController> getAnimationControllers() {
		return Collections.unmodifiableCollection(animationControllers);
	}

	public void playAnimation(AnimationController controller) {
		animationControllers.add(controller);
	}

	public void stopAnimation(AnimationController controller) {
		if (updatingTree) pendingRemoval.add(controller);
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

	@Override
	protected void onTick() {
		updatingTree = true;
		long now = System.nanoTime();
		root.updateTree((float) ((now - lastNano) / 1_000_000000d));
		lastNano = now;
		updatingTree = false;

		pendingRemoval.forEach(animationControllers::remove);
		pendingRemoval.clear();
	}
}
