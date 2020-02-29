package me.nahkd.crystalize.core.expressions;

import me.nahkd.crystalize.core.CrystalizeObject;
import me.nahkd.crystalize.core.Expression;
import me.nahkd.crystalize.core.objects.ObjectsCompound;

public class VariableExpression extends Expression {
	
	public final CrystalizeObject linked;
	
	public VariableExpression(CrystalizeObject link) {
		linked = link;
	}

	@Override
	public CrystalizeObject toObject(ObjectsCompound instance) {
		return linked;
	}
	
	@Override
	public String toString() {
		return linked.toString();
	}
	
}
