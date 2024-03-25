package io.github.nahkd123.crystalize.bukkit.model;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import io.github.nahkd123.crystalize.anim.controller.AnimatableBone;
import io.github.nahkd123.crystalize.model.ElementGroup;

public class BonePart implements AnimatableBone {
	private BukkitModel holder;
	private BonePart parent;
	private ItemDisplay display;
	private ElementGroup template;
	private List<BonePart> children = new ArrayList<>();

	public final Vector3f boneOrigin = new Vector3f();
	public final Vector3f boneTranslation = new Vector3f();
	public final Vector3f boneRotation = new Vector3f();
	public final Vector3f boneScale = new Vector3f();
	private final Transformation outTransformation;

	public BonePart(BukkitModel holder, BonePart parent, ItemDisplay display, ElementGroup template) {
		this.holder = holder;
		this.parent = parent;
		this.display = display;
		this.template = template;
		this.outTransformation = new Transformation(new Vector3f(), new Quaternionf(), new Vector3f(), new Quaternionf());
	}

	public BukkitModel getHolder() { return holder; }

	public BonePart getParent() { return parent; }

	public ItemDisplay getDisplay() { return display; }

	@Override
	public String getAnimatorId() { return template.id(); }

	@Override
	public List<BonePart> getChildren() { return children; }

	@Override
	public ElementGroup getTemplate() { return template; }

	@Override
	public Vector3f getOrigin() { return boneOrigin; }

	@Override
	public Vector3f getTranslation() { return boneTranslation; }

	@Override
	public Vector3f getRotation() { return boneRotation; }

	@Override
	public Vector3f getScale() { return boneScale; }

	public void updateTree() {
		// Make sure to mirror from Fabric!
		Location boneLocation = holder.getAttachment().getAttachmentLocation();

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
		// for (AnimationController controller : holder.getAnimationControllers())
		// controller.animate(this);

		Quaternionf modelRot = new Quaternionf()
			.rotateZYX(holder.modelRotation.x, holder.modelRotation.y, holder.modelRotation.z);
		Vector3f computedTranslate = new Vector3f(boneOrigin)
			.add(boneTranslation)
			.rotate(modelRot)
			.add(holder.modelTranslation);

		outTransformation.getTranslation().set(computedTranslate);
		outTransformation.getLeftRotation().identity()
			.rotateZYX(holder.modelRotation.x, holder.modelRotation.y, holder.modelRotation.z);
		outTransformation.getRightRotation().identity()
			.rotateZYX(boneRotation.x, boneRotation.y, boneRotation.z);
		outTransformation.getScale().set(boneScale);

		display.teleport(boneLocation);
		display.setTransformation(outTransformation);
		for (BonePart child : children) child.updateTree();
	}
}
