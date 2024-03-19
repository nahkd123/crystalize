package io.github.nahkd123.crystalize.blob;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

public interface Blob {
	public InputStream open();

	default byte[] getBytes() {
		try (InputStream stream = open()) {
			return stream.readAllBytes();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}
