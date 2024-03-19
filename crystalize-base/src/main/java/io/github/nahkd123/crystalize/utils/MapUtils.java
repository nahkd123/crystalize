package io.github.nahkd123.crystalize.utils;

import java.util.Map;

public class MapUtils {
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> reverse(Map<V, K> forward) {
		Map.Entry<K, V>[] entries = new Map.Entry[forward.size()];
		int i = 0;
		for (Map.Entry<V, K> e : forward.entrySet()) entries[i++] = Map.entry(e.getValue(), e.getKey());
		return Map.ofEntries(entries);
	}
}
