package com.icemetalpunk.jac.proxy;

import com.icemetalpunk.jac.registries.JACBlockRegistry;
import com.icemetalpunk.jac.registries.JACItemRegistry;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public abstract class JACProxy {
	public JACBlockRegistry blocks = new JACBlockRegistry();
	public JACItemRegistry items = new JACItemRegistry();

	public abstract void preInit(FMLPreInitializationEvent event);

	public abstract void init(FMLInitializationEvent event);

	public abstract void postInit(FMLPostInitializationEvent event);
}
