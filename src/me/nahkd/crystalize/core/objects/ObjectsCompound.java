package me.nahkd.crystalize.core.objects;

import java.util.HashMap;

import me.nahkd.crystalize.core.CrystalizeObject;
import me.nahkd.crystalize.core.Expression;

public class ObjectsCompound extends CrystalizeObject {

	public HashMap<String, CrystalizeObject> objects;
	public ObjectsCompound parent;
	
	public ObjectsCompound(ObjectsCompound parent) {
		super("object");
		objects = new HashMap<String, CrystalizeObject>();
		this.parent = parent;
	}
	public ObjectsCompound() {
		this(null);
	}
	
	public CrystalizeObject getObject(String namedKey) {
		String[] splitedKeys = namedKey.split("\\.");
		CrystalizeObject obj = this;
		for (int i = 0; i < splitedKeys.length; i++) obj = ((ObjectsCompound) obj).objects.get(splitedKeys[i]);
		if (obj == null) return (parent != null)? parent.getObject(namedKey) : null;
		return obj;
	}
	public boolean hasObject(String namedKey) {
		String[] splitedKeys = namedKey.split("\\.");
		CrystalizeObject obj = this;
		for (int i = 0; i < splitedKeys.length; i++) {
			if (!(obj instanceof ObjectsCompound)) return false; // Failed while checking
			obj = ((ObjectsCompound) obj).objects.get(splitedKeys[i]);
			if (obj == null) return (parent != null)? parent.hasObject(namedKey) : false;
		}
		return true;
	}

	@Override
	public ObjectString toCrystalizeString() {
		return null;
	}

	@Override
	public Expression toExpression() {
		return null;
	}

}
