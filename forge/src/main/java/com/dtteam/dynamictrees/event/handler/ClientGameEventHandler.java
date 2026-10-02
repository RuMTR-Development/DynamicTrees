package com.dtteam.dynamictrees.event.handler;

import com.dtteam.dynamictrees.DynamicTrees;
import com.dtteam.dynamictrees.api.season.ClimateZoneType;
import com.dtteam.dynamictrees.api.worldgen.LevelContext;
import com.dtteam.dynamictrees.client.Tooltips;
import com.dtteam.dynamictrees.item.Seed;
import com.dtteam.dynamictrees.systems.season.ClimateHelper;
import com.dtteam.dynamictrees.systems.season.SeasonHelper;
import com.dtteam.dynamictrees.tree.species.Species;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

@Mod.EventBusSubscriber(modid = DynamicTrees.MOD_ID, value = Dist.CLIENT)
public class ClientGameEventHandler {

    ///////////////////////////////////////////
    // ITEM
    ///////////////////////////////////////////

    @SubscribeEvent
    public static void onItemTooltipAdded(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        Item item = stack.getItem();

        if (!(item instanceof Seed seed)) {
            return;
        }

        Player player = event.getEntity();

        if (player == null) {
            return;
        }

        LevelContext levelContext = LevelContext.create(player.level);
        Species species = seed.getSpecies();

        if (SeasonHelper.getSeasonValue(levelContext, BlockPos.ZERO) == null || !species.isValid()) {
            return;
        }

        //~ if < 1.19.4 'BlockPos.containing' -> 'new BlockPos'
        BlockPos playerPos = new BlockPos(player.position());
        ClimateZoneType climate = ClimateHelper.getClimate(player.level, playerPos);
        int flags = seed.getSpecies().getSeasonalTooltipFlags(levelContext, player);
        Tooltips.applySeasonalTooltips(event.getToolTip(), flags, climate);
    }

}