package com.icemetalpunk.jac.registries;

import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class JACRegistry<T> {
	private HashMap<String, T> registry = new HashMap<>();
	private String registryName = "Object";

	public JACRegistry(String name) {
		this.registryName = name;
	}

	public T get(String name) {
		if (!registry.containsKey(name)) {
			System.err.println("Trying to get unregistered " + this.registryName + " " + name + ". Returning null.");
		}
		return registry.get(name);
	}

	public boolean register(String name, T object) {
		if (registry.containsKey(name)) {
			System.err.println(
					"Trying to register the already-registered " + this.registryName + " " + name + ". Skipping.");
			return false;
		} else {
			registry.put(name, object);
			return true;
		}
	}
	public void process(BiConsumer<? super String, ? super T> callback) {
		registry.forEach(callback);
	}

}
