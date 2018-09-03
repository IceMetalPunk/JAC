package com.icemetalpunk.jac.events;

import java.util.List;

import com.icemetalpunk.jac.JAC;
import com.icemetalpunk.jac.util.CompressionLookup;
import com.icemetalpunk.jac.util.CompressionLookup.MultiItem;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class JACEventHandler {
	public JACEventHandler() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void decompressJACDrops(BlockEvent.HarvestDropsEvent event) {

		if (event.isSilkTouching()) {
			return;
		}

		EntityPlayer player = event.getHarvester();
		if (player == null) {
			return;
		}
		ItemStack handItemStack = player.getHeldItem(player.getActiveHand());
		ItemStack jacItemStack = new ItemStack(JAC.proxy.items.get("jac"));
		if (!ItemStack.areItemsEqualIgnoreDurability(handItemStack, jacItemStack)) {
			return;
		}

		IBlockState state = event.getState();
		Block block = state.getBlock();
		List<ItemStack> drops = event.getDrops();
		World world = event.getWorld();
		int metaData = block.getMetaFromState(state);
		ItemStack itemBlock = new ItemStack(Item.getItemFromBlock(block), 1, metaData);
		MultiItem multiItem = new MultiItem(itemBlock);
		if (itemBlock != null && itemBlock.getItem() != Items.AIR) {
			if (CompressionLookup.decompressionRecipes.containsKey(multiItem)) {
				event.setDropChance(1);
				drops.clear();
				drops.add(CompressionLookup.decompressionRecipes.get(multiItem).copy());
			} else {
				event.setDropChance(0);
				drops.clear();
			}
		}
	}
}
