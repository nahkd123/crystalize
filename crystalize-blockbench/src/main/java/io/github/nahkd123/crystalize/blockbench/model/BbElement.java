package io.github.nahkd123.crystalize.blockbench.model;

import java.util.Map;
import java.util.UUID;

import org.joml.Vector3fc;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

import io.github.nahkd123.crystalize.utils.MapUtils;

public interface BbElement {
	public MapCodec<? extends BbElement> getCodec();

	public Vector3fc origin();

	public Vector3fc rotation();

	public UUID uuid();

	public static final Map<String, MapCodec<? extends BbElement>> ID_TO_FACTORY = Map.of(
		"cube", BbCubeElement.CODEC,
		"locator", BbLocatorElement.CODEC);

	public static final Map<MapCodec<? extends BbElement>, String> FACTORY_TO_ID = MapUtils.reverse(ID_TO_FACTORY);

	public static <T extends BbElement> void register(String type, MapCodec<T> codec) {
		if (ID_TO_FACTORY.containsKey(type))
			throw new IllegalStateException("Element type '" + type + "' is already registered");
		ID_TO_FACTORY.put(type, codec);
		FACTORY_TO_ID.put(codec, type);
	}

	public static final Codec<BbElement> CODEC = Codec.STRING.dispatch(
		"type",
		e -> FACTORY_TO_ID.get(e.getCodec()),
		ID_TO_FACTORY::get);
}
