package com.dtteam.dynamictrees.compat.waila;

import com.dtteam.dynamictrees.DynamicTrees;
import com.dtteam.dynamictrees.block.DynamicBlockProperties;
import com.dtteam.dynamictrees.block.fruit.FruitBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;

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
public class WailaFruitHandler implements IBlockComponentProvider {

    public static final ResourceLocation FRUIT_UID = DynamicTrees.location("fruit");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if (accessor.getBlock() instanceof FruitBlock fruitBlock) {
            /* Used to switch off component for fruit with pre-made ages, since Jade already supports these. */
            if (Arrays.stream(DynamicBlockProperties.defaultAges).anyMatch(a -> fruitBlock.getMaxAge() == a)) return;
            float ageAsPercentage = fruitBlock.getAgeAsPercentage(accessor.getBlockState());
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
        return FRUIT_UID;
    }
    //? }
}
