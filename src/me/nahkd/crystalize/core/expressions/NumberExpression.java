package me.nahkd.crystalize.core.expressions;

import me.nahkd.crystalize.core.CrystalizeObject;
import me.nahkd.crystalize.core.Expression;
import me.nahkd.crystalize.core.objects.ObjectNumber;
import me.nahkd.crystalize.core.objects.ObjectsCompound;

public class NumberExpression extends Expression {
	
	public final double number;
	
	public NumberExpression(double number) {
		this.number = number;
	}

	@Override
	public CrystalizeObject toObject(ObjectsCompound instance) {
		return new ObjectNumber(number);
	}
	
	@Override
	public String toString() {
		return "<number expression: " + number + ">";
	}
	
}
