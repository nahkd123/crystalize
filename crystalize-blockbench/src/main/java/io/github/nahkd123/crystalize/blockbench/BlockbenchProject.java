package io.github.nahkd123.crystalize.blockbench;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.github.nahkd123.crystalize.blockbench.anim.BbAnimation;
import io.github.nahkd123.crystalize.blockbench.model.BbElement;
import io.github.nahkd123.crystalize.blockbench.outliner.Outliner;
import io.github.nahkd123.crystalize.blockbench.utils.BbCodecs;
import io.github.nahkd123.crystalize.texture.Texture;

public record BlockbenchProject(List<BbElement> elements, List<Outliner> outliner, List<Texture> textures, List<BbAnimation> animations) {

	public static final Codec<BlockbenchProject> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		BbElement.CODEC.listOf()
			.optionalFieldOf("elements", Collections.emptyList())
			.forGetter(BlockbenchProject::elements),
		Outliner.CODEC.listOf()
			.optionalFieldOf("outliner", Collections.emptyList())
			.forGetter(BlockbenchProject::outliner),
		BbCodecs.CODEC.listOf()
			.optionalFieldOf("textures", Collections.emptyList())
			.forGetter(BlockbenchProject::textures),
		BbAnimation.CODEC.listOf()
			.optionalFieldOf("animations", Collections.emptyList())
			.forGetter(BlockbenchProject::animations))
		.apply(instance, BlockbenchProject::new));

	@Override
	public String toString() {
		return "BlockbenchProject[\n"
			+ "  elements (" + elements.size() + ") = [\n"
			+ elements.stream()
				.flatMap(e -> Stream.of(e.toString().split("\n")))
				.map(s -> "    " + s)
				.collect(Collectors.joining("\n"))
			+ "\n"
			+ "  outliner (" + outliner.size() + ") = [\n"
			+ outliner.stream()
				.flatMap(o -> Stream.of(o.toString().split("\n")))
				.map(s -> "    " + s)
				.collect(Collectors.joining("\n"))
			+ "\n  ]\n"
			+ "  textures (" + textures.size() + ") = [\n"
			+ textures.stream()
				.map(s -> "    " + s.toString())
				.collect(Collectors.joining("\n"))
			+ "\n  ]\n"
			+ "  animations (" + animations.size() + ") = [\n"
			+ animations.stream()
				.flatMap(e -> Stream.of(e.toString().split("\n")))
				.map(s -> "    " + s)
				.collect(Collectors.joining("\n"))
			+ "\n  ]\n"
			+ "]";
	}
}
