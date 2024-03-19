package io.github.nahkd123.crystalize.minecraft.atlas;

import org.junit.jupiter.api.Test;

import com.mojang.serialization.JsonOps;

class SpriteSourceTest {
	@Test
	void testCodec() {
		var result = SpriteSource.CODEC.encodeStart(JsonOps.INSTANCE, new SpriteDirectorySource("a", "a/"));
		System.out.println(result);
	}
}
