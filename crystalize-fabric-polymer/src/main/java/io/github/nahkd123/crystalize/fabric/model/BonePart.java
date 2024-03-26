package io.github.nahkd123.crystalize.fabric.model;

import static io.github.nahkd123.crystalize.fabric.model.TranslateStrategy.POSITION_ONLY;

import java.util.ArrayList;
import java.util.List;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import io.github.nahkd123.crystalize.anim.controller.AnimatableBone;
import io.github.nahkd123.crystalize.anim.controller.AnimationController;
import io.github.nahkd123.crystalize.fabric.utils.ModifiableHandle;
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

	// Reduce send packet calls
	private ModifiableHandle<Vector3f> displayTranslation = new ModifiableHandle<>(Vector3f::new, Vector3f::set);
	private ModifiableHandle<Quaternionf> displayModelRotation = new ModifiableHandle<>(Quaternionf::new, Quaternionf::set);
	private ModifiableHandle<Vector3f> displayPartRotation = new ModifiableHandle<>(Vector3f::new, Vector3f::set);
	private ModifiableHandle<Vector3f> displayScale = new ModifiableHandle<>(() -> new Vector3f(1, 1, 1), Vector3f::set);
	private Quaternionf displayPartQuaternion = new Quaternionf();
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
	 * @deprecated use {@link #computeTree()} and {@link #tickTree()}
	 */
	@Deprecated
	public void updateTree() {
		computeTree();
		tickTree();
	}

	/**
	 * <p>
	 * Compute the display entity's transformations before applying to the entity.
	 * This also compute the animations.
	 * </p>
	 * <p>
	 * Originally intended to send this for off-thread computation, but it causes
	 * the parts to separate, so I decided to let it run on main thread instead for
	 * the time being.
	 * </p>
	 */
	public void computeTree() {
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

		displayTranslation.getCurrent().set(computedTranslate);
		displayModelRotation.getCurrent().set(modelRot);
		displayPartRotation.getCurrent().set(boneRotation);
		displayPartQuaternion.identity().rotateZYX(boneRotation.x, boneRotation.y, boneRotation.z);
		displayScale.getCurrent().set(boneScale);
		for (BonePart child : children) child.computeTree();
	}

	/**
	 * <p>
	 * Send transformations of this part to watching players.
	 * </p>
	 */
	public void tickTree() {
		boolean interpolate = displayTranslation.isModified()
			|| displayModelRotation.isModified()
			|| displayPartRotation.isModified()
			|| displayScale.isModified();

		displayTranslation.onModified(display::setTranslation);
		displayModelRotation.onModified(display::setLeftRotation);
		displayPartRotation.onModified($ -> display.setRightRotation(displayPartQuaternion));
		displayScale.onModified(display::setScale);

		if (holder.getTranslateStrategy() != POSITION_ONLY && interpolate) {
			display.setStartInterpolation(0);
			display.setInterpolationDuration(1);
		}
		if (holder.getTranslateStrategy() == POSITION_ONLY) display.setInterpolationDuration(0);

		for (BonePart child : children) child.tickTree();
	}
}
