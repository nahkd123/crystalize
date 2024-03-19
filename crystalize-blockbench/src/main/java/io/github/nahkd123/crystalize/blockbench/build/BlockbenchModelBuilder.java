package io.github.nahkd123.crystalize.blockbench.build;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;

import org.joml.Vector3f;
import org.joml.Vector3fc;

import io.github.nahkd123.crystalize.anim.Animation;
import io.github.nahkd123.crystalize.blockbench.BlockbenchProject;
import io.github.nahkd123.crystalize.blockbench.model.BbCubeElement;
import io.github.nahkd123.crystalize.blockbench.model.BbElement;
import io.github.nahkd123.crystalize.blockbench.model.face.FaceTexture;
import io.github.nahkd123.crystalize.blockbench.outliner.BbElementGroup;
import io.github.nahkd123.crystalize.blockbench.outliner.BbElementReference;
import io.github.nahkd123.crystalize.blockbench.outliner.Outliner;
import io.github.nahkd123.crystalize.model.Cube;
import io.github.nahkd123.crystalize.model.Element;
import io.github.nahkd123.crystalize.model.ElementGroup;
import io.github.nahkd123.crystalize.model.Model;
import io.github.nahkd123.crystalize.model.face.CubeFace;
import io.github.nahkd123.crystalize.model.face.Face;
import io.github.nahkd123.crystalize.texture.Texture;

public class BlockbenchModelBuilder {
	public static Model build(UUID rootUuid, BlockbenchProject project) {
		BbElementGroup root = new BbElementGroup(rootUuid, new Vector3f(), new Vector3f(), true, project.outliner());
		ElementGroup builtRoot = build(root, project, root);
		List<Animation> animations = project.animations().stream()
			.map(BlockbenchAnimationBuilder::buildAnimation)
			.toList();
		return new Model(builtRoot, project.textures(), animations);
	}

	public static ElementGroup build(BbElementGroup group, BlockbenchProject project, BbElementGroup parent) {
		Vector3fc parentOrigin = parent.origin();
		Vector3f selfOrigin = new Vector3f(group.origin()).sub(parentOrigin);
		List<Element> children = new ArrayList<>();

		for (Outliner child : group.children()) {
			if (child instanceof BbElementReference ref) {
				Optional<BbElement> any = project.elements().stream()
					.filter(e -> e.uuid().equals(ref.target()))
					.findAny();

				if (any.isEmpty()) continue; // ignore?
				BbElement elem = any.get();

				if (elem instanceof BbCubeElement cube) {
					Vector3f cubeOrigin = new Vector3f(cube.origin()).sub(group.origin());
					Vector3fc cubeRotation = cube.rotation();
					Vector3f cubeFrom = new Vector3f(cube.from()).sub(group.origin());
					Vector3f cubeTo = new Vector3f(cube.to()).sub(group.origin());
					Map<Face, CubeFace> faces = new EnumMap<>(Face.class);

					for (Entry<Face, FaceTexture> e : cube.faces().entrySet()) {
						Texture texture = project.textures().get(e.getValue().textureIndex());
						CubeFace face = new CubeFace(e.getValue().uv(), texture, 0); // TODO rotation
						faces.put(e.getKey(), face);
					}

					Cube builtCube = new Cube(cubeOrigin, cubeRotation, cubeFrom, cubeTo, faces);
					children.add(builtCube);
				}

				// TODO locator
				// TODO log
			}

			if (child instanceof BbElementGroup childGroup) {
				ElementGroup builtChild = build(childGroup, project, group);
				children.add(builtChild);
			}

			// TODO log
		}

		return new ElementGroup(group.uuid().toString(), selfOrigin, group.rotation(), children);
	}
}
