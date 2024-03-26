package io.github.nahkd123.crystalize.fabric.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.joml.Vector3f;

import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import io.github.nahkd123.crystalize.anim.controller.AnimateResult;
import io.github.nahkd123.crystalize.anim.controller.AnimationController;
import io.github.nahkd123.crystalize.fabric.CrystalizeFabric;
import io.github.nahkd123.crystalize.model.Element;
import io.github.nahkd123.crystalize.model.ElementGroup;
import io.github.nahkd123.crystalize.model.Model;
import net.minecraft.util.Identifier;

public class CrystalizeElementHolder extends ElementHolder {
	private RegisteredModel base;
	private TranslateStrategy translateStrategy;
	private BonePart root;

	// Animation
	private long lastNano;
	private Set<AnimationController> animationControllers = Collections.newSetFromMap(new ConcurrentHashMap<>());
	private Set<AnimationController> pendingRemoval = Collections.newSetFromMap(new ConcurrentHashMap<>());
	private boolean updatingAnimations = false;

	// Transform
	public final Vector3f modelTranslation = new Vector3f();
	public final Vector3f modelRotation = new Vector3f();
	// TODO model scale here?

	public CrystalizeElementHolder(RegisteredModel base, TranslateStrategy translateStrategy) {
		Objects.requireNonNull(base, "base can't be null");
		this.base = base;
		this.translateStrategy = translateStrategy != null ? translateStrategy : TranslateStrategy.MIXED;
		this.root = createBone(getTemplate().root(), null);
		this.root.computeTree();
		this.root.tickTree();
		this.lastNano = System.nanoTime();
	}

	public CrystalizeElementHolder(RegisteredModel base) {
		this(base, TranslateStrategy.MIXED);
	}

	public CrystalizeElementHolder(Identifier registeredId, TranslateStrategy translateStrategy) {
		Objects.requireNonNull(registeredId, "registeredId can't be null");
		RegisteredModel base = CrystalizeFabric.getInstance().getModelsManager().getModel(registeredId);
		if (base == null) throw new IllegalStateException("Model '" + registeredId + "' is not registered!");

		this.base = base;
		this.translateStrategy = translateStrategy != null ? translateStrategy : TranslateStrategy.MIXED;
		this.root = createBone(getTemplate().root(), null);
		this.root.computeTree();
		this.root.tickTree();
		this.lastNano = System.nanoTime();
	}

	public CrystalizeElementHolder(Identifier registeredId) {
		this(registeredId, TranslateStrategy.MIXED);
	}

	public RegisteredModel getBase() { return base; }

	public Identifier getModelId() { return base.id(); }

	public Model getTemplate() { return base.template(); }

	public BonePart getRoot() { return root; }

	public TranslateStrategy getTranslateStrategy() { return translateStrategy; }

	public void setTranslateStrategy(TranslateStrategy translateStrategy) {
		this.translateStrategy = translateStrategy != null ? translateStrategy : TranslateStrategy.MIXED;
	}

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
		animationTick();
		updateTick();
	}

	public void updateTick() {
		root.tickTree();
	}

	public void animationTick() {
		updateAnimations((float) ((System.nanoTime() - lastNano) / 1_000_000000d));
		lastNano = System.nanoTime();
		root.computeTree();
		pendingRemoval.forEach(animationControllers::remove);
		pendingRemoval.clear();
	}
}
