package io.github.nahkd123.crystalize.bukkit.model;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.ItemDisplay;
import org.joml.Vector3f;

import io.github.nahkd123.crystalize.anim.controller.AnimatableBone;
import io.github.nahkd123.crystalize.model.ElementGroup;

public class BonePart implements AnimatableBone {
	private BukkitModel holder;
	private ItemDisplay display;
	private ElementGroup template;
	private List<BonePart> children = new ArrayList<>();

	public final Vector3f boneOrigin = new Vector3f();
	public final Vector3f boneTranslation = new Vector3f();
	public final Vector3f boneRotation = new Vector3f();
	public final Vector3f boneScale = new Vector3f();

	public BonePart(BukkitModel holder, ItemDisplay display, ElementGroup template) {
		this.holder = holder;
		this.display = display;
		this.template = template;
	}

	public BukkitModel getHolder() { return holder; }

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
		for (BonePart child : children) child.updateTree();
	}
}
