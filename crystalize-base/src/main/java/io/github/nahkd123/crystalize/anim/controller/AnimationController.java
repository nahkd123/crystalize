package io.github.nahkd123.crystalize.anim.controller;

/**
 * <p>
 * Animation controllers are used for controlling the bones in the model over
 * time, usually by interpolating between 2 keyframes at given time. However,
 * not all animation controller implementations have to do this.
 * </p>
 * <p>
 * Some controllers, like inverse kinematics, allow you to specify a location in
 * 3D space, then the model parts will slowly rotate itself towards the
 * destination. Pretty useful for dynamically animating Chinese dragon.
 * </p>
 * <p>
 * Crystalize provides {@link TemplatedAnimationController} out of the box,
 * which is a controller that animates the bones based on predefined animation
 * (either converted from Blockbench, or manually constructed from your code).
 * </p>
 * 
 * @see TemplatedAnimationController
 */
public interface AnimationController {
	/**
	 * <p>
	 * Update the time counter in this animation controller. It should only be
	 * called by model implementation, but that doesn't stop you from manually
	 * calling this to seek forward (or backward) with certain amount of time. This
	 * will be called by model implementation <i>once</i> every animation tick
	 * (usually matches with server tick).
	 * </p>
	 * 
	 * @param deltaTime The time offset to advance this animation controller.
	 *                  Positive values will advance forward; negative values will
	 *                  advance backward, but it is not guaranteed to works with all
	 *                  controllers.
	 * @param root      The root bone, which consists of other bones in
	 *                  {@link AnimatableBone#getChildren()}. Mainly used by inverse
	 *                  kinematics controllers to compute the desired bone position.
	 * @return Animation controller result. See {@link AnimateResult} for more
	 *         information. Please note that {@link #animate(AnimatableBone)} will
	 *         still be called when this method returns
	 *         {@link AnimateResult#REMOVE_CONTROLLER}.
	 * @implNote The implementation should <b>never</b> modify the bones in this
	 *           method. If needed, you can store the bone in the controller's field
	 *           and let {@link #animate(AnimatableBone)} handle it.
	 */
	public AnimateResult updateTimeRelative(float deltaTime, AnimatableBone root);

	/**
	 * <p>
	 * Apply the animation. This will be called by model implementation for all
	 * individual bones.
	 * </p>
	 * 
	 * @param part The bone, which you can use to apply animation.
	 */
	public void animate(AnimatableBone part);
}
