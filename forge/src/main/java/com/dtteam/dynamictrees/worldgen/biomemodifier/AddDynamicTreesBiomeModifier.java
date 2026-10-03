package com.dtteam.dynamictrees.worldgen.biomemodifier;

import com.dtteam.dynamictrees.DynamicTrees;
import com.dtteam.dynamictrees.config.DTConfigs;
import com.dtteam.dynamictrees.registry.DTRegistries;
import com.dtteam.dynamictrees.registry.ForgeRegistryLoader;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

//? if >= 1.19.4 {
/*import net.minecraft.core.registries.Registries;
*///? }

//? if >= 1.19.2 {
/*import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
*///? }

//? if < 1.19.2
@Mod.EventBusSubscriber(modid = DynamicTrees.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AddDynamicTreesBiomeModifier
        //? if >= 1.19.2
        //implements BiomeModifier
{
    private static void apply(BiomeGenerationSettingsBuilder generationSettings) {
        if (!DTConfigs.SERVER.worldGen.get()) {
            return;
        }

        //? if >= 1.19.2 {
        /*//~ if < 1.19.4 'Registries.PLACED_FEATURE' -> 'Registry.PLACED_FEATURE_REGISTRY'
        var placedFeatures = ServerLifecycleHooks.getCurrentServer().registryAccess().registryOrThrow(Registry.PLACED_FEATURE_REGISTRY);
        generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placedFeatures.getHolderOrThrow(DTRegistries.CAVE_ROOTED_TREE_PLACED_FEATURE));
        generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placedFeatures.getHolderOrThrow(DTRegistries.DYNAMIC_TREE_PLACED_FEATURE));
        *///? } else {
        generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ForgeRegistryLoader.DYNAMIC_TREE_PLACED_FEATURE.getHolder().orElseThrow());
        generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ForgeRegistryLoader.CAVE_SURFACE_TREE_PLACED_FEATURE.getHolder().orElseThrow());
        //? }
    }

    //? if >= 1.19.2 {
    /*@Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase != Phase.ADD) {
            return;
        }

        apply(builder.getGenerationSettings());
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return ForgeRegistryLoader.ADD_DYNAMIC_TREES_BIOME_MODIFIER.get();
    }
    *///? } else {
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onBiomeLoading(BiomeLoadingEvent event) {
        apply(event.getGeneration());
    }
    //? }
}