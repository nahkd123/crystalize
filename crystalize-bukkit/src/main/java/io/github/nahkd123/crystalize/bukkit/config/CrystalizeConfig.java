package io.github.nahkd123.crystalize.bukkit.config;

import org.bukkit.configuration.ConfigurationSection;

public record CrystalizeConfig(PackConfig pack) {
	public static final CrystalizeConfig DEFAULT = new CrystalizeConfig(PackConfig.DEFAULT);

	public static CrystalizeConfig fromConfig(ConfigurationSection config) {
		if (config == null) return DEFAULT;
		PackConfig pack = PackConfig.fromConfig(config.getConfigurationSection("pack"));
		return new CrystalizeConfig(pack);
	}
}
