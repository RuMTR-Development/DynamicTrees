package com.dtteam.dynamictrees.data.provider;

import com.dtteam.dynamictrees.DynamicTrees;
import com.dtteam.dynamictrees.data.CustomBootstrapContext;
import com.dtteam.dynamictrees.registry.DTRegistries;
import com.dtteam.dynamictrees.tree.species.Species;
import com.dtteam.dynamictrees.worldgen.feature.CaveRootedTreePlacement;
import com.dtteam.dynamictrees.worldgen.feature.DTReplaceNyliumFungiBlockStateProvider;
import com.dtteam.dynamictrees.worldgen.structure.VillageTreeReplacement;
import com.mojang.serialization.JsonOps;import net.minecraft.core.*;
import net.minecraft.data.DataGenerator;import net.minecraft.data.worldgen.features.NetherFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.RegistryOps;import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NetherForestVegetationConfig;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.EnvironmentScanPlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RandomOffsetPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

//? if >= 1.19.4 {
/*import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstapContext;
*///? } else {
import net.minecraft.data.BuiltinRegistries;
//? }

//? if >= 1.19.2 {
/*import net.minecraftforge.common.data.JsonCodecProvider;
*///? }

public class DTDatapackBuiltinEntriesProvider
        //? if >= 1.19.4
        //extends DatapackBuiltinEntriesProvider
{
    //? if >= 1.19.4 {
    /*public DTDatapackBuiltinEntriesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, Set<String> modIds) {
        super(output, registries.thenApply(p -> constructRegistries(p, getBuilder(p))), modIds);
    }

    @SuppressWarnings({"unchecked", "UnstableApiUsage"})
    private static HolderLookup.Provider constructRegistries(HolderLookup.Provider original, RegistrySetBuilder datapackEntriesBuilder) {
        try {
//            // We don't need SRG mappings; this is for in-dev datagen only
//            Field ownerField = ObfuscationReflectionHelper.findField(Holder.Reference.class, "owner");
//            Object holderOwner = ownerField.get(original.lookupOrThrow(Registries.BIOME).getOrThrow(Biomes.BADLANDS));
//            Class<?> universalOwnerClass = Class.forName("net.minecraft.core.RegistrySetBuilder$UniversalOwner");
//            Field ownersField = ObfuscationReflectionHelper.findField(universalOwnerClass, "owners");
//            Set<HolderOwner<?>> owners = (Set<HolderOwner<?>>) ownersField.get(holderOwner);
//            var builderKeys = new HashSet<>(datapackEntriesBuilder.getEntryKeys());
//            DataPackRegistriesHooks.getDataPackRegistriesWithDimensions().filter(data -> !builderKeys.contains(data.key())).forEach(data -> datapackEntriesBuilder.add(data.key(), context -> {}));
            HolderLookup.Provider provider = datapackEntriesBuilder.buildPatch(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY), original); //Where to get the cloner factory?
//            Object newHolderOwner = ownerField.get(provider.full().lookupOrThrow(Registries.CONFIGURED_FEATURE).getOrThrow(DTRegistries.DYNAMIC_TREE_CONFIGURED_FEATURE));
//            owners.addAll((Set<HolderOwner<?>>) ownersField.get(newHolderOwner));
            return provider;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static RegistrySetBuilder getBuilder(HolderLookup.Provider vanillaProvider) {
        return new RegistrySetBuilder()
                .add(Registries.TEMPLATE_POOL, context -> bootstrapTemplatePools(vanillaProvider, context))
                .add(Registries.CONFIGURED_FEATURE, context -> bootstrapConfiguredFeatures(vanillaProvider, context))
                .add(Registries.PLACED_FEATURE, DTDatapackBuiltinEntriesProvider::bootstrapPlacedFeatures);
    }
    *///? } else {
    public static void registerProviders(DataGenerator generator, ExistingFileHelper fileHelper, String modId, boolean run) {
        Map<ResourceLocation, StructureTemplatePool> templatePools = new HashMap<>();
        Map<ResourceLocation, ConfiguredFeature<?, ?>> configuredFeatures = new HashMap<>();
        Map<ResourceLocation, PlacedFeature> placedFeatures = new HashMap<>();

        bootstrapTemplatePools((key, value) -> templatePools.put(key.location(), value));
        bootstrapConfiguredFeatures((key, value) -> configuredFeatures.put(key.location(), value));
        bootstrapPlacedFeatures((key, value) -> placedFeatures.put(key.location(), value));

        //? if < 1.19.2 {
        if (!run) {
            return;
        }
        //? }

        generator.addProvider(
                //? if >= 1.19.2
                //run,

                //~ if < 1.19.2 'JsonCodecProvider' -> 'CustomJsonCodecProvider'
                CustomJsonCodecProvider.forDatapackRegistry(
                    generator,
                    fileHelper,
                    modId,
                    RegistryOps.create(JsonOps.INSTANCE, BuiltinRegistries.ACCESS),
                    Registry.TEMPLATE_POOL_REGISTRY,
                    templatePools
                )
        );

        generator.addProvider(
                //? if >= 1.19.2
                //run,

                //~ if < 1.19.2 'JsonCodecProvider' -> 'CustomJsonCodecProvider'
                CustomJsonCodecProvider.forDatapackRegistry(
                    generator,
                    fileHelper,
                    modId,
                    RegistryOps.create(JsonOps.INSTANCE, BuiltinRegistries.ACCESS),
                    Registry.CONFIGURED_FEATURE_REGISTRY,
                    configuredFeatures
                )
        );

        generator.addProvider(
                //? if >= 1.19.2
                //run,

                //~ if < 1.19.2 'JsonCodecProvider' -> 'CustomJsonCodecProvider'
                CustomJsonCodecProvider.forDatapackRegistry(
                    generator,
                    fileHelper,
                    modId,
                    RegistryOps.create(JsonOps.INSTANCE, BuiltinRegistries.ACCESS),
                    Registry.PLACED_FEATURE_REGISTRY,
                    placedFeatures
            )
        );
    }
    //? }

    private static void bootstrapTemplatePools(
            //? if >= 1.19.4
            //HolderLookup.Provider vanillaProvider,

            //~ if < 1.19.4 'BootstapContext' -> 'CustomBootstrapContext'
            CustomBootstrapContext<StructureTemplatePool> context
    ) {
        // TODO 1.20: Verify this works
        VillageTreeReplacement.replaceTreesFromVanillaVillages(
                //? if >= 1.19.4
                //vanillaProvider,

                context
        );
    }

    private static void bootstrapConfiguredFeatures(
            //? if >= 1.19.4
            //HolderLookup.Provider vanillaProvider,

            //~ if < 1.19.4 'BootstapContext' -> 'CustomBootstrapContext'
            CustomBootstrapContext<ConfiguredFeature<?, ?>> context
    ) {
        context.register(DTRegistries.DYNAMIC_TREE_CONFIGURED_FEATURE,
                new ConfiguredFeature<>(DTRegistries.DYNAMIC_TREE_FEATURE.get(), NoneFeatureConfiguration.INSTANCE));
        context.register(DTRegistries.CAVE_ROOTED_TREE_CONFIGURED_FEATURE,
                new ConfiguredFeature<>(DTRegistries.CAVE_ROOTED_TREE_FEATURE.get(), NoneFeatureConfiguration.INSTANCE));

        // TODO 1.20: Verify this works
        replaceNyliumFungiFeatures(
                //? if >= 1.19.4
                //vanillaProvider,

                context
        );
    }

    //~ if < 1.19.4 'BootstapContext' -> 'CustomBootstrapContext'
    private static void bootstrapPlacedFeatures(CustomBootstrapContext<PlacedFeature> context) {
        //? if >= 1.19.4 {
        /*var configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        *///? } else {
        var configuredFeatures = BuiltinRegistries.ACCESS.registryOrThrow(Registry.CONFIGURED_FEATURE_REGISTRY);

        //? }

        context.register(DTRegistries.DYNAMIC_TREE_PLACED_FEATURE, new PlacedFeature(
                //? if >= 1.19.4 {
                /*configuredFeatures.getOrThrow(DTRegistries.DYNAMIC_TREE_CONFIGURED_FEATURE),
                *///? } else {
                configuredFeatures.getHolderOrThrow(DTRegistries.DYNAMIC_TREE_CONFIGURED_FEATURE),
                //? }

                List.of()
        ));

        context.register(DTRegistries.CAVE_ROOTED_TREE_PLACED_FEATURE,
                new PlacedFeature(
                        //? if >= 1.19.4 {
                        /*configuredFeatures.getOrThrow(DTRegistries.DYNAMIC_TREE_CONFIGURED_FEATURE),
                        *///? } else {
                        configuredFeatures.getHolderOrThrow(DTRegistries.DYNAMIC_TREE_CONFIGURED_FEATURE),
                        //? }

                        List.of(
                                CaveRootedTreePlacement.INSTANCE, PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                                EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12),
                                RandomOffsetPlacement.vertical(ConstantInt.of(-1)), BiomeFilter.biome()))
        );
    }

    private static void replaceNyliumFungiFeatures(
            //? if >= 1.19.4
            //HolderLookup.Provider vanillaProvider,

            //~ if < 1.19.4 'BootstapContext' -> 'CustomBootstrapContext'
            CustomBootstrapContext<ConfiguredFeature<?, ?>> context
    ) {
        Species.findSpecies(DynamicTrees.CRIMSON).getSapling().ifPresent(crimsonSapling ->
                Species.findSpecies(DynamicTrees.WARPED).getSapling().ifPresent(warpedSapling -> {
                    //? if >= 1.19.4 {
                    /*var configuredFeatures = vanillaProvider.lookup(Registries.CONFIGURED_FEATURE).orElseThrow();
                    *///? } else {
                    var configuredFeatures = BuiltinRegistries.ACCESS.registryOrThrow(Registry.CONFIGURED_FEATURE_REGISTRY);
                    //? }

                    List.of(NetherFeatures.CRIMSON_FOREST_VEGETATION, NetherFeatures.CRIMSON_FOREST_VEGETATION_BONEMEAL,
                                    NetherFeatures.WARPED_FOREST_VEGETION, NetherFeatures.WARPED_FOREST_VEGETATION_BONEMEAL)
                            .forEach(key -> replaceFeature(
                                    context,
                                    configuredFeatures,

                                    //? if >= 1.19.4 {
                                    /*key,
                                    *///? } else {
                                    (ResourceKey) key.unwrapKey().orElseThrow(),
                                    //? }

                                    crimsonSapling,
                                    warpedSapling
                            ));
                })
        );
    }

    private static void replaceFeature(
            //~ if < 1.19.4 'BootstapContext' -> 'CustomBootstrapContext'
            CustomBootstrapContext<ConfiguredFeature<?, ?>> context,

            //~ if < 1.19.4 'HolderLookup.RegistryLookup' -> 'Registry'
            Registry<ConfiguredFeature<?, ?>> configuredFeatures,

            ResourceKey<ConfiguredFeature<?, ?>> key,
            Block crimsonSapling,
            Block warpedSapling
    ) {
        //? if >= 1.19.4 {
        /*var feature = configuredFeatures.getOrThrow(key).value();
        *///? } else {
        var feature = configuredFeatures.get(key);
        //? }

        var config = (NetherForestVegetationConfig) feature.config();
        var stateProvider = (WeightedStateProvider) config.stateProvider;

        var newConfig = new NetherForestVegetationConfig(replaceBlockStates(stateProvider, crimsonSapling, warpedSapling), config.spreadWidth, config.spreadHeight);
        context.register(key, new ConfiguredFeature<>(Feature.NETHER_FOREST_VEGETATION, newConfig));
    }

    private static BlockStateProvider replaceBlockStates(WeightedStateProvider stateProvider, Block crimsonSapling, Block warpedSapling) {
        var listBuilder = SimpleWeightedRandomList.<BlockState>builder();

        for (var entry : stateProvider.weightedList.unwrap()) {
            BlockState blockState = entry.getData();
            if (blockState.is(Blocks.CRIMSON_FUNGUS)) {
                blockState = crimsonSapling.defaultBlockState();
            } else if (blockState.is(Blocks.WARPED_FUNGUS)) {
                blockState = warpedSapling.defaultBlockState();
            }

            listBuilder.add(blockState, entry.getWeight().asInt());
        }

        return new DTReplaceNyliumFungiBlockStateProvider(new WeightedStateProvider(listBuilder), stateProvider);
    }
}
