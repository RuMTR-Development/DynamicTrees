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
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

//? if >= 1.19.4 {
/*import net.minecraft.core.registries.Registries;
*///? }

public class RunFeatureCancellersBiomeModifier implements BiomeModifier {
    //~ if < 1.19.4 'Registries.PLACED_FEATURE' -> 'Registry.PLACED_FEATURE_REGISTRY'
    public static final TagKey<PlacedFeature> FEATURE_CANCELLER_EXCLUSIONS_KEY = TagKey.create(Registry.PLACED_FEATURE_REGISTRY,
            DynamicTrees.location("feature_canceller_exclusions"));

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.REMOVE && DTConfigs.SERVER.worldGen.get()) {
            ResourceKey<Biome> biomeKey = biome.unwrapKey().orElseThrow();
            BiomeGenerationSettingsBuilder generationSettings = builder.getGenerationSettings();

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
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return ForgeRegistryLoader.RUN_FEATURE_CANCELLERS_BIOME_MODIFIER.get();
    }
}