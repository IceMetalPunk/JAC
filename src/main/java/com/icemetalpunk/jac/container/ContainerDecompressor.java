package com.icemetalpunk.jac.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.IInventory;

public class ContainerDecompressor extends ContainerFurnace {

	public ContainerDecompressor(InventoryPlayer playerInventory, IInventory furnaceInventory) {
		super(playerInventory, furnaceInventory);
		// FIXME: Override as necessary to allow proper fuels and items in.
		// That is, override transferStackInSlot, and also make new types of slots
		// rather than FurnaceFuelSlot, etc. to handle the proper items.
	}

}
