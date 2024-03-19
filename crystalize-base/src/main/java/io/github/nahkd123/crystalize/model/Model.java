package io.github.nahkd123.crystalize.model;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import io.github.nahkd123.crystalize.anim.Animation;
import io.github.nahkd123.crystalize.texture.Texture;

public record Model(ElementGroup root, List<Texture> textures, List<Animation> animations) {

	public static final UUID ROOT_UUID = UUID.nameUUIDFromBytes("crystalize:root".getBytes(StandardCharsets.UTF_8));

	public Model {
		textures = Collections.unmodifiableList(textures);
		animations = Collections.unmodifiableList(animations);
	}
}
