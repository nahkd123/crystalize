package io.github.nahkd123.crystalize.fabric.model;

import java.util.ArrayList;
import java.util.List;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import io.github.nahkd123.crystalize.anim.controller.AnimatableBone;
import io.github.nahkd123.crystalize.anim.controller.AnimationController;
import io.github.nahkd123.crystalize.model.ElementGroup;
import net.minecraft.util.math.Vec3d;

public class BonePart implements AnimatableBone {
	private CrystalizeElementHolder holder;
	private BonePart parent;
	private ElementGroup template;
	private ItemDisplayElement display;
	private List<BonePart> children = new ArrayList<>();

	private final Vector3f boneOrigin = new Vector3f();
	public final Vector3f boneTranslation = new Vector3f();
	public final Vector3f boneRotation = new Vector3f();
	public final Vector3f boneScale = new Vector3f();

	// Bandage interpolation fix
	private Vector3f lastComputedTranslate = new Vector3f();
	private Vector3f lastBoneRotation = new Vector3f();
	private Vector3f lastBoneScale = new Vector3f();
	private Vector3f lastModelRot = new Vector3f();

	// Reduce send packet calls
	private Vec3d lastEntityOffset = null;

	public BonePart(CrystalizeElementHolder holder, BonePart parent, ElementGroup template, ItemDisplayElement display) {
		this.holder = holder;
		this.parent = parent;
		this.template = template;
		this.display = display;
	}

	public CrystalizeElementHolder getHolder() { return holder; }

	public BonePart getParent() { return parent; }

	@Override
	public ElementGroup getTemplate() { return template; }

	public ItemDisplayElement getDisplay() { return display; }

	@Override
	public List<BonePart> getChildren() { return children; }

	@Override
	public String getAnimatorId() { return template.id(); }

	@Override
	public Vector3f getOrigin() { return boneOrigin; }

	@Override
	public Vector3f getTranslation() { return boneTranslation; }

	@Override
	public Vector3f getRotation() { return boneRotation; }

	@Override
	public Vector3f getScale() { return boneScale; }

	/**
	 * <p>
	 * Update position and display transformations for this bone, along with its
	 * children.
	 * </p>
	 */
	public void updateTree() {
		if (parent != null) {
			boneOrigin
				.set(parent.boneOrigin)
				.add(parent.boneTranslation.rotate(new Quaternionf()
					.rotateZYX(parent.boneRotation.x, parent.boneRotation.y, parent.boneRotation.z)));
			boneTranslation.zero();
			boneRotation.set(parent.boneRotation);
			boneScale.set(parent.boneScale);
		} else {
			boneOrigin.zero();
			boneTranslation.zero();
			boneRotation.zero();
			boneScale.set(1, 1, 1);
		}

		Vector3f offset = new Vector3f(template.origin())
			.mul(-1f, 1f, -1f)
			.mul(1f / 16f)
			.rotate(new Quaternionf().rotateZYX(boneRotation.x, boneRotation.y, boneRotation.z));
		boneOrigin.add(offset);
		boneRotation.add(template.rotation());
		for (AnimationController controller : holder.getAnimationControllers()) controller.animate(this);

		Quaternionf modelRot = new Quaternionf()
			.rotateZYX(holder.modelRotation.x, holder.modelRotation.y, holder.modelRotation.z);
		Vector3f computedTranslate = new Vector3f(boneOrigin)
			.add(boneTranslation)
			.rotate(modelRot)
			.add(holder.modelTranslation);
		Vec3d entityOffset;

		switch (holder.getTranslateStrategy()) {
		case POSITION_ONLY:
			entityOffset = new Vec3d(computedTranslate);
			computedTranslate.zero();
			break;
		case TRANSLATION_ONLY:
			entityOffset = Vec3d.ZERO;
			break;
		case MIXED:
		default:
			computedTranslate.set(boneTranslation).rotate(modelRot);
			entityOffset = new Vec3d(new Vector3f(boneOrigin).rotate(modelRot).add(holder.modelTranslation));
			break;
		}

		if (lastEntityOffset == null || !lastEntityOffset.equals(entityOffset)) {
			display.setOffset(entityOffset);
			lastEntityOffset = entityOffset;
		}

		applyTransformations(computedTranslate, modelRot);
		for (BonePart child : children) child.updateTree();
	}

	private void applyTransformations(Vector3f computedTranslate, Quaternionf modelRotation) {
		boolean translateChanged = !lastComputedTranslate.equals(computedTranslate);
		boolean rotationChanged = !lastBoneRotation.equals(boneRotation);
		boolean scaleChanged = !lastBoneScale.equals(boneScale);
		boolean modelRotChanged = !lastModelRot.equals(holder.modelRotation);
		boolean interpolation = translateChanged || rotationChanged || scaleChanged || modelRotChanged;

		if (translateChanged)
			display.setTranslation(computedTranslate);
		if (rotationChanged)
			display.setRightRotation(new Quaternionf().rotateZYX(boneRotation.x, boneRotation.y, boneRotation.z));
		if (scaleChanged)
			display.setScale(boneScale);
		if (modelRotChanged)
			display.setLeftRotation(modelRotation);

		if (holder.getTranslateStrategy() != TranslateStrategy.POSITION_ONLY && interpolation) {
			display.setStartInterpolation(0);
			display.setInterpolationDuration(1);
		}

		if (holder.getTranslateStrategy() == TranslateStrategy.POSITION_ONLY) display.setInterpolationDuration(0);

		lastComputedTranslate.set(computedTranslate);
		lastBoneRotation.set(boneRotation);
		lastBoneScale.set(boneScale);
		lastModelRot.set(holder.modelRotation);
	}
}
