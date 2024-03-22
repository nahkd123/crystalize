package io.github.nahkd123.crystalize.bukkit.pack;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import io.github.nahkd123.crystalize.bukkit.utils.MultiOutputStream;

public class PackBuildContextImpl implements PackBuildContext {
	private List<Path> destinations = new ArrayList<>();
	private MultiOutputStream stream = null;

	public void addDestination(Path path) {
		destinations.add(path);
	}

	@Override
	public OutputStream createStream() {
		if (stream != null) throw new IllegalStateException("Can only create at most 1 stream per build");
		return stream = new MultiOutputStream(destinations.stream()
			.map(p -> {
				try {
					return Files.newOutputStream(p);
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			})
			.filter(s -> s != null)
			.toList());
	}
}
