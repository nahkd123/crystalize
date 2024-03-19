package io.github.nahkd123.crystalize.model.face;

import io.github.nahkd123.crystalize.texture.Texture;
import io.github.nahkd123.crystalize.utils.UV;

public record CubeFace(UV uv, Texture texture, int rotation) {
}
