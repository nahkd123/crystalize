package io.github.nahkd123.crystalize.fabric.sample.entity;

import static io.github.nahkd123.crystalize.fabric.model.TranslateStrategy.TRANSLATION_ONLY;

import java.util.Arrays;

import org.joml.Vector3f;

import eu.pb4.polymer.core.api.entity.PolymerEntity;
import eu.pb4.polymer.virtualentity.api.attachment.EntityAttachment;
import io.github.nahkd123.crystalize.anim.controller.FabrikController;
import io.github.nahkd123.crystalize.fabric.model.CrystalizeElementHolder;
import io.github.nahkd123.crystalize.fabric.sample.CrystalizeSampleMod;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RoboticArmEntity extends Entity implements PolymerEntity {
	private static final Identifier MODEL_ID = new Identifier(CrystalizeSampleMod.MODID, "robotic_arm");
	public static final EntityType<RoboticArmEntity> TYPE = FabricEntityTypeBuilder
		.<RoboticArmEntity>create(SpawnGroup.MISC, RoboticArmEntity::new)
		.build();

	private CrystalizeElementHolder modelHolder;
	private Vector3f target = new Vector3f();

	public RoboticArmEntity(EntityType<? extends RoboticArmEntity> type, World world) {
		super(type, world);

		// Custom server-side entities with Polymer is actually pretty easy. All you
		// need to do is implementing PolymerEntity interface, then register it with
		// PolymerEntityUtils.registerType().
		// Adding our server-model is easy as well! Create CrystalizeElementHolder then
		// attach it to your entity and you're done.
		EntityAttachment.ofTicking(modelHolder = new CrystalizeElementHolder(MODEL_ID, TRANSLATION_ONLY), this);

		// Let's make our robotic arm tracking nearby players!
		// Target position can be modified through our mutable vector object.
		// These IDs are obtained from Blockbench file, in "outliner" object. Each
		// outliner object is a ElementGroup (a.k.a bone).
		modelHolder.addAnimation(new FabrikController(Arrays.asList(
			"984422d6-82df-2fc5-9509-809912aa01fd",
			"fd6d3d3e-aa57-518e-b758-3e56901278ea",
			"e99bebd5-5564-9731-9819-3bb487096f1c"), target));

		// Check out player tracking code in #tick()! It's actually pretty simple.
	}

	public RoboticArmEntity(World world, Vec3d pos) {
		this(TYPE, world);
		setPosition(pos);
	}

	@Override
	public EntityType<?> getPolymerEntityType(ServerPlayerEntity player) {
		return EntityType.INTERACTION;
	}

	@Override
	protected void initDataTracker() {}

	@Override
	protected void readCustomDataFromNbt(NbtCompound nbt) {}

	@Override
	protected void writeCustomDataToNbt(NbtCompound nbt) {}

	@Override
	public void tick() {
		super.tick();

		PlayerEntity closest = getWorld().getClosestPlayer(getX(), getY(), getZ(), 3d, false);
		if (closest == null) return;

		// "target" is relative to the model's origin, so we need to subtract player's
		// position by entity's position.
		target.set(closest.getPos().toVector3f()).sub(getPos().toVector3f());
	}
}
