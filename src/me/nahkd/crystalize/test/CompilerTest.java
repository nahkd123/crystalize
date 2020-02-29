package me.nahkd.crystalize.test;

import me.nahkd.crystalize.core.CrystalizeCore;
import me.nahkd.crystalize.core.CrystalizeStatement;
import me.nahkd.crystalize.core.objects.ObjectsCompound;

public class CompilerTest {

	public static void main(String[] args) {
		ObjectsCompound instance = new ObjectsCompound();
		CrystalizeCore.setupCore(instance);
		instance.objects.put("test", new InternalFunctionTest());
		CrystalizeStatement[] compiled = CrystalizeCore.compile(""
				+ "print(\"hi\" + \" abc\")"
				+ "", instance);
		System.out.println(CrystalizeCore.eval(instance, compiled));
	}

}
