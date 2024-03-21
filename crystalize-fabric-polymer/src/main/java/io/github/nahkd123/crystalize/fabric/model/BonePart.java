package io.github.nahkd123.crystalize.fabric.model;

import java.util.ArrayList;
import java.util.List;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import io.github.nahkd123.crystalize.fabric.anim.AnimationController;
import io.github.nahkd123.crystalize.fabric.debug.DebugDisplayModels;
import io.github.nahkd123.crystalize.model.ElementGroup;
import net.minecraft.entity.decoration.Brightness;
import net.minecraft.util.math.Vec3d;

public class BonePart {
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

	public List<BonePart> getChildren() { return children; }

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

		// @formatter:off
		// display.setOffset(new Vec3d(boneOrigin));
		display.setOffset(new Vec3d(0, 0, 0));
		display.setTranslation(new Vector3f(boneOrigin)
			.add(boneTranslation)
			.rotate(new Quaternionf().rotateZYX(holder.modelRotation.x, holder.modelRotation.y, holder.modelRotation.z))
			.add(holder.modelTranslation));
		display.setRightRotation(new Quaternionf().rotateZYX(boneRotation.x, boneRotation.y, boneRotation.z));
		display.setLeftRotation(new Quaternionf().rotateZYX(holder.modelRotation.x, holder.modelRotation.y, holder.modelRotation.z));
		display.setScale(boneScale);
		display.setStartInterpolation(0);
		display.setInterpolationDuration(1);
		// @formatter:on

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
