package com.dtteam.dynamictrees.event.handler;

import com.dtteam.dynamictrees.DynamicTrees;
import com.dtteam.dynamictrees.config.DTConfigs;
import com.dtteam.dynamictrees.platform.Services;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.common.MinecraftForge;

/**
 * Holds and manages optional event handlers.
 * These handlers are not always active and depend on some condition.
 *
 * @author Harley O'Connor
 */
public final class OptionalHandlers {

    public static final LeafUpdateEventHandler LEAF_UPDATE_EVENT_HANDLER = new LeafUpdateEventHandler();
    public static final VanillaSaplingEventHandler VANILLA_SAPLING_EVENT_HANDLER = new VanillaSaplingEventHandler();

    /**
     * Registers common events, called in {@link DynamicTrees}.
     */
    public static void registerHandlers() {
        IEventBus bus = MinecraftForge.EVENT_BUS;

        if (Services.PLATFORM.isModLoaded(DynamicTrees.FAST_LEAF_DECAY)) {
            bus.register(LEAF_UPDATE_EVENT_HANDLER);
        }
    }

    /**
     * Registers or unregisters event handlers based on config changes. Called when the config is loaded or reloaded in
     * {@link com.dtteam.dynamictrees.config.DTConfigEvents}.
     */
    public static void configReload() {
        registerOrUnregister(VANILLA_SAPLING_EVENT_HANDLER, DTConfigs.COMMON.replaceVanillaSaplings.get());
    }

    /**
     * Registers or unregisters the given object to the {@link MinecraftForge#EVENT_BUS}, depending on the boolean
     * given.
     *
     * @param handler  The handler object to register/unregisters.
     * @param register True if handler should be registered.
     */
    private static void registerOrUnregister(final Object handler, final boolean register) {
        if (register) {
            MinecraftForge.EVENT_BUS.register(handler);
        } else {
            MinecraftForge.EVENT_BUS.unregister(handler);
        }
    }

}
