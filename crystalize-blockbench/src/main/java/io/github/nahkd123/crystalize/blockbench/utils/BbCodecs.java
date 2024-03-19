package io.github.nahkd123.crystalize.blockbench.utils;

import java.util.Base64;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.github.nahkd123.crystalize.blob.Blob;
import io.github.nahkd123.crystalize.blob.BytesBlob;
import io.github.nahkd123.crystalize.texture.Texture;

public class BbCodecs {
	private static final String PNG_BASE64_PREFIX = "data:image/png;base64,";
	private static final Codec<byte[]> PNG_BASE64 = Codec.STRING.comapFlatMap(
		s -> {
			if (!s.startsWith(PNG_BASE64_PREFIX))
				return DataResult.error(() -> "The source must be prefixed with '" + PNG_BASE64_PREFIX + "'");

			try {
				return DataResult.success(Base64.getDecoder().decode(s.substring(PNG_BASE64_PREFIX.length())));
			} catch (Exception e) {
				return DataResult.error(e::getMessage);
			}
		},
		bs -> PNG_BASE64_PREFIX + Base64.getEncoder().encodeToString(bs));

	private static final Codec<Blob> PNG_BASE64_BLOB = PNG_BASE64.xmap(BytesBlob::new, Blob::getBytes);

	public static final Codec<Texture> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.STRING.fieldOf("uuid").forGetter(Texture::id),
		Codec.INT.fieldOf("width").forGetter(Texture::width),
		Codec.INT.fieldOf("height").forGetter(Texture::height),
		Codec.FLOAT.fieldOf("uv_width").forGetter(Texture::uvWidth),
		Codec.FLOAT.fieldOf("uv_height").forGetter(Texture::uvHeight),
		Codec.DOUBLE.fieldOf("frame_time").forGetter(Texture::frameTime),
		PNG_BASE64_BLOB.fieldOf("source").forGetter(Texture::data))
		.apply(instance, Texture::new));
}
