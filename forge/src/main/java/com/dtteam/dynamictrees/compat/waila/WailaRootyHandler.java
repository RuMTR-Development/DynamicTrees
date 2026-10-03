package com.dtteam.dynamictrees.compat.waila;

import com.dtteam.dynamictrees.DynamicTrees;
import com.dtteam.dynamictrees.block.soil.SoilBlock;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

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
//? }

//~ if < 1.19.2 'IBlockComponentProvider' -> 'IComponentProvider'
public class WailaRootyHandler implements IComponentProvider {

    public static final ResourceLocation ROOTY_UID = DynamicTrees.location("rooty");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if (accessor.getBlock() instanceof final SoilBlock rooty) {
            final int fertility = rooty.getFertility(accessor.getBlockState(), accessor.getLevel(), accessor.getPosition());
            //~ if < 1.19.2 'Component.translatable' -> 'new TranslatableComponent'
            tooltip.add(new TranslatableComponent("tooltip.dynamictrees.fertility", Mth.floor(fertility * 100 / 15f) + "%"));
        }
    }

    //? if >= 1.19.2 {
    /*@Override
    public ResourceLocation getUid() {
        return ROOTY_UID;
    }
    *///? }
}
