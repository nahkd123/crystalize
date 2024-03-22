package io.github.nahkd123.crystalize.bukkit.config;

import java.nio.file.Path;

import org.bukkit.configuration.ConfigurationSection;

import io.github.nahkd123.crystalize.bukkit.utils.Placeholders;

public record AutoCopy(boolean enable, String copyTo) {
	private static final String DEFAULT_PATH = "{minecraftClientHome}/resourcepacks/crystalize.zip";
	public static final AutoCopy DEFAULT = new AutoCopy(false, DEFAULT_PATH);

	private static String getMinecraftHome() {
		String osName = System.getProperty("os.name");
		if (osName.startsWith("Windows ")) return Path.of(System.getenv("APPDATA"), ".minecraft").toString();
		return Path.of(System.getProperty("user.home"), ".minecraft").toString();
	}

	public Path getCopyPath() {
		return Path.of(Placeholders.apply(copyTo, k -> switch (k) {
		case "minecraftClientHome" -> getMinecraftHome();
		default -> null;
		}));
	}

	public static AutoCopy fromConfig(ConfigurationSection config) {
		if (config == null) return DEFAULT;
		boolean enable = config.getBoolean("enable", false);
		String copyTo = config.getString("copyTo", DEFAULT_PATH);
		return new AutoCopy(enable, copyTo);
	}
}
