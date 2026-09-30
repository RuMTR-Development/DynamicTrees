package com.dtteam.dynamictrees.event.handler;

import com.dtteam.dynamictrees.DynamicTrees;
import com.dtteam.dynamictrees.api.worldgen.LevelContext;
import com.dtteam.dynamictrees.command.DTCommand;
import com.dtteam.dynamictrees.config.DTConfigs;
import com.dtteam.dynamictrees.recipe.DendroPotionRecipeHandler;
import com.dtteam.dynamictrees.systems.FutureBreak;
import com.dtteam.dynamictrees.systems.poissondisc.UniversalPoissonDiscProvider;
import com.dtteam.dynamictrees.systems.season.SeasonCompatibilityHandler;
import com.dtteam.dynamictrees.systems.season.SeasonHelper;
import com.dtteam.dynamictrees.treepack.Resources;
import com.dtteam.dynamictrees.worldgen.BiomeDatabases;
import com.dtteam.dynamictrees.worldgen.feature.DynamicTreeFeature;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.level.ChunkDataEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DynamicTrees.MOD_ID)
public class CommonGameEventHandler {

    ///////////////////////////////////////////
    // LEVEL
    ///////////////////////////////////////////

    @SubscribeEvent
    public static void onPreLevelTick(TickEvent.LevelTickEvent event) {
        if (!event.level.isClientSide()) {
            FutureBreak.process(event.level);
        }
        SeasonHelper.updateTick(event.level, event.level.getDayTime());
    }

    @SubscribeEvent
    public static void onLevelLoad(LevelEvent.Load event) {
        if (event.getLevel().isClientSide()) {
            ClientModEventHandler.discoverWoodColors();
        } else {
            BiomeDatabases.populateBlacklistFromConfig();
        }
    }

    /**
     * We'll use this instead because at least new chunks aren't created after the world is unloaded. I hope. >:(
     */
    @SubscribeEvent
    public static void onLevelUnload(LevelEvent.Unload event) {
        final LevelAccessor level = event.getLevel();
        if (!level.isClientSide()) {
            DynamicTreeFeature.DISC_PROVIDER.unloadWorld((ServerLevel) level);//clears the circles
        }
    }

    @SubscribeEvent
    public static void onChunkDataLoad(ChunkDataEvent.Load event) {
        if (!DTConfigs.SERVER.worldGen.get()) return;

        final LevelAccessor level = event.getLevel();

		if (level == null || level.isClientSide()) {
			return;
		}

        final byte[] circleData = event.getData().getByteArray(UniversalPoissonDiscProvider.CIRCLE_DATA_ID);
        final UniversalPoissonDiscProvider discProvider = DynamicTreeFeature.DISC_PROVIDER;

        final ChunkPos chunkPos = event.getChunk().getPos();
        discProvider.setChunkPoissonData(LevelContext.create(level), chunkPos, circleData);
    }

    @SubscribeEvent
    public static void onChunkDataSave(ChunkDataEvent.Save event) {
        if (!DTConfigs.SERVER.worldGen.get()) return;

        final LevelContext levelContext = LevelContext.create(event.getLevel());
        final UniversalPoissonDiscProvider discProvider = DynamicTreeFeature.DISC_PROVIDER;
        final ChunkAccess chunk = event.getChunk();
        final ChunkPos chunkPos = chunk.getPos();

        final byte[] circleData = discProvider.getChunkPoissonData(levelContext, chunkPos);
        event.getData().putByteArray(UniversalPoissonDiscProvider.CIRCLE_DATA_ID, circleData); // Set circle data.

		if (chunk instanceof LevelChunk && !((LevelChunk) chunk).loaded) {
			discProvider.unloadChunkPoissonData(levelContext, chunkPos);
		}
    }

    ///////////////////////////////////////////
    // SERVER
    ///////////////////////////////////////////

    @SubscribeEvent
    public static void onServerStart(final ServerStartingEvent event) {
        SeasonCompatibilityHandler.getSeasonManager().flushMappings();
    }

    @SubscribeEvent
    public static void registerCommands(final RegisterCommandsEvent event) {
        new DTCommand().registerDTCommand(event.getDispatcher());
    }

    ///////////////////////////////////////////
    // RESOURCES
    ///////////////////////////////////////////

    @SubscribeEvent
    public static void addReloadListeners(final AddReloadListenerEvent event) {
        event.addListener(new Resources.ReloadListener(event.getServerResources().getRecipeManager()));
    }

}