package io.github.nahkd123.crystalize.bukkit.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.logging.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;

import io.github.nahkd123.crystalize.blockbench.BlockbenchProject;
import io.github.nahkd123.crystalize.blockbench.build.BlockbenchModelBuilder;
import io.github.nahkd123.crystalize.model.Model;

public abstract class SimpleModelResourceProvider implements ModelResourceProvider {
	private Logger logger;
	private ModelResourceManager manager;
	private String name;

	public SimpleModelResourceProvider(Logger logger, String name) {
		this.logger = logger;
		this.name = name;
	}

	@Override
	public void init(ModelResourceManager manager) {
		this.manager = manager;
	}

	@Override
	public String getProviderName() { return name; }

	public ModelResourceManager getManager() { return manager; }

	protected <T> Optional<Model> readJsonModel(InputStream stream, Codec<T> codec, Function<T, Model> builder, String file) throws IOException {
		JsonElement json = JsonParser.parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
		var source = codec.decode(JsonOps.INSTANCE, json);
		if (source.error().isPresent())
			logger.warning("Failed to read " + file + ": " + source.error().get().message());
		return source.result().map(Pair::getFirst).map(builder);
	}

	protected Optional<Model> readBlockbenchModel(InputStream stream, String file) throws IOException {
		return readJsonModel(stream, BlockbenchProject.CODEC, proj -> {
			UUID rootId = UUID.nameUUIDFromBytes("root".getBytes(StandardCharsets.UTF_8));
			return BlockbenchModelBuilder.build(rootId, proj);
		}, file);
	}
}
