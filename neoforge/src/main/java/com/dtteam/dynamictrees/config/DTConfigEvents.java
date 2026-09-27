package com.dtteam.dynamictrees.config;

import com.dtteam.dynamictrees.DynamicTrees;
import com.dtteam.dynamictrees.event.handler.OptionalHandlers;
import com.dtteam.dynamictrees.systems.season.SeasonCompatibilityHandler;
import fuzs.forgeconfigapiport.neoforge.impl.forge.ForgeConfigSpecAdapter;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;

@EventBusSubscriber(modid = DynamicTrees.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class DTConfigEvents {

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent.Loading event) {
        if (
            event.getConfig().getSpec() instanceof ForgeConfigSpecAdapter spec &&
            spec.spec() == DTConfigs.COMMON_CONFIG
        ) {
            OptionalHandlers.configReload();
            SeasonCompatibilityHandler.reloadSeasonManager();
        }
    }

    @SubscribeEvent
    public static void onReload(final ModConfigEvent.Reloading event) {
        if (
            event.getConfig().getSpec() instanceof ForgeConfigSpecAdapter spec &&
            spec.spec() == DTConfigs.COMMON_CONFIG
        ) {
            OptionalHandlers.configReload();
            SeasonCompatibilityHandler.reloadSeasonManager();
        }
    }

}
