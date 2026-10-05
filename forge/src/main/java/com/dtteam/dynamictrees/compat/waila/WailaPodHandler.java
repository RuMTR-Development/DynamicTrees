package com.dtteam.dynamictrees.compat.waila;

import com.dtteam.dynamictrees.DynamicTrees;
import com.dtteam.dynamictrees.block.pod.PodBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

//? if >= 1.19.4 {
import net.minecraft.core.registries.BuiltInRegistries;
//? } else {
/*import net.minecraftforge.registries.ForgeRegistries;
*///? }

//? if >= 1.19.2 {
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
//? } else {
/*import mcp.mobius.waila.api.BlockAccessor;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.config.IPluginConfig;
import net.minecraft.network.chat.TranslatableComponent;
*///? }

//~ if < 1.19.2 'IBlockComponentProvider' -> 'IComponentProvider'
public class WailaPodHandler implements IBlockComponentProvider {

    private static final ResourceLocation POD_UID = DynamicTrees.location("pod");

    /* Used to switch off component for cocoa, since Jade already supports this. */
    public static final ResourceLocation COCOA = DynamicTrees.location("cocoa");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        //~ if < 1.19.4 'BuiltInRegistries.BLOCK' -> 'ForgeRegistries.BLOCKS'
        if (accessor.getBlock() instanceof PodBlock podBlock && !BuiltInRegistries.BLOCK.getKey(accessor.getBlock()).equals(COCOA)) {
            float ageAsPercentage = podBlock.getAgeAsPercentage(accessor.getBlockState());
            //~ if < 1.19.2 'Component.translatable' -> 'new TranslatableComponent'
            tooltip.add(Component.translatable(
                    "tooltip.jade.crop_growth",
                    ageAsPercentage < 100F ? String.format("%.0f%%", ageAsPercentage) :
                            //~ if < 1.19.2 'Component.translatable' -> 'new TranslatableComponent'
                            Component.translatable("tooltip.jade.crop_mature").withStyle(ChatFormatting.GREEN)
            ));
        }
    }

    //? if >= 1.19.2 {
    @Override
    public ResourceLocation getUid() {
        return POD_UID;
    }
    //? }
}