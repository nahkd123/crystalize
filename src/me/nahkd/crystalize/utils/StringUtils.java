package me.nahkd.crystalize.utils;

public class StringUtils {
	
	public static final String trim(String str) {
		int start = 0;
		int end = str.length() - 1;
		while (str.charAt(start) == ' ') start++;
		while (str.charAt(end) == ' ') end--;
		return str.substring(start, end + 1);
	}
	
}
