package io.github.nahkd123.crystalize.anim.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Vector3f;

/**
 * <p>
 * An implementation of FABRIK algorithm. Only support unconstrained joints at
 * this moment (though with hope and dream, the joint constraints shall be
 * implemented).
 * </p>
 * 
 * @deprecated This controller is currently WIP. The {@link Deprecated}
 *             annotation will be removed once I've finished implementing IK.
 * @see #getChainIds()
 * @see #getTolerance()
 * @see #getIterations()
 * @see #getTarget()
 * @see <a href=
 *      "http://www.andreasaristidou.com/publications/papers/FABRIK.pdf">Paper:
 *      "FABRIK: A fast, iterative solver for the Inverse Kinematics problem"</a>
 */
@Deprecated
public class FabrikController implements AnimationController {
	private List<String> chainIds;
	private float tolerance;
	private int iterations;
	private Vector3f target;
	private float time = 0f;

	private Map<AnimatableBone, Vector3f> nextOrigins = new HashMap<>();

	/**
	 * <p>
	 * Create a new FABRIK bones controller.
	 * </p>
	 * 
	 * @param chainIds   An ordered list/sequence of IDs, with the first ID
	 *                   corresponding to the head of the chain, and the last ID for
	 *                   the tail of the chain. The list must have at least 2
	 *                   elements.
	 * @param tolerance  The IK tolerance. Smaller value implies better quality, but
	 *                   will consumes more CPU usage. <b>Setting this to 0 may
	 *                   cause the controller to stuck in infinite loop!</b> You
	 *                   might also want to change the number to iterations to be
	 *                   higher if the tolerance value is small enough.
	 * @param iterations The number of IK iterations. Higher value implies better
	 *                   quality at a cost of longer processing time.
	 * @param target     The target position, which all the bones in the chain will
	 *                   points towards. The vector must be <i>mutable</i>.
	 * @see #FabrikController(List, Vector3f)
	 */
	public FabrikController(List<String> chainIds, float tolerance, int iterations, Vector3f target) {
		this.chainIds = chainIds;
		this.tolerance = tolerance;
		this.iterations = iterations;
		this.target = target;
	}

	/**
	 * <p>
	 * Create a new controller with default tolerance and iterations (1E-6
	 * tolerance, 3*Chain iterations).
	 * </p>
	 * 
	 * @param chainIds Chain IDs.
	 * @param target   The mutable target position.
	 * @see #FabrikController(List, float, int, Vector3f)
	 */
	public FabrikController(List<String> chainIds, Vector3f target) {
		this(chainIds, 1E-6f, chainIds.size() * 3, target);
	}

	/**
	 * <p>
	 * Get the IDs of the bone chain. If the chain is not continuously connected, or
	 * the list length is less than 2, this IK controller will do nothing.
	 * </p>
	 * <p>
	 * The head bone will always be the first element of the chain, while the tail
	 * bone (the bone that will be placed at target) will always be the last element
	 * of the chain.
	 * 
	 * @return A list of chain IDs.
	 */
	public List<String> getChainIds() { return Collections.unmodifiableList(chainIds); }

	/**
	 * <p>
	 * Get the IK tolerance. Smaller value improves the quality, but may cause
	 * performance issue. The value should never be {@code 0.0}, otherwise it may
	 * block the current running thread.
	 * </p>
	 * 
	 * @return The IK tolerance.
	 * @see #setTolerance(float)
	 */
	public float getTolerance() { return tolerance; }

	public void setTolerance(float tolerance) { this.tolerance = tolerance; }

	/**
	 * <p>
	 * Get the number of iterations. The higher, the better, but also more taxing on
	 * CPU usage. Please note that the number of iterations might not always be the
	 * value this method returns, as it is also being limited by
	 * {@link #getTolerance()}.
	 * </p>
	 * 
	 * @return The IK iterations.
	 */
	public int getIterations() { return iterations; }

	public void setIterations(int iterations) { this.iterations = iterations; }

