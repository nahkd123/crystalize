package io.github.nahkd123.crystalize.bukkit.config;

import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;

public record PackConfig(boolean ignore, NamespacedKey preferredBuilder, AutoCopy autoCopy) {

	private static final NamespacedKey DEFAULT_BUILDER = NamespacedKey.fromString("crystalize:default");
	public static final PackConfig DEFAULT = new PackConfig(false, DEFAULT_BUILDER, AutoCopy.DEFAULT);

	public static PackConfig fromConfig(ConfigurationSection config) {
		if (config == null) return DEFAULT;
		boolean ignore = config.getBoolean("ignore", false);
		NamespacedKey preferred = NamespacedKey.fromString(config.getString("preferredBuilder", "crystalize:default"));
		AutoCopy autoCopy = AutoCopy.fromConfig(config.getConfigurationSection("autoCopy"));
		return new PackConfig(ignore, preferred, autoCopy);
	}
}
