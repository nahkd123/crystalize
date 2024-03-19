package io.github.nahkd123.crystalize.blockbench.utils;

import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
import java.util.List;
import java.util.function.Consumer;

public class HierarchicalRecords {
	public static void print(Record record, String prefix, String head, Consumer<String> linePrinter) {
		try {
			RecordComponent[] components = record.getClass().getRecordComponents();
			linePrinter.accept(prefix + head + record.getClass().getSimpleName() + " {");

			for (RecordComponent component : components) {
				Method accessor = component.getAccessor();
				String name = component.getName();
				accessor.setAccessible(true);
				Object object = accessor.invoke(record);
				accessor.setAccessible(false);

				if (object instanceof Record childRecord) print(childRecord, prefix + "  ", name + " = ", linePrinter);
				else if (object instanceof List list) {
					if (list.size() == 0) {
						linePrinter.accept(prefix + "  " + name + " (" + list.size() + ")");
						continue;
					}

					linePrinter.accept(prefix + "  " + name + " (" + list.size() + ") = [");

					for (Object obj : list) {
						if (obj instanceof Record listRecord)
							print(listRecord, prefix + "    ", "", linePrinter);
						else linePrinter.accept(prefix + "    " + obj);
					}

					linePrinter.accept(prefix + "  ]");
				} else {
					linePrinter.accept(prefix + "  " + name + " = " + object);
				}
			}

			linePrinter.accept(prefix + "}");
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
			linePrinter.accept(record.toString());
		}
	}
}
