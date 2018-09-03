package com.icemetalpunk.jac.gui;

import com.icemetalpunk.jac.container.ContainerDecompressor;
import com.icemetalpunk.jac.registries.JACGuiRegistry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class JACGuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == JACGuiRegistry.GUI_DECOMPRESSOR) {
			return new ContainerDecompressor(player.inventory, (IInventory) world.getTileEntity(new BlockPos(x, y, z)));
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == JACGuiRegistry.GUI_DECOMPRESSOR) {
			return new GuiDecompressor(player.inventory, (IInventory) world.getTileEntity(new BlockPos(x, y, z)));
		}
		return null;
	}

}
