package com.dtteam.dynamictrees.worldgen.biomemodifier;

import com.dtteam.dynamictrees.config.DTConfigs;
import com.dtteam.dynamictrees.registry.DTRegistries;
import com.dtteam.dynamictrees.registry.ForgeRegistryLoader;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.server.ServerLifecycleHooks;

public class AddDynamicTreesBiomeModifier implements BiomeModifier {

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.ADD && DTConfigs.SERVER.worldGen.get()) {
            BiomeGenerationSettingsBuilder generationSettings = builder.getGenerationSettings();
            var placedFeatures = ServerLifecycleHooks.getCurrentServer().registryAccess().registryOrThrow(Registries.PLACED_FEATURE);
            generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placedFeatures.getHolderOrThrow(DTRegistries.CAVE_ROOTED_TREE_PLACED_FEATURE));
            generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placedFeatures.getHolderOrThrow(DTRegistries.DYNAMIC_TREE_PLACED_FEATURE));
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return ForgeRegistryLoader.ADD_DYNAMIC_TREES_BIOME_MODIFIER.get();
    }

}