package com.dtteam.dynamictrees.compat.waila;

import com.dtteam.dynamictrees.DynamicTrees;
import com.dtteam.dynamictrees.block.pod.PodBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

//? if >= 1.19.4 {
/*import net.minecraft.core.registries.BuiltInRegistries;
*///? } else {
import net.minecraftforge.registries.ForgeRegistries;
//? }

public class WailaPodHandler implements IBlockComponentProvider {

    private static final ResourceLocation POD_UID = DynamicTrees.location("pod");

    /* Used to switch off component for cocoa, since Jade already supports this. */
    public static final ResourceLocation COCOA = DynamicTrees.location("cocoa");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        //~ if < 1.19.4 'BuiltInRegistries.BLOCK' -> 'ForgeRegistries.BLOCKS'
        if (accessor.getBlock() instanceof PodBlock podBlock && !ForgeRegistries.BLOCKS.getKey(accessor.getBlock()).equals(COCOA)) {
            float ageAsPercentage = podBlock.getAgeAsPercentage(accessor.getBlockState());
            tooltip.add(Component.translatable(
                    "tooltip.jade.crop_growth",
                    ageAsPercentage < 100F ? String.format("%.0f%%", ageAsPercentage) :
                            Component.translatable("tooltip.jade.crop_mature").withStyle(ChatFormatting.GREEN)
            ));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return POD_UID;
    }
}