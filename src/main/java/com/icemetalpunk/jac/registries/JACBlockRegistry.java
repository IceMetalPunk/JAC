package com.icemetalpunk.jac.registries;

import java.util.function.BiConsumer;

import com.icemetalpunk.jac.util.ModelHelper;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class JACBlockRegistry extends JACRegistry<Block> {

	public JACBlockRegistry() {
		super("block");
		// TODO: Register blocks here
	}

	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> event) {
		this.process(new BiConsumer<String, Block>() {
			@Override
			public void accept(String name, Block block) {
				event.getRegistry().register(block);
			}
		});
	}

	@SubscribeEvent
	public void registerModels(ModelRegistryEvent event) {
		this.process(new BiConsumer<String, Block>() {
			@Override
			public void accept(String name, Block block) {
				ModelHelper.registerItemModel(Item.getItemFromBlock(block));
			}
		});
	}

}
