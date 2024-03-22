package io.github.nahkd123.crystalize.bukkit.utils;

import java.io.IOException;
import java.io.OutputStream;

/**
 * <p>
 * Write the same data to multiple output streams at once.
 * </p>
 */
public class MultiOutputStream extends OutputStream {
	private Iterable<OutputStream> streams;

	public MultiOutputStream(Iterable<OutputStream> streams) {
		this.streams = streams;
	}

	@Override
	public void write(int b) throws IOException {
		for (OutputStream s : streams) s.write(b);
	}

	@Override
	public void write(byte[] b) throws IOException {
		for (OutputStream s : streams) s.write(b);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		for (OutputStream s : streams) s.write(b, off, len);
	}

	@Override
	public void flush() throws IOException {
		for (OutputStream s : streams) s.flush();
	}

	@Override
	public void close() throws IOException {
		for (OutputStream s : streams) s.close();
	}
}
