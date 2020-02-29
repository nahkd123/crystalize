package me.nahkd.crystalize.core.expressions;

import me.nahkd.crystalize.core.CrystalizeObject;
import me.nahkd.crystalize.core.Expression;
import me.nahkd.crystalize.core.objects.ObjectFunction;
import me.nahkd.crystalize.core.objects.ObjectsCompound;

public class FunctionExpression extends Expression {
	
	public final ObjectFunction linkedFunction;
	public final Expression[] exps;
	
	public FunctionExpression(ObjectFunction link, Expression... exps) {
		linkedFunction = link;
		this.exps = exps;
	}

	@Override
	public CrystalizeObject toObject(ObjectsCompound instance) {
		return linkedFunction.validate(instance).toObject(instance);
	}
	
	@Override
	public String toString() {
		return "<function expression: " + linkedFunction + ">";
	}
	
}
