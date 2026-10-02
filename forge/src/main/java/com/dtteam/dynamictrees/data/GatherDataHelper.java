package com.dtteam.dynamictrees.data;

import com.dtteam.dynamictrees.api.registry.Registry;
import com.dtteam.dynamictrees.data.provider.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

//? if >= 1.19.4 {
/*import net.minecraft.data.PackOutput;
*///? }

/**
 * @author Harley O'Connor
 */
public final class GatherDataHelper {
    private static final Map<String, Generator<DTDataProvider.Language, String>> extraLangGenerators = new HashMap<>();

    public static void gatherAllData(final String modId, final GatherDataEvent event, Registry<?>... registries) {
        gatherTagData(modId, event);
        gatherBlockStateAndModelData(modId, event, registries);
        gatherItemModelData(modId, event, registries);
        gatherLootData(modId, event);
        gatherLangData(modId, event, registries);
    }

    public static void gatherAllData(final String modId, final GatherDataEvent event, Generator<DTDataProvider.Language, String> generator, Registry<?>... registries) {
        addLangGenerator(modId, generator);
        gatherAllData(modId, event, registries);
    }

    public static void gatherTagData(final String modId, final GatherDataEvent event) {
        final DataGenerator generator = event.getGenerator();

        //? if >= 1.19.4 {
        /*PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        *///? }

        //? if >= 1.19.4 {
        /*final DTBlockTagsProvider blockTagsProvider = new DTBlockTagsProvider(packOutput, modId, lookupProvider, event.getExistingFileHelper());
        final DTItemTagsProvider itemTagsProvider = new DTItemTagsProvider(packOutput, modId, lookupProvider, blockTagsProvider.contentsGetter(), event.getExistingFileHelper());
        *///? } else {
        final DTBlockTagsProvider blockTagsProvider = new DTBlockTagsProvider(generator, modId, event.getExistingFileHelper());
        final DTItemTagsProvider itemTagsProvider = new DTItemTagsProvider(generator, blockTagsProvider, modId, event.getExistingFileHelper());
        //? }

        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeServer(), itemTagsProvider);
    }

    public static void gatherBlockStateAndModelData(final String modId, final GatherDataEvent event, Registry<?>... registries) {
        //? if >= 1.19.4 {
        /*event.getGenerator().addProvider(event.includeServer(), new DTSpriteSourceProvider(event.getGenerator().getPackOutput(),
                event.getLookupProvider(), modId, event.getExistingFileHelper(), registries));
        *///? }

        event.getGenerator().addProvider(event.includeServer(), new DTBlockStateProvider(
                //? if >= 1.19.4 {
                /*event.getGenerator().getPackOutput(),
                *///? } else {
                event.getGenerator(),
                //? }

                modId,
                event.getExistingFileHelper(),
                Arrays.asList(registries)
        ));
    }

    public static void gatherItemModelData(final String modId, final GatherDataEvent event, Registry<?>... registries) {
        event.getGenerator().addProvider(event.includeServer(), new DTItemModelProvider(
                //? if >= 1.19.4 {
                /*event.getGenerator().getPackOutput(),
                 *///? } else {
                event.getGenerator(),
                //? }

                modId,
                event.getExistingFileHelper(),
                Arrays.asList(registries)
        ));
    }

    public static void gatherLootData(final String modId, final GatherDataEvent event) {
        event.getGenerator().addProvider(event.includeServer(), new DTLootTableProvider(
                //? if >= 1.19.4 {
                /*event.getGenerator().getPackOutput(),
                 *///? } else {
                event.getGenerator(),
                //? }

                modId,
                event.getExistingFileHelper()
        ));
    }
    public static void gatherLangData(final String modId, final GatherDataEvent event, Registry<?>... registries){
        event.getGenerator().addProvider(event.includeClient(), new DTLangProvider(
                //? if >= 1.19.4 {
                /*event.getGenerator().getPackOutput(),
                 *///? } else {
                event.getGenerator(),
                //? }

                modId,
                Arrays.asList(registries)
        ));
    }

    public static void addLangGenerator(String modId, Generator<DTDataProvider.Language, String> generator) {
        GatherDataHelper.extraLangGenerators.put(modId,generator);
    }
    public static Map<String,Generator<DTDataProvider.Language, String>> getExtraLangGenerators() {
        return GatherDataHelper.extraLangGenerators;
    }
}
