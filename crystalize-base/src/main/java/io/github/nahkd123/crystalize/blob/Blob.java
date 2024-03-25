package io.github.nahkd123.crystalize.blob;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

/**
 * <p>
 * An interface for all <b>b</b>inary <b>l</b>arge <b>ob</b>ject (blob)
 * implementations. Mainly used for storing texture data.
 * </p>
 * 
 * @see BytesBlob
 */
public interface Blob {
	/**
	 * <p>
	 * Open the stream to read this blob.
	 * </p>
	 * 
	 * @return The input stream.
	 */
	public InputStream open();

	/**
	 * <p>
	 * Convert this blob into byte array. This may fill your memory for very large
	 * objects. If the binary data can be streamed, consider using {@link #open()}.
	 * </p>
	 * 
	 * @return The byte array.
	 */
	default byte[] getBytes() {
		try (InputStream stream = open()) {
			return stream.readAllBytes();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}
