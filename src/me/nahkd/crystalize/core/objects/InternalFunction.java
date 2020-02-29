package me.nahkd.crystalize.core.objects;

import me.nahkd.crystalize.core.CrystalizeObject;
import me.nahkd.crystalize.core.CrystalizeStatement;
import me.nahkd.crystalize.core.Expression;

public abstract class InternalFunction extends ObjectFunction {

	public InternalFunction(String... args) {
		super("<internal function>", args, (CrystalizeStatement[]) null);
	}
	
	@Override
	public abstract Expression validate(ObjectsCompound instance, CrystalizeObject... args);

}
