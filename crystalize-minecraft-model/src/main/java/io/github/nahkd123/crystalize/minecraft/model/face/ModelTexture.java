package io.github.nahkd123.crystalize.minecraft.model.face;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

public record ModelTexture(String target) {
	public static final Codec<ModelTexture> CODEC = Codec.STRING.comapFlatMap(
		s -> s.startsWith("#")
			? DataResult.success(new ModelTexture(s.substring(1)))
			: DataResult.error(() -> "Texture target must be prefixed with '#'"),
		t -> "#" + t.target());
}
