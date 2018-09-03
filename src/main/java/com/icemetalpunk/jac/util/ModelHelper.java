package com.icemetalpunk.jac.util;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ModelHelper {
	public static void registerItemModel(Item item) {
		ModelResourceLocation model = new ModelResourceLocation(item.getRegistryName(), "inventory");
		ModelLoader.registerItemVariants(item, model);
		ModelLoader.setCustomModelResourceLocation(item, 0, model);
	}
}
