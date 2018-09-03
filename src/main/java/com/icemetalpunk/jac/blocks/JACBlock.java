package com.icemetalpunk.jac.blocks;

import com.icemetalpunk.jac.JAC;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;

public class JACBlock extends Block {

	private ItemBlock itemBlock = new ItemBlock(this);

	public JACBlock(String name, Material materialIn) {
		super(materialIn);
		init(name);
	}

	public JACBlock(String name, Material materialIn, MapColor mapColor) {
		super(materialIn, mapColor);
		init(name);
	}

	public void init(String name) {
		this.setRegistryName(new ResourceLocation(JAC.MODID, name)).setUnlocalizedName(JAC.MODID + "." + name);
		this.setCreativeTab(JAC.tab);
		this.itemBlock.setRegistryName(this.getRegistryName());
	}

	public ItemBlock getItemBlock() {
		return this.itemBlock;
	}

}
