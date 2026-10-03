package com.dtteam.dynamictrees;

import com.dtteam.dynamictrees.block.leaves.LeavesProperties;
import com.dtteam.dynamictrees.block.soil.SoilProperties;
import com.dtteam.dynamictrees.client.BlockColorMultipliers;
import com.dtteam.dynamictrees.config.*;
import com.dtteam.dynamictrees.data.GatherDataHelper;
import com.dtteam.dynamictrees.data.generator.DTExtraLangGenerator;
import com.dtteam.dynamictrees.data.generator.DataGenerators;
import com.dtteam.dynamictrees.data.provider.DTDatapackBuiltinEntriesProvider;
import com.dtteam.dynamictrees.event.handler.OptionalHandlers;
import com.dtteam.dynamictrees.recipe.DendroPotionRecipeHandler;
import com.dtteam.dynamictrees.registry.ForgeRegistryHandler;
import com.dtteam.dynamictrees.registry.ForgeRegistryLoader;
import com.dtteam.dynamictrees.tree.family.Family;
import com.dtteam.dynamictrees.tree.species.Species;
import com.dtteam.dynamictrees.treepack.Resources;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.Set;

//? if >= 1.19.4 {
/*import com.dtteam.dynamictrees.client.ThickBranchRingsSource;
import net.minecraft.client.renderer.texture.atlas.SpriteSources;
*///? }

//? if >= 1.19.2 {
/*import net.minecraftforge.data.event.GatherDataEvent;
 *///? } else {
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.ModLoadingContext;
//? }

@Mod(DynamicTrees.MOD_ID)
public class DynamicTreesForge {

    public DynamicTreesForge() {
        FMLJavaModLoadingContext context = FMLJavaModLoadingContext.get();
        IEventBus eventBus = context.getModEventBus();

        eventBus.addListener(this::clientSetup);
        eventBus.addListener(this::onCommonSetup);
        eventBus.addListener(this::gatherData);

        //? if < 1.19.2
        ModLoadingContext configContext = ModLoadingContext.get();

        //~ if < 1.19.2 'context.' -> 'configContext.' {
        configContext.registerConfig(ModConfig.Type.SERVER, DTConfigs.SERVER_CONFIG);
        configContext.registerConfig(ModConfig.Type.COMMON, DTConfigs.COMMON_CONFIG);
        configContext.registerConfig(ModConfig.Type.CLIENT, DTConfigs.CLIENT_CONFIG);
        //~ }

        ForgeRegistryHandler.setup(DynamicTrees.MOD_ID, eventBus);

        DynamicTrees.init();

        ForgeRegistryLoader.setup(eventBus);

        OptionalHandlers.registerHandlers();

        DataGenerators.register();
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        LeavesProperties.postInitClient();
        BlockColorMultipliers.cleanUp();

        //? if >= 1.19.4
        //SpriteSources.register(ThickBranchRingsSource.ID.toString(), ThickBranchRingsSource.setType(ThickBranchRingsSource.CODEC).codec());
    }

    private void onCommonSetup(final FMLCommonSetupEvent event) {
        DynamicTrees.commonSetup();

        DendroPotionRecipeHandler.getAllDendroRecipes().forEach(BrewingRecipeRegistry::addRecipe);
    }

    private void gatherData(final GatherDataEvent event) {
        //Generate the tree block and item data
        Resources.MANAGER.gatherData();
        GatherDataHelper.gatherAllData(
                DynamicTrees.MOD_ID, event,
                new DTExtraLangGenerator(),
                SoilProperties.REGISTRY,
                Family.REGISTRY,
                Species.REGISTRY,
                LeavesProperties.REGISTRY
        );
        //Generate the feature replacement data
        //? if >= 1.19.4 {
        /*DataGenerator dataGen = event.getGenerator();
        dataGen.addProvider(event.includeServer(), new DTDatapackBuiltinEntriesProvider(
                dataGen.getPackOutput(), event.getLookupProvider(), Set.of(DynamicTrees.MOD_ID, DynamicTrees.MINECRAFT)
        ));
        *///? } else {
        DTDatapackBuiltinEntriesProvider.registerProviders(event.getGenerator(), event.getExistingFileHelper(), DynamicTrees.MOD_ID, event.includeServer());
        //? }
    }

}