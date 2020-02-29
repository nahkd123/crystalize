package me.nahkd.crystalize.core;

import me.nahkd.crystalize.core.objects.ObjectString;

public abstract class CrystalizeObject {
	
	public final String typeof;
	
	public CrystalizeObject(String typeof) {
		this.typeof = typeof;
	}
	
	public abstract ObjectString toCrystalizeString();
	public abstract Expression toExpression();
	@Override
	public String toString() {
		return "<" + typeof + " object>";
	}
	
}
