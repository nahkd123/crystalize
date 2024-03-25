package io.github.nahkd123.crystalize.bukkit.model;

import org.bukkit.World;

import io.github.nahkd123.crystalize.model.ElementGroup;

public class BukkitModel {
	private RegisteredModel base;
	private World world;

	public BukkitModel(RegisteredModel base) {
		this.base = base;
	}

	public RegisteredModel getBase() { return base; }

	private BonePart createBone(ElementGroup template, BonePart parent) {
		// TODO
		return null;
	}
}
