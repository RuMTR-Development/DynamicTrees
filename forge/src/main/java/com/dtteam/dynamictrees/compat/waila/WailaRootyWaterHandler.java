package com.dtteam.dynamictrees.compat.waila;

import com.dtteam.dynamictrees.DynamicTrees;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

//? if >= 1.19.2 {
/*import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
*///? } else {
import mcp.mobius.waila.api.BlockAccessor;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.config.IPluginConfig;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.TextComponent;
//? }

//~ if < 1.19.2 'IBlockComponentProvider' -> 'IComponentProvider'
public class WailaRootyWaterHandler implements IComponentProvider {

    public static final ResourceLocation ROOTY_WATER_UID = DynamicTrees.location("rooty_water");
    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        //~ if < 1.19.2 'Component.translatable' -> 'new TranslatableComponent'
        //~ if < 1.19.2 'Component.literal' -> 'new TextComponent'
        tooltip.add(new TextComponent(ChatFormatting.WHITE + new TranslatableComponent(accessor.getBlock().getDescriptionId()).getString()));
    }

    //? if >= 1.19.2 {
    /*@Override
    public ResourceLocation getUid() {
        return ROOTY_WATER_UID;
    }
    *///? }
}
