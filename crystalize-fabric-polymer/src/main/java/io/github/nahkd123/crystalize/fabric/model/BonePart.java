package io.github.nahkd123.crystalize.fabric.model;

import java.util.ArrayList;
import java.util.List;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import io.github.nahkd123.crystalize.anim.controller.AnimatableBone;
import io.github.nahkd123.crystalize.anim.controller.AnimationController;
import io.github.nahkd123.crystalize.fabric.debug.DebugDisplayModels;
import io.github.nahkd123.crystalize.model.ElementGroup;
import net.minecraft.entity.decoration.Brightness;
import net.minecraft.util.math.Vec3d;

public class BonePart implements AnimatableBone {
	private CrystalizeElementHolder holder;
	private BonePart parent;
	private ElementGroup template;
	private ItemDisplayElement display;
	private ItemDisplayElement debugAxes;
	private List<BonePart> children = new ArrayList<>();

	private final Vector3f boneOrigin = new Vector3f();
	public final Vector3f boneTranslation = new Vector3f();
	public final Vector3f boneRotation = new Vector3f();
	public final Vector3f boneScale = new Vector3f();

	public BonePart(CrystalizeElementHolder holder, BonePart parent, ElementGroup template, ItemDisplayElement display) {
		this.holder = holder;
		this.parent = parent;
		this.template = template;
		this.display = display;
		this.debugAxes = new ItemDisplayElement(DebugDisplayModels.getAxesModel());
	}

	public CrystalizeElementHolder getHolder() { return holder; }

	public BonePart getParent() { return parent; }

	public ElementGroup getTemplate() { return template; }

	public ItemDisplayElement getDisplay() { return display; }

	public ItemDisplayElement getDebugAxes() { return debugAxes; }

	@Override
	public List<BonePart> getChildren() { return children; }

	@Override
	public String getAnimatorId() { return template.id(); }

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

		switch (holder.getTranslateStrategy()) {
		case POSITION_ONLY:
			display.setOffset(new Vec3d(computedTranslate));
			display.setTranslation(new Vector3f(0, 0, 0));
			display.setStartInterpolation(0);
			display.setInterpolationDuration(0);
			break;
		case TRANSLATION_ONLY:
			display.setOffset(Vec3d.ZERO);
			display.setTranslation(computedTranslate);
			display.setStartInterpolation(0);
			display.setInterpolationDuration(1);
			break;
		case MIXED:
		default:
			display.setOffset(new Vec3d(new Vector3f(boneOrigin).rotate(modelRot).add(holder.modelTranslation)));
			display.setTranslation(new Vector3f(boneTranslation).rotate(modelRot));
			display.setStartInterpolation(0);
			display.setInterpolationDuration(1);
			break;
		}

		display.setRightRotation(new Quaternionf().rotateZYX(boneRotation.x, boneRotation.y, boneRotation.z));
		display.setLeftRotation(modelRot);
		display.setScale(boneScale);

		// Copy to debug axes
		debugAxes.setOffset(display.getOffset());
		debugAxes.setTranslation(display.getTranslation());
		debugAxes.setLeftRotation(display.getLeftRotation());
		debugAxes.setRightRotation(display.getRightRotation());
		debugAxes.setScale(display.getScale().mul(0.1f, new Vector3f()));
		debugAxes.setBrightness(new Brightness(15, 15));
		holder.removeElement(debugAxes);

		for (BonePart child : children) child.updateTree();
	}
}
