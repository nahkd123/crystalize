package me.nahkd.crystalize.core;

public class LangMatcher {
	
	public String matchingString;
	public int mainPointer;
	public int nextPointer;
	public String previousMatched;
	
	public int curlyBracketsCounter;
	
	public LangMatcher(String matchingString) {
		reset(matchingString);
	}
	
	public void reset(String matchingString) {
		this.matchingString = matchingString;
		resetPointers();
		previousMatched = "";
	}
	public void resetPointers() {
		mainPointer = 0;
		nextPointer = 0;
		curlyBracketsCounter = 0;
	}
	
	public boolean next(String... matches) {
		for (String match : matches) {
			if ((match.equalsIgnoreCase("!!!eos") && endOfString()) || (mainPointer + match.length() <= matchingString.length() && matchingString.substring(mainPointer, mainPointer + match.length()).equals(match))) {
				nextPointer = mainPointer + (match.equalsIgnoreCase("!!!eos")? 0 : match.length());
				previousMatched = match;
				return true;
			}
		}
		return false;
	}
	public String nextUntil(String... matches) {
		String out = "";
		while (!next(matches)) {
			out += matchingString.charAt(mainPointer);
			mainPointer++;
		}
		accept();
		return out;
	}
	
	public String nextWithBrackets() {
		String out = "";
		while (curlyBracketsCounter > 0 || matchingString.charAt(mainPointer) != '}') {
			char ch = matchingString.charAt(mainPointer);
			if (ch == '{') curlyBracketsCounter++;
			else if (ch == '}') curlyBracketsCounter--;
			out += ch;
			mainPointer++;
		}
		mainPointer++;
		return out;
	}
	
	public String acceptRet() {
		String out = matchingString.substring(mainPointer, nextPointer - mainPointer);
		mainPointer = nextPointer;
		return out;
	}
	public void accept() {
		mainPointer = nextPointer;
	}
	
	public boolean endOfString() {
		return mainPointer >= matchingString.length();
	}
	
}
