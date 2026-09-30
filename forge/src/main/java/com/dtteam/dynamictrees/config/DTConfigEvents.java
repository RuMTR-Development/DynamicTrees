package com.dtteam.dynamictrees.config;

import com.dtteam.dynamictrees.DynamicTrees;
import com.dtteam.dynamictrees.event.handler.OptionalHandlers;
import com.dtteam.dynamictrees.systems.season.SeasonCompatibilityHandler;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = DynamicTrees.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DTConfigEvents {

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent.Loading event) {
        if (
            event.getConfig().getSpec() == DTConfigs.COMMON_CONFIG
        ) {
            OptionalHandlers.configReload();
            SeasonCompatibilityHandler.reloadSeasonManager();
        }
    }

    @SubscribeEvent
    public static void onReload(final ModConfigEvent.Reloading event) {
        if (
            event.getConfig().getSpec() == DTConfigs.COMMON_CONFIG
        ) {
            OptionalHandlers.configReload();
            SeasonCompatibilityHandler.reloadSeasonManager();
        }
    }

}
