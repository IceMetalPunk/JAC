package com.icemetalpunk.jac.blocks;

import com.icemetalpunk.jac.JAC;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class JACItem extends Item {

	public JACItem(String name) {
		super();
		this.setRegistryName(new ResourceLocation(JAC.MODID, name)).setUnlocalizedName(JAC.MODID + "." + name);
	}

}
