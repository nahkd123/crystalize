package me.nahkd.crystalize.test;

import me.nahkd.crystalize.core.CrystalizeObject;
import me.nahkd.crystalize.core.Expression;
import me.nahkd.crystalize.core.expressions.NumberExpression;
import me.nahkd.crystalize.core.objects.InternalFunction;
import me.nahkd.crystalize.core.objects.ObjectsCompound;

public class InternalFunctionTest extends InternalFunction {

	@Override
	public Expression validate(ObjectsCompound instance, CrystalizeObject... args) {
		System.out.println(args.length);
		return new NumberExpression(1269);
	}

}
