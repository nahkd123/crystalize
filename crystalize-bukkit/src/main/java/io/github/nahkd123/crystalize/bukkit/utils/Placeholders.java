package io.github.nahkd123.crystalize.bukkit.utils;

import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Placeholders {
	public static final Pattern CURLY = Pattern.compile("[{](.+?)[}]");

	public static String apply(String input, Pattern pattern, UnaryOperator<String> mapper) {
		return CURLY.matcher(input).replaceAll(result -> {
			String value = mapper.apply(result.group(1));
			return value != null ? value : Matcher.quoteReplacement(result.group(1));
		});
	}

	public static String apply(String input, UnaryOperator<String> mapper) {
		return apply(input, CURLY, mapper);
	}
}
