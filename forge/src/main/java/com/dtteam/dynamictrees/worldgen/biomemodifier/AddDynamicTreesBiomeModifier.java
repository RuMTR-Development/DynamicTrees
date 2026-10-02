package com.dtteam.dynamictrees.worldgen.biomemodifier;

import com.dtteam.dynamictrees.config.DTConfigs;
import com.dtteam.dynamictrees.registry.DTRegistries;
import com.dtteam.dynamictrees.registry.ForgeRegistryLoader;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.server.ServerLifecycleHooks;

//? if >= 1.19.4 {
/*import net.minecraft.core.registries.Registries;
*///? }

public class AddDynamicTreesBiomeModifier implements BiomeModifier {

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.ADD && DTConfigs.SERVER.worldGen.get()) {
            BiomeGenerationSettingsBuilder generationSettings = builder.getGenerationSettings();
            //~ if < 1.19.4 'Registries.PLACED_FEATURE' -> 'Registry.PLACED_FEATURE_REGISTRY'
            var placedFeatures = ServerLifecycleHooks.getCurrentServer().registryAccess().registryOrThrow(Registry.PLACED_FEATURE_REGISTRY);
            generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placedFeatures.getHolderOrThrow(DTRegistries.CAVE_ROOTED_TREE_PLACED_FEATURE));
            generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placedFeatures.getHolderOrThrow(DTRegistries.DYNAMIC_TREE_PLACED_FEATURE));
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return ForgeRegistryLoader.ADD_DYNAMIC_TREES_BIOME_MODIFIER.get();
    }

}