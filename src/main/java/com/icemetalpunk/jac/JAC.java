/*
 * TODO:
 * 1. Add Skeleton JAC, with a chance of dropping pumpkins when used.
 * 2. Add Compressor.
 * 3. Add Decompressor.
 * 4. ??
 * 5. Profit!
 */
package com.icemetalpunk.jac;

import org.apache.logging.log4j.Logger;

import com.icemetalpunk.jac.events.JACEventHandler;
import com.icemetalpunk.jac.proxy.JACProxy;
import com.icemetalpunk.jac.util.CompressionLookup;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = JAC.MODID, name = JAC.NAME, version = JAC.VERSION)
public class JAC {
	public static final String MODID = "jacmod";
	public static final String NAME = "JAC";
	public static final String VERSION = "1.0";

	private static Logger logger;

	@SidedProxy(clientSide = "com.icemetalpunk.jac.proxy.JACClient", serverSide = "com.icemetalpunk.jac.proxy.JACServer")
	public static JACProxy proxy;

	public static CreativeTabs tab = new CreativeTabs("jacmod") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(proxy.items.get("jac"));
		}
	};

	public static JACEventHandler eventHandler = new JACEventHandler();

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
		eventHandler.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
		CompressionLookup.buildLookup();
	}
}
