package io.github.nahkd123.crystalize.bukkit.pack;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;

import io.github.nahkd123.crystalize.blob.Blob;
import io.github.nahkd123.crystalize.blob.BytesBlob;
import io.github.nahkd123.crystalize.bukkit.CrystalizePlugin;
import io.github.nahkd123.crystalize.bukkit.model.ItemModel;
import io.github.nahkd123.crystalize.minecraft.atlas.SpriteSingleSource;
import io.github.nahkd123.crystalize.minecraft.atlas.TextureAtlas;
import io.github.nahkd123.crystalize.minecraft.model.MinecraftModel;
import io.github.nahkd123.crystalize.minecraft.utils.ResourceLocation;
import io.github.nahkd123.crystalize.texture.Texture;

public class CrystalizeResourcePackBuilder implements ResourcePackBuilder {
	private Map<String, Blob> fileSystem = new HashMap<>();
	private CrystalizePlugin plugin;
	private Map<Material, Integer> modelsCounter = new HashMap<>();
	private Gson gson = new Gson();

	public CrystalizeResourcePackBuilder(CrystalizePlugin plugin) {
		this.plugin = plugin;
	}

	private <T> void writeJson(String path, T data, Codec<T> codec) {
		// TODO blob data that doesn't eat too much ram
		// delegate this to Crystalize Base
		DataResult<JsonElement> result = codec.encodeStart(JsonOps.INSTANCE, data);
		JsonElement json = result.resultOrPartial(plugin.getLogger()::severe).get();
		BytesBlob blob = new BytesBlob(gson.toJson(json).getBytes(StandardCharsets.UTF_8));
		fileSystem.put(path, blob);
	}

	private <T> T readJson(String path, Codec<T> codec) {
		Blob blob = fileSystem.get(path);
		if (blob == null) return null;
		JsonElement json;

		try {
			InputStreamReader reader = new InputStreamReader(blob.open(), StandardCharsets.UTF_8);
			json = JsonParser.parseReader(reader);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		DataResult<Pair<T, JsonElement>> result = codec.decode(JsonOps.INSTANCE, json);
		T data = result.resultOrPartial(plugin.getLogger()::severe).map(Pair::getFirst).get();
		return data;
	}

	private <T> void update(String path, Codec<T> codec, UnaryOperator<T> onPresent, Supplier<T> onAbsent) {
		T data = readJson(path, codec);
		data = data == null ? onAbsent.get() : onPresent.apply(data);
		writeJson(path, data, codec);
	}

	private ItemModel allocateNewModel(NamespacedKey modelPath) {
		// TODO cycle
		int value = modelsCounter.compute(Material.COMMAND_BLOCK, (mat, val) -> val == null ? 1 : (val + 1));
		// TODO add model to command_block.json
		return new ItemModel(Material.COMMAND_BLOCK, value);
	}

	@Override
	public ItemModel addModel(NamespacedKey modelPath, MinecraftModel model) {
		String path = "assets/" + modelPath.getNamespace() + "/models/" + modelPath.getKey() + ".json";
		writeJson(path, model, MinecraftModel.CODEC);
		return allocateNewModel(modelPath);
	}

	@Override
	public void addTexture(NamespacedKey texturePath, Texture texture) {
		String path = "assets/" + texturePath.getNamespace() + "/textures/" + texturePath.getKey() + ".png";
		fileSystem.put(path, texture.data());
		// TODO .mcmeta

		// Update atlas
		ResourceLocation resLoc = new ResourceLocation(texturePath.getNamespace(), texturePath.getKey());
		SpriteSingleSource source = new SpriteSingleSource(resLoc, null);
		update("assets/minecraft/atlases/blocks.json", TextureAtlas.CODEC,
			old -> new TextureAtlas(Stream.concat(old.sources().stream(), Stream.of(source)).toList()),
			() -> new TextureAtlas(Collections.singletonList(source)));
	}

	@Override
	public void build(PackBuildContext context) {
		try (OutputStream stream = context.createStream()) {
			ZipOutputStream zip = new ZipOutputStream(stream, StandardCharsets.UTF_8);

			for (Entry<String, Blob> entry : fileSystem.entrySet()) {
				String path = entry.getKey();
				Blob data = entry.getValue();
				zip.putNextEntry(new ZipEntry(path));

				try (InputStream dataStream = data.open()) {
					dataStream.transferTo(zip);
				}

				zip.closeEntry();
			}

			zip.flush();
			zip.finish();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
