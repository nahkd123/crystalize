package me.nahkd.crystalize.core.objects;

import me.nahkd.crystalize.core.CrystalizeObject;
import me.nahkd.crystalize.core.Expression;

public class ObjectString extends CrystalizeObject {

	public final String value;
	
	public ObjectString(String value) {
		super("string");
		this.value = value;
	}
	
	@Override
	public ObjectString toCrystalizeString() {return this;}
	@Override
	public String toString() {
		return value;
	}

	@Override
	public Expression toExpression() {
		return null;
	}

}
