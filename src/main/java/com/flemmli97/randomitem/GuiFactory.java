package com.flemmli97.randomitem;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.client.config.DummyConfigElement.DummyCategoryElement;

public class GuiFactory implements IModGuiFactory {

	@Override
	public void initialize(Minecraft minecraftInstance) {}

	@Override
	public boolean hasConfigGui() {
		return true;
	}

	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen) {
		return new GuiConfig(parentScreen, this.list(Config.config), RandomItem.MODID, false, false, RandomItem.MODNAME);
	}
	
	private List<IConfigElement> list(Configuration... configs)
    {
        List<IConfigElement> list = Lists.newArrayList();
        for(Configuration config : configs)
        {
            for(String cat : config.getCategoryNames())
            {
                ConfigCategory category = config.getCategory(cat);
                if(cat.isEmpty())
                {
                    list.addAll(new ConfigElement(category).getChildElements());
                }
                else
                {
                    if (category.isChild())
                        continue;
                    DummyCategoryElement element = new DummyCategoryElement(category.getName(), category.getLanguagekey(), new ConfigElement(category).getChildElements());
                    element.setRequiresMcRestart(category.requiresMcRestart());
                    element.setRequiresWorldRestart(category.requiresWorldRestart());
                    list.add(element);
                }
            }
        }
        return list;
    }

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}
}