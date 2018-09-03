package com.icemetalpunk.jac.gui;

import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;

public class GuiDecompressor extends GuiFurnace {

	public GuiDecompressor(InventoryPlayer playerInv, IInventory furnaceInv) {
		super(playerInv, furnaceInv);
	}

}
