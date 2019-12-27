package com.flemmli97.randomitem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = RandomItem.MODID, name = RandomItem.MODNAME, version = RandomItem.VERSION, guiFactory = "com.flemmli97.randomitem.GuiFactory")
public class RandomItem {

    public static final String MODID = "randomitem";
    public static final String MODNAME = "RandomItem";
    public static final String VERSION = "${@VERSION}";
    public static final Logger logger = LogManager.getLogger(RandomItem.MODNAME);
        
    @Instance
    public static RandomItem instance = new RandomItem();
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        Config.load();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @EventHandler
    public void init(FMLInitializationEvent e) {
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {
    }
    
    @SubscribeEvent
    public void config(OnConfigChangedEvent event)
    {
        if(event.getModID().equals(RandomItem.MODID))
            Config.load();
    }
    
    @EventHandler
    public void serverStart(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandSpawnItem());
    }
}
    