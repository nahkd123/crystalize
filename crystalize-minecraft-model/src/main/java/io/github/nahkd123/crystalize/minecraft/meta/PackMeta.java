package io.github.nahkd123.crystalize.minecraft.meta;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record PackMeta(int packFormat, PackVersionRange supportedFormats) {
	public PackMeta(int packFormat) {
		this(packFormat, null);
	}

	public static final Codec<PackMeta> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.INT.fieldOf("pack_format").forGetter(PackMeta::packFormat),
		PackVersionRange.CODEC.optionalFieldOf("supported_formats", null).forGetter(PackMeta::supportedFormats))
		.apply(instance, PackMeta::new));
}
