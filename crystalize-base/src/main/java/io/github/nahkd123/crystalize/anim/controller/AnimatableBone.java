package io.github.nahkd123.crystalize.anim.controller;

import java.util.Collection;

import org.joml.Vector3f;

import io.github.nahkd123.crystalize.anim.Animation;
import io.github.nahkd123.crystalize.model.ElementGroup;

/**
 * <p>
 * An interface for bones so that animation controllers can animate the bone.
 * Modifications can be applied directly to {@link #getTranslation()},
 * {@link #getRotation()} and {@link #getScale()} mutable vectors.
 * </p>
 */
public interface AnimatableBone {
	/**
	 * <p>
	 * Get the ID of animator that should control this bone. Usually used for
	 * picking animator from {@link Animation} template. The value is usually the ID
	 * of {@link ElementGroup}.
	 * </p>
	 * 
	 * @return The animator ID.
	 */
	public String getAnimatorId();

	/**
	 * <p>
	 * Get children from this bone. Can be used inside IK controller to perform
	 * inverse kinematics magic.
	 * </p>
	 * 
	 * @return Children of this bone.
	 */
	public Collection<? extends AnimatableBone> getChildren();

	/**
	 * <p>
	 * The translation (as known as offset or relative position to parent) of the
	 * bone. Set to zero (using {@link Vector3f#zero()}) to reset the bone to
	 * initial position.
	 * </p>
	 * 
	 * @return The bone translation.
	 */
	public Vector3f getTranslation();

	/**
	 * <p>
	 * The relative rotation of this bone. The bone rotates around its origin plus
	 * bone translations from all animation controllers and itself.
	 * </p>
	 * <p>
	 * The angles are in radians, and the rotation is Euler angles in {@code ZYX}
	 * rotation ordering.
	 * </p>
	 * 
	 * @return The bone rotation.
	 */
	public Vector3f getRotation();

	/**
	 * <p>
	 * The scale of this bone. Inside the animation controller, you should only
	 * multiply this with value of your choice instead of adding like
	 * {@link #getTranslation()} or {@link #getRotation()}.
	 * </p>
	 * 
	 * @return The bone scale.
	 */
	public Vector3f getScale();

	/**
	 * <p>
	 * Lookup a bone in the tree. This will traverse through all children.
	 * </p>
	 * 
	 * @param id The ID of the bone.
	 * @return The child animatable bone, or {@code null} if such child doesn't
	 *         exists.
	 */
	default AnimatableBone lookup(String id) {
		if (id.equals(getAnimatorId())) return this;

		for (AnimatableBone child : getChildren()) {
			if (child.getAnimatorId().equals(id)) return child;
			AnimatableBone childLookup = child.lookup(id);
			if (childLookup != null) return childLookup;
		}

		return null;
	}
}
