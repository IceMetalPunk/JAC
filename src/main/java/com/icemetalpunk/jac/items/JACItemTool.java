package com.icemetalpunk.jac.items;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nullable;

import com.icemetalpunk.jac.JAC;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ResourceLocation;

public class JACItemTool extends ItemTool {

	private boolean effectiveOnEverything = false;

	public JACItemTool(String name, ToolMaterial material, Set<Block> effectiveBlocks) {
		super(material, effectiveBlocks);
		this.setRegistryName(new ResourceLocation(JAC.MODID, name)).setUnlocalizedName(JAC.MODID + "." + name);
	}

	public JACItemTool(String name, ToolMaterial material) {
		this(name, material, new HashSet<Block>());
	}

	public JACItemTool(String name) {
		this(name, ToolMaterial.DIAMOND);
	}

	public void setEffectiveOnEverything(boolean effective) {
		this.effectiveOnEverything = true;
	}

	public boolean isEffectiveOnEverything() {
		return this.effectiveOnEverything;
	}

	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state) {
		if (this.effectiveOnEverything) {
			return 10.0f;
		} else {
			return super.getDestroySpeed(stack, state);
		}
	}

	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass, @Nullable EntityPlayer player,
			@Nullable IBlockState blockState) {
		if (this.effectiveOnEverything) {
			return 1000;
		} else {
			return super.getHarvestLevel(stack, toolClass, player, blockState);
		}
	}

}
