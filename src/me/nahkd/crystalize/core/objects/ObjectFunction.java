package me.nahkd.crystalize.core.objects;

import me.nahkd.crystalize.core.CrystalizeCore;
import me.nahkd.crystalize.core.CrystalizeObject;
import me.nahkd.crystalize.core.CrystalizeStatement;
import me.nahkd.crystalize.core.Expression;
import me.nahkd.crystalize.core.expressions.FunctionExpression;

public class ObjectFunction extends CrystalizeObject {

	public final CrystalizeStatement[] statements;
	public final String orignal;
	public final String[] args;
	
	public ObjectFunction(String orignalString, String[] args, CrystalizeStatement... statements) {
		super("function");
		this.statements = statements;
		this.orignal = orignalString;
		this.args = args;
	}
	
	public Expression validate(ObjectsCompound instance, CrystalizeObject... args) {
		ObjectsCompound newInstance = new ObjectsCompound(instance);
		return CrystalizeCore.eval(newInstance, statements);
	}

	@Override
	public ObjectString toCrystalizeString() {
		return new ObjectString(orignal);
	}
	
	@Override
	public String toString() {
		return orignal;
	}

	@Override
	public Expression toExpression() {
		return new FunctionExpression(this);
	}

}
