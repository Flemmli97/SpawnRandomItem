package com.flemmli97.randomitem;

import java.io.File;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class Config {

    public static Configuration config;
    public static List<String> blackList = Lists.newArrayList("minecraft:command_block", "minecraft:structure_void", "minecraft:structure_block",
            "minecraft:barrier", "minecraft:mob_spawner", "minecraft:enchanted_book", "minecraft:chain_command_block", "minecraft:repeating_command_block",
            "minecraft:filled_map", "minecraft:knowledge_book", "minecraft:written_book", "minecraft:bedrock", "minecraft:command_block_minecart");
    public static boolean whiteList;
    private static ItemStack[] items = null;

    public static void load() {
        if(config==null)
        {
            config=new Configuration(new File(Loader.instance().getConfigDir(), "randomitem.cfg"));
            config.load();
        }
        blackList = Lists.newArrayList(config.getStringList("Item Blacklist", "general", blackList.toArray(new String[0]), "BlackList for items. Put just modid to add all items from that mod."));
        whiteList = config.getBoolean("WhiteList", "general", false, "Treat blacklist as whitelist");
        
        NonNullList<ItemStack> list = NonNullList.create();
        ForgeRegistries.ITEMS.getValuesCollection().forEach(item->{
            if(whiteList?contains(item):!contains(item))
                if(item==Items.ENCHANTED_BOOK) {
                    for(CreativeTabs tab : CreativeTabs.CREATIVE_TAB_ARRAY)
                        if(tab!=CreativeTabs.SEARCH)
                            item.getSubItems(tab, list);
                }
                else if(item.getCreativeTab()!=null)
                    item.getSubItems(item.getCreativeTab(), list);
                else
                    list.add(new ItemStack(item));
        });
        items = list.toArray(new ItemStack[0]);
        config.save();
    }
    
    private static boolean contains(Item item) {
        return blackList.contains(item.getRegistryName().toString()) || blackList.contains(item.getRegistryName().getResourceDomain());
    }
    public static ItemStack getRandomItem(Random rand) {
        if(items==null)
            return ItemStack.EMPTY;
        ItemStack item = items[rand.nextInt(items.length)];
        return item.copy();
    }
}
