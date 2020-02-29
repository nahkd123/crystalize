package me.nahkd.crystalize.core.expressions;

import me.nahkd.crystalize.core.CrystalizeObject;
import me.nahkd.crystalize.core.Expression;
import me.nahkd.crystalize.core.objects.ObjectString;
import me.nahkd.crystalize.core.objects.ObjectsCompound;

public class StringExpression extends Expression {

	public final String str;
	
	public StringExpression(String str) {
		this.str = str;
	}
	
	@Override
	public CrystalizeObject toObject(ObjectsCompound instance) {
		return new ObjectString(str);
	}
	
	@Override
	public String toString() {
		return str;
	}

}
