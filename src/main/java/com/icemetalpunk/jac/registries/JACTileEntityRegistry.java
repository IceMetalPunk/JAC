package com.icemetalpunk.jac.registries;

import java.util.function.BiConsumer;

import com.icemetalpunk.jac.JAC;
import com.icemetalpunk.jac.tileentity.TileEntityDecompressor;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class JACTileEntityRegistry extends JACRegistry<Class<? extends TileEntity>> {

	public JACTileEntityRegistry() {
		super("item");
		this.register("decompressor", TileEntityDecompressor.class);
	}

	@SubscribeEvent
	public void registerTileEntities(RegistryEvent.Register<Block> event) {
		this.process(new BiConsumer<String, Class<? extends TileEntity>>() {
			@Override
			public void accept(String name, Class<? extends TileEntity> tileEntity) {
				GameRegistry.registerTileEntity(tileEntity, new ResourceLocation(JAC.MODID, name));
			}
		});
	}

}
