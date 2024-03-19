package io.github.nahkd123.crystalize.blockbench;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;

class BlockbenchProjectTest {
	BlockbenchProject open(String res) {
		try (InputStream stream = getClass().getClassLoader().getResourceAsStream(res)) {
			JsonReader reader = new JsonReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
			JsonElement elem = JsonParser.parseReader(reader);
			return BlockbenchProject.CODEC.decode(JsonOps.INSTANCE, elem)
				.resultOrPartial(e -> { throw new RuntimeException(e); })
				.map(Pair::getFirst)
				.get();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	@Test
	void testParse() {
		System.out.println(open("bones_chain.bbmodel"));
	}
}
