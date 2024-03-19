package io.github.nahkd123.crystalize.blockbench.build;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;

import io.github.nahkd123.crystalize.blockbench.BlockbenchProject;
import io.github.nahkd123.crystalize.blockbench.utils.HierarchicalRecords;
import io.github.nahkd123.crystalize.model.Model;

class BlockbenchModelBuilderTest {
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
	void testBuild() {
		BlockbenchProject project = open("bones_chain.bbmodel");
		Model model = BlockbenchModelBuilder.build(UUID.nameUUIDFromBytes(new byte[0]), project);
		HierarchicalRecords.print(model, "", "", System.out::println);
	}
}
