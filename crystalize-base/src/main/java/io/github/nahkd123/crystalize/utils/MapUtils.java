package io.github.nahkd123.crystalize.utils;

import java.util.Map;

public class MapUtils {
	/**
	 * <p>
	 * Reverse the key and value from input map.
	 * </p>
	 * 
	 * @param <K>     The output key.
	 * @param <V>     The output value.
	 * @param forward The forward map. <b>The map should be immutable!</b>
	 * @return The reverse map, which is also immutable.
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> reverse(Map<V, K> forward) {
		Map.Entry<K, V>[] entries = new Map.Entry[forward.size()];
		int i = 0;
		for (Map.Entry<V, K> e : forward.entrySet()) entries[i++] = Map.entry(e.getValue(), e.getKey());
		return Map.ofEntries(entries);
	}
}
