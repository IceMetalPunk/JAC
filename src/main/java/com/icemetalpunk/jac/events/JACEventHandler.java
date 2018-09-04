package com.icemetalpunk.jac.events;

import java.util.Arrays;
import java.util.List;

import com.icemetalpunk.jac.JAC;
import com.icemetalpunk.jac.util.CompressionLookup;
import com.icemetalpunk.jac.util.CompressionLookup.MultiItem;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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
	List<Item> jacItems;

	public JACEventHandler() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	public void init() {
		jacItems = Arrays.asList(new Item[] { JAC.proxy.items.get("jac"), JAC.proxy.items.get("skeleton_jac") });
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
		Item handItem = handItemStack.getItem();

		if (!jacItems.contains(handItem)) {
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
				if (handItem == JAC.proxy.items.get("skeleton_jac") && world.rand.nextInt(5) == 1) {
					drops.add(new ItemStack(Item.getItemFromBlock(Blocks.PUMPKIN)));
				}
			} else {
				event.setDropChance(0);
				drops.clear();
			}
		}
	}
}
