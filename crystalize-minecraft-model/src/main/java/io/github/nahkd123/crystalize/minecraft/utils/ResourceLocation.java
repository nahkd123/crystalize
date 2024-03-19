package io.github.nahkd123.crystalize.minecraft.utils;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

public record ResourceLocation(String namespace, String path) {
	public ResourceLocation {
		if (!isValidNamespace(namespace))
			throw new IllegalArgumentException("Namespace does not match [a-z0-9_.-] pattern");
		if (!isValidPath(path))
			throw new IllegalArgumentException("Path does not match [a-z0-9_.-/] pattern");
	}

	public ResourceLocation(String path) {
		// @formatter:off
		this(
			path.contains(":") ? path.split(":", 2)[0] : "minecraft",
			path.contains(":") ? path.split(":", 2)[1] : path);
		// @formatter:on
	}

	private static boolean isValidNamespace(String ns) {
		for (int i = 0; i < ns.length(); i++) {
			char ch = ns.charAt(i);
			if ((ch >= '0' && ch <= '9') ||
				(ch >= 'a' && ch <= 'z') ||
				ch == '_' || ch == '-' || ch == '.') continue;
			return false;
		}

		return true;
	}

	private static boolean isValidPath(String p) {
		for (int i = 0; i < p.length(); i++) {
			char ch = p.charAt(i);
			if ((ch >= '0' && ch <= '9') ||
				(ch >= 'a' && ch <= 'z') ||
				ch == '_' || ch == '-' || ch == '.' || ch == '/') continue;
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		return namespace + ":" + path;
	}

	public static final Codec<ResourceLocation> CODEC = Codec.STRING.comapFlatMap(
		s -> {
			try {
				return DataResult.success(new ResourceLocation(s));
			} catch (IllegalArgumentException e) {
				return DataResult.error(e::getMessage);
			}
		},
		rl -> rl.toString());
}
