package io.github.nahkd123.crystalize.blob;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class BytesBlob implements Blob {
	private byte[] bytes;

	public BytesBlob(byte[] bytes) {
		this.bytes = bytes;
	}

	@Override
	public InputStream open() {
		return new ByteArrayInputStream(bytes);
	}

	@Override
	public byte[] getBytes() { return bytes; }

	@Override
	public String toString() {
		return "BytesBlob[size=" + bytes.length + "]";
	}
}
