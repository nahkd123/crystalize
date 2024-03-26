package io.github.nahkd123.crystalize.fabric.utils;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * <p>
 * A modifiable handle caches previous value to compare previous with current in
 * order to determine whether it should call callback or not. Mainly used for
 * sending update only when value is changed, such as translation or origin.
 * </p>
 * 
 * @param <T> The value type.
 */
public class ModifiableHandle<T> {
	private final T last, current;
	private boolean initialized = false;
	private BiConsumer<T, T> setter;

	public ModifiableHandle(Supplier<T> supplier, BiConsumer<T, T> setter) {
		this.setter = setter;
		this.last = supplier.get();
		this.current = supplier.get();
	}

	public T getLast() { return last; }

	public T getCurrent() { return current; }

	public boolean isModified() { return !initialized || !last.equals(current); }

	public void onModified(Consumer<T> consumer) {
		if (isModified()) {
			initialized = true;
			consumer.accept(current);
			setter.accept(last, current);
		}
	}
}
