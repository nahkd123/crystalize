package io.github.nahkd123.crystalize.fabric.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;

import eu.pb4.polymer.resourcepack.api.ResourcePackBuilder;
import io.github.nahkd123.crystalize.fabric.CrystalizeMod;

/**
 * <p>
 * Extension/Wrapper class for saving objects with codecs easier.
 * </p>
 */
public class ResourceWrapper {
	private BiConsumer<String, byte[]> saver;
	private Function<String, byte[]> loader;
	private Gson gson;

	public ResourceWrapper(BiConsumer<String, byte[]> saver, Function<String, byte[]> loader, Gson gson) {
		this.saver = saver;
		this.loader = loader;
		this.gson = gson;
	}

	public ResourceWrapper(ResourcePackBuilder builder, Gson gson) {
		this(builder::addData, builder::getData, gson);
	}

	public void saveRaw(String path, byte[] data) {
		saver.accept(path, data);
	}

	public byte[] loadRaw(String path) {
		return loader.apply(path);
	}

	public <T> void saveJson(String path, T object, Codec<T> codec) {
		DataResult<JsonElement> result = codec.encodeStart(JsonOps.INSTANCE, object);
		JsonElement encoded = result.resultOrPartial(CrystalizeMod.LOGGER::error).get();
		if (encoded != null) saver.accept(path, gson.toJson(encoded).getBytes(StandardCharsets.UTF_8));
	}

	public <T> T loadJson(String path, Codec<T> codec) {
		byte[] raw = loadRaw(path);
		if (raw == null) return null;
		JsonElement json = JsonParser.parseReader(new JsonReader(new InputStreamReader(new ByteArrayInputStream(raw))));
		DataResult<Pair<T, JsonElement>> result = codec.decode(JsonOps.INSTANCE, json);
		T decoded = result.resultOrPartial(CrystalizeMod.LOGGER::error).get().getFirst();
		return decoded;
	}

	public <T> T transformJson(String path, Codec<T> codec, UnaryOperator<T> ifPresent, Supplier<T> ifEmpty) {
		T data = loadJson(path, codec);
		data = data != null ? ifPresent.apply(data) : ifEmpty.get();
		saveJson(path, data, codec);
		return data;
	}
}
