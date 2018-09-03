package com.icemetalpunk.jac.registries;

import java.util.function.BiConsumer;

import com.icemetalpunk.jac.blocks.BlockDecompressor;
import com.icemetalpunk.jac.blocks.JACBlock;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class JACBlockRegistry extends JACRegistry<Block> {

	public JACBlockRegistry() {
		super("block");
		this.register("decompressor", new BlockDecompressor());
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
	public void registerItemBlocks(RegistryEvent.Register<Item> event) {
		this.process(new BiConsumer<String, Block>() {
			@Override
			public void accept(String name, Block block) {
				if (block instanceof JACBlock) {
					JACBlock jacBlock = (JACBlock) block;
					event.getRegistry().register(jacBlock.getItemBlock());
				}
			}
		});
	}

	@SubscribeEvent
	public void registerModels(ModelRegistryEvent event) {
		this.process(new BiConsumer<String, Block>() {
			@Override
			public void accept(String name, Block block) {
				// ModelHelper.registerItemModel(Item.getItemFromBlock(block));
			}
		});
	}

}