	/**
	 * <p>
	 * Get the target position, where the end bone's origin will be placed.
	 * </p>
	 * 
	 * @return The target position.
	 */
	public Vector3f getTarget() { return target; }

	@Override
	public AnimateResult updateTimeRelative(float deltaTime, AnimatableBone root) {
		time += deltaTime;
		target.x = (float) Math.sin(time * 1f);
		target.y = (float) Math.cos(time * 2f);
		target.z = (float) Math.cos(time * 3f);
		applyIk(root);
		return AnimateResult.CONTINUE;
	}

	public void applyIk(AnimatableBone root) {
		if (chainIds.size() < 2) return;

		List<AnimatableBone> branch = new ArrayList<>();
		branch.add(root.lookup(chainIds.get(0)));
		if (branch.get(0) == null) return;

		while (branch.size() < chainIds.size()) {
			AnimatableBone last = branch.get(branch.size() - 1);
			branch.add(last.lookupChild(chainIds.get(branch.size())));
			if (branch.get(branch.size() - 1) == null) return;
		}

		Vector3f[] origins = new Vector3f[branch.size()];
		float[] distances = new float[branch.size() - 1];

		for (int i = 0; i < origins.length; i++) {
			// We'll assume there always be a distance of 0.5 between all bones
			// Note that this is for testing only; the actual implementation will have to
			// fetch the distance from model template.
			if (i >= 1) distances[i - 1] = 0.5f;

			// Here we get the previous origin of the bone (which was applied from previous
			// animation tick). This allows the bones to move around in more natural way.
			origins[i] = nextOrigins.compute(branch.get(i), (k, vec) -> vec == null ? new Vector3f() : vec);
			origins[i].set(branch.get(i).getOrigin());
		}

		// If the target is unreachable (out of reachable range), we'll straighten the
		// bones towards the target.
		// This can be done by taking a sum of all distances and compare it with the
		// distance from root's origin to target.
		float distSum = 0f;
		for (int i = 0; i < distances.length; i++) distSum += distances[i];

		if (origins[0].distance(target) > distSum) {
			Vector3f direction = target.sub(origins[0], new Vector3f()).normalize();

			for (int i = 1; i < origins.length; i++) {
				float distance = distances[i - 1];
				Vector3f newOrigin = direction.mul(distance, new Vector3f()).add(origins[i - 1]);
				origins[i].set(newOrigin);
			}
		} else {
			// It is reachable, thus we will do forward and backward reaching here
			int maxIteration = iterations;
			Vector3f oldRoot = new Vector3f(origins[0]);
			float dif = origins[origins.length - 1].distance(target);

			while (dif > tolerance && maxIteration > 0) {
				maxIteration--;

				// Forward reaching
				origins[origins.length - 1].set(target);

				for (int i = origins.length - 2; i >= 0; i--) {
					float distance = origins[i].distance(origins[i + 1]);
					float scale = distances[i] / distance;
					origins[i].x = (1 - scale) * origins[i + 1].x + scale * origins[i].x;
					origins[i].y = (1 - scale) * origins[i + 1].y + scale * origins[i].y;
					origins[i].z = (1 - scale) * origins[i + 1].z + scale * origins[i].z;
				}

				// Backward reaching
				origins[0].set(oldRoot);

				for (int i = 0; i < origins.length - 2; i++) {
					float distance = origins[i].distance(origins[i + 1]);
					float scale = distances[i] / distance;
					origins[i + 1].x = (1 - scale) * origins[i].x + scale * origins[i + 1].x;
					origins[i + 1].y = (1 - scale) * origins[i].y + scale * origins[i + 1].y;
					origins[i + 1].z = (1 - scale) * origins[i].z + scale * origins[i + 1].z;
				}

				dif = origins[origins.length - 1].distance(target);
			}
		}
	}

	@Override
	public void animate(AnimatableBone part) {
		if (chainIds.size() < 2) return;
		Vector3f v = nextOrigins.get(part);
		if (v != null) part.getOrigin().set(v);
		// TODO rotation
	}
}