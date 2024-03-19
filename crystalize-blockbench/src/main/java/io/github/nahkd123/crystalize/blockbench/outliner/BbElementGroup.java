package io.github.nahkd123.crystalize.blockbench.outliner;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.joml.Vector3fc;

import io.github.nahkd123.crystalize.blockbench.anim.BbAnimator;
import io.github.nahkd123.crystalize.blockbench.build.BlockbenchModelBuilder;
import io.github.nahkd123.crystalize.model.ElementGroup;

public record BbElementGroup(UUID uuid, Vector3fc origin, Vector3fc rotation, boolean visibility, List<Outliner> children) implements Outliner {
	/**
	 * @param uuid       The ID of this group. Mainly for {@link BbAnimator} to
	 *                   animate this group.
	 * @param origin     The group origin in model space (a.k.a absolute position),
	 *                   unlike {@link ElementGroup#origin()}, which is relative to
	 *                   parent.
	 * @param rotation   The rotation of this group around its origin.
	 * @param visibility The visibility of this group.
	 *                   {@link BlockbenchModelBuilder#build(BbElementGroup, List)}
	 *                   will ignores all invisible children.
	 * @param children   Children elements of this group. Contains either
	 *                   {@link BbElementGroup} or {@link BbElementReference}.
	 */
	public BbElementGroup {
		children = Collections.unmodifiableList(children);
	}

	@Override
	public String toString() {
		return "BbElementGroup[\n"
			+ "  uuid = " + uuid + "\n"
			+ "  origin = " + origin + "\n"
			+ "  rotation = " + rotation + "\n"
			+ "  visibility = " + visibility + "\n"
			+ "  children = [\n"
			+ children.stream()
				.flatMap(child -> Stream.of(child.toString().split("\n")))
				.map(s -> "    " + s)
				.collect(Collectors.joining("\n"))
			+ "\n  ]\n"
			+ "]";
	}
}
