package me.nahkd.crystalize.core;

import me.nahkd.crystalize.core.objects.ObjectsCompound;

public abstract class Expression {
	
	public abstract CrystalizeObject toObject(ObjectsCompound instance);
	
}
