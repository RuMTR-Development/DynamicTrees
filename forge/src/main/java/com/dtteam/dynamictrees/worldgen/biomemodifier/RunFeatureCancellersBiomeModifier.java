package com.dtteam.dynamictrees.worldgen.biomemodifier;

import com.dtteam.dynamictrees.DynamicTrees;
import com.dtteam.dynamictrees.api.worldgen.BiomePropertySelectors;
import com.dtteam.dynamictrees.api.worldgen.FeatureCanceller;
import com.dtteam.dynamictrees.config.DTConfigs;
import com.dtteam.dynamictrees.registry.ForgeRegistryLoader;
import com.dtteam.dynamictrees.worldgen.BiomeDatabase;
import com.dtteam.dynamictrees.worldgen.featurecancellation.FeatureCancellationRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.eventbus.api.EventPriority;import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;import net.minecraftforge.registries.ForgeRegistries;

//? if >= 1.19.4 {
/*import net.minecraft.core.registries.Registries;
*///? }

//? if >= 1.19.2 {
/*import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
*///? } else {
import net.minecraftforge.event.world.BiomeLoadingEvent;
//? }

//? if < 1.19.2
@Mod.EventBusSubscriber(modid = DynamicTrees.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RunFeatureCancellersBiomeModifier
        //? if >= 1.19.2
        //implements BiomeModifier
{
    //~ if < 1.19.4 'Registries.PLACED_FEATURE' -> 'Registry.PLACED_FEATURE_REGISTRY'
    public static final TagKey<PlacedFeature> FEATURE_CANCELLER_EXCLUSIONS_KEY = TagKey.create(Registry.PLACED_FEATURE_REGISTRY,
            DynamicTrees.location("feature_canceller_exclusions"));

    private static void apply(ResourceKey<Biome> biomeKey, BiomeGenerationSettingsBuilder generationSettings) {
        if (!DTConfigs.SERVER.worldGen.get()) {
            return;
        }

        BiomePropertySelectors.NormalFeatureCancellation featureCancellations = new BiomePropertySelectors.NormalFeatureCancellation();

        for (FeatureCancellationRegistry.Entry entry : FeatureCancellationRegistry.getCancellations()) {
            if (entry.biomes().containsKey(biomeKey)) {
                if (entry.operation() == BiomeDatabase.Operation.REPLACE)
                    featureCancellations.reset();
                featureCancellations.addFrom(entry.cancellations());
            }
        }

        featureCancellations.getDecorationSteps().forEach(stage -> generationSettings.getFeatures(stage).removeIf(placedFeatureHolder -> {
            // If you want a placed feature to be entirely excluded from cancellation by any feature cancellers,
            // add it to the dynamictrees:tags/worldgen/placed_feature/feature_canceller_exclusions tag.
            if (placedFeatureHolder.is(FEATURE_CANCELLER_EXCLUSIONS_KEY))
                return false;

            PlacedFeature placedFeature = placedFeatureHolder.value();

            return placedFeature.getFeatures().anyMatch(configuredFeature -> {
                for (FeatureCanceller featureCanceller : featureCancellations.getCancellers()) {
                    if (featureCanceller.shouldCancel(configuredFeature, featureCancellations)) {
                        return true;
                    }
                }

                return false;
            });
        }));
    }

    //? if >= 1.19.2 {
    /*@Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase != Phase.REMOVE) {
            return;
        }

        ResourceKey<Biome> biomeKey = biome.unwrapKey().orElseThrow();
        BiomeGenerationSettingsBuilder generationSettings = builder.getGenerationSettings();

        apply(biomeKey, generationSettings);
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return ForgeRegistryLoader.RUN_FEATURE_CANCELLERS_BIOME_MODIFIER.get();
    }
    *///? } else {
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onBiomeLoading(BiomeLoadingEvent event) {
        ResourceLocation biomeId = event.getName();

        if (biomeId == null) {
            return;
        }

        ResourceKey<Biome> biomeKey = ResourceKey.create(ForgeRegistries.Keys.BIOMES, biomeId);
        BiomeGenerationSettingsBuilder generationSettings = event.getGeneration();

        apply(biomeKey, generationSettings);
    }
    //? }
}