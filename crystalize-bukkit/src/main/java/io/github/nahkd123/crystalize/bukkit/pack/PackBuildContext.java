package io.github.nahkd123.crystalize.bukkit.pack;

import java.io.OutputStream;

public interface PackBuildContext {
	/**
	 * <p>
	 * Create a new data stream that your resource pack builder can write to. This
	 * will write the pack to location configured in user's configuration. Don't
	 * forget to flush and close the stream.
	 * </p>
	 * <p>
	 * Please note that you must write valid zip data to this stream.
	 * </p>
	 * 
	 * @return The output data stream.
	 */
	public OutputStream createStream();
}
