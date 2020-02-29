package me.nahkd.crystalize.lang.functions;

import me.nahkd.crystalize.core.CrystalizeObject;
import me.nahkd.crystalize.core.Expression;
import me.nahkd.crystalize.core.objects.InternalFunction;
import me.nahkd.crystalize.core.objects.ObjectsCompound;

public class InternalFunctionPrint extends InternalFunction {

	@Override
	public Expression validate(ObjectsCompound instance, CrystalizeObject... args) {
		for (CrystalizeObject obj : args) {
			System.out.print(obj + " ");
		}
		System.out.println();
		return null;
	}

}
