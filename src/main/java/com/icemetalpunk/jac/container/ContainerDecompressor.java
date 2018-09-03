package com.icemetalpunk.jac.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.IInventory;

public class ContainerDecompressor extends ContainerFurnace {

	public ContainerDecompressor(InventoryPlayer playerInventory, IInventory furnaceInventory) {
		super(playerInventory, furnaceInventory);
	}

}
