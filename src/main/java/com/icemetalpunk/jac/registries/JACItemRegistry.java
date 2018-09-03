package com.icemetalpunk.jac.registries;

import java.util.function.BiConsumer;

import com.icemetalpunk.jac.items.ItemJAC;
import com.icemetalpunk.jac.util.ModelHelper;

import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class JACItemRegistry extends JACRegistry<Item> {

	public JACItemRegistry() {
		super("item");
		// TODO: Register items here
		this.register("jac", new ItemJAC());
	}

	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event) {
		this.process(new BiConsumer<String, Item>() {
			@Override
			public void accept(String name, Item item) {
				event.getRegistry().register(item);
			}
		});
	}

	@SubscribeEvent
	public void registerModels(ModelRegistryEvent event) {
		this.process(new BiConsumer<String, Item>() {
			@Override
			public void accept(String name, Item item) {
				ModelHelper.registerItemModel(item);
			}
		});
	}

}
