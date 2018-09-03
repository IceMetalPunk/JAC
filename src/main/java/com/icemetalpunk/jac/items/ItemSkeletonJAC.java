package com.icemetalpunk.jac.items;

import java.util.List;

import javax.annotation.Nullable;

import com.icemetalpunk.jac.JAC;
import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSkeletonJAC extends JACItemTool {

	public ItemSkeletonJAC() {
		super("skeleton_jac", ToolMaterial.IRON);
		this.setCreativeTab(JAC.tab);
		this.setEffectiveOnEverything(true);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(ChatFormatting.DARK_AQUA + "JAC: The Pumpkin King" + ChatFormatting.RESET);
	}

}
