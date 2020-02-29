package me.nahkd.crystalize.utils;

import java.text.NumberFormat;
import java.text.ParsePosition;

/**
 * Apache? No we don't want to use it for now...
 * @author nahkd123
 *
 */
public class NumberUtils {
	
	static NumberFormat formatter = NumberFormat.getInstance();
	
	public static final boolean isNumeric(String str) {
		ParsePosition parsePos = new ParsePosition(0);
		formatter.parse(str, parsePos);
		return parsePos.getIndex() == str.length();
	}
	
}
