package io.github.nahkd123.crystalize.fabric.sample.entity;

import static io.github.nahkd123.crystalize.fabric.model.TranslateStrategy.TRANSLATION_ONLY;
import static io.github.nahkd123.crystalize.fabric.sample.CrystalizeSampleMod.MODID;

import java.util.List;

import org.joml.Math;

import eu.pb4.polymer.core.api.entity.PolymerEntity;
import eu.pb4.polymer.virtualentity.api.attachment.EntityAttachment;
import eu.pb4.polymer.virtualentity.mixin.accessors.InteractionEntityAccessor;
import io.github.nahkd123.crystalize.anim.Animation;
import io.github.nahkd123.crystalize.anim.controller.TemplatedAnimationController;
import io.github.nahkd123.crystalize.fabric.model.CrystalizeElementHolder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.DataTracker.SerializedEntry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.FrogEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class TinyPotatogEntity extends FrogEntity implements PolymerEntity {
	private static final Identifier MODEL_ID = new Identifier(MODID, "tiny_potatog");
	public static final EntityType<TinyPotatogEntity> TYPE = FabricEntityTypeBuilder
		.<TinyPotatogEntity>create(SpawnGroup.MISC, TinyPotatogEntity::new)
		.trackRangeBlocks(10)
		.build();

	private CrystalizeElementHolder holder;

	// Animations
	private Animation walkingAnimation;
	private Animation jumpAnimation;

	private TemplatedAnimationController walkingController = null;
	private TemplatedAnimationController jumpingController = null;

	public TinyPotatogEntity(EntityType<? extends AnimalEntity> entityType, World world) {
		// See RoboticArmEntity for more info
		super(entityType, world);
		EntityAttachment.ofTicking(holder = new CrystalizeElementHolder(MODEL_ID, TRANSLATION_ONLY), this);
		holder.modelTranslation.y = -0.98f;
		walkingAnimation = holder.getTemplate().getAnimation("animation.walking").get();
		jumpAnimation = holder.getTemplate().getAnimation("animation.jump").get();
	}

	@Override
	public EntityType<?> getPolymerEntityType(ServerPlayerEntity player) {
		return EntityType.INTERACTION;
	}

	@Override
	public Vec3d getClientSidePosition(Vec3d vec3d) {
		return vec3d.add(0d, -0.98d, 0d);
	}

	@Override
	public void modifyRawTrackedData(List<SerializedEntry<?>> data, ServerPlayerEntity player, boolean initial) {
		data.add(DataTracker.SerializedEntry.of(InteractionEntityAccessor.getWIDTH(), 0.5f));
		data.add(DataTracker.SerializedEntry.of(InteractionEntityAccessor.getHEIGHT(), 0.5f));
	}

	@Override
	public void tick() {
		super.tick();
		holder.modelRotation.y = -Math.toRadians(getHeadYaw());

		if (limbAnimator.isLimbMoving()) {
			if (walkingController == null)
				holder.addAnimation(walkingController = new TemplatedAnimationController(walkingAnimation, 1f, null));
			walkingController.setTimeScale(limbAnimator.getSpeed());
		} else if (!limbAnimator.isLimbMoving() && walkingController != null) {
			holder.removeAnimation(walkingController);
			walkingController = null;
		}

		if (longJumpingAnimationState.isRunning() && jumpingController == null) {
			holder.addAnimation(jumpingController = new TemplatedAnimationController(jumpAnimation, 1f, null));
		} else if (!longJumpingAnimationState.isRunning() && jumpingController != null) {
			holder.removeAnimation(jumpingController);
			jumpingController = null;
		}
	}
}
