package me.nahkd.crystalize.core.objects;

import me.nahkd.crystalize.core.CrystalizeObject;
import me.nahkd.crystalize.core.Expression;
import me.nahkd.crystalize.core.expressions.NumberExpression;

public class ObjectNumber extends CrystalizeObject {

	public final double number;
	
	public ObjectNumber(double number) {
		super("number");
		this.number = number;
	}

	@Override
	public ObjectString toCrystalizeString() {
		return new ObjectString(number + "");
	}

	@Override
	public Expression toExpression() {
		return new NumberExpression(number);
	}
	
	@Override
	public String toString() {
		return number + "";
	}

}
