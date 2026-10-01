package com.dtteam.dynamictrees.worldgen.structure;

import com.dtteam.dynamictrees.DynamicTrees;
import com.dtteam.dynamictrees.data.CustomBootstrapContext;import com.dtteam.dynamictrees.tree.species.Species;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;import net.minecraft.core.RegistryAccess;import net.minecraft.data.worldgen.PlainVillagePools;
import net.minecraft.data.worldgen.ProcessorLists;
import net.minecraft.world.level.levelgen.structure.pools.ListPoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool.Projection.RIGID;
import static net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool.Projection.TERRAIN_MATCHING;

//? if >= 1.21 {
import net.minecraft.data.worldgen.BootstrapContext;
//? } else if >= 1.19.4 {
/*import net.minecraft.data.worldgen.BootstapContext;
*///? }

//? if >= 1.19.4 {
import net.minecraft.core.registries.Registries;
//? }

/**
 * @author Harley O'Connor
 */
public final class VillageTreeReplacement {

    public static final Logger LOGGER = LogManager.getLogger();
    private static final String REPLACEMENT_TOWN_CENTER_ID = DynamicTrees.location("village/plains/town_centers/plains_meeting_point_3").toString();

    public static void replaceTreesFromVanillaVillages(
            //? if >= 1.19.4
             HolderLookup.Provider vanillaProvider,

            //? if >= 1.21 {
            BootstrapContext<StructureTemplatePool> context
            //? } else if >= 1.19.4 {
            /*BootstapContext<StructureTemplatePool> context
            *///? } else {
            /*CustomBootstrapContext<StructureTemplatePool> context
            *///? }
    ) {
        // Replace Oak tree in Plains village town center.
        //? if >= 1.19.4 {
        HolderLookup.RegistryLookup<StructureProcessorList> processorLists = vanillaProvider.lookupOrThrow(Registries.PROCESSOR_LIST);
        //? } else {
        /*HolderLookup.RegistryLookup<StructureProcessorList> processorLists = new HolderLookup.RegistryLookup<>(
                RegistryAccess.BUILTIN.get().registryOrThrow(Registry.PROCESSOR_LIST_REGISTRY)
        );
        *///? }
        
        final TreePoolElement townCenterTreePattern = new TreePoolElement(Species.REGISTRY.get(DynamicTrees.OAK), new BlockPos(5, 1, 5) /*new BlockPos(0, 1, 0)*/, RIGID);
        RegularTemplatePoolModifier.create(
                //? if >= 1.19.4 {
                vanillaProvider,
                PlainVillagePools.START
                //? } else {
                /*PlainVillagePools.START.unwrapKey().orElseThrow()
                *///? }
        ).replaceTemplate(3,
                new ListPoolElement(ImmutableList.of(
                        //? if >= 1.19.4 {
                        StructurePoolElement.legacy(REPLACEMENT_TOWN_CENTER_ID, processorLists.getOrThrow(ProcessorLists.MOSSIFY_70_PERCENT)).apply(RIGID),
                        //? } else {
                        /*StructurePoolElement.legacy(REPLACEMENT_TOWN_CENTER_ID, processorLists.get(ProcessorLists.MOSSIFY_70_PERCENT.unwrapKey().orElseThrow()).orElseThrow()).apply(RIGID),
                        *///? }

                        townCenterTreePattern
                ), RIGID)
        ).replaceTemplate(7,
                new ListPoolElement(ImmutableList.of(
                        //? if >= 1.19.4 {
                        StructurePoolElement.legacy(REPLACEMENT_TOWN_CENTER_ID, processorLists.getOrThrow(ProcessorLists.ZOMBIE_PLAINS)).apply(RIGID),
                         //? } else {
                        /*StructurePoolElement.legacy(REPLACEMENT_TOWN_CENTER_ID, processorLists.get(ProcessorLists.ZOMBIE_PLAINS.unwrapKey().orElseThrow()).orElseThrow()).apply(RIGID),
                        *///? }
                        
                        townCenterTreePattern
                ), RIGID)
        ).registerPool(context);

        //~ if < 1.19.4 'RegularTemplatePoolModifier.village(vanillaProvider, ' -> 'RegularTemplatePoolModifier.village(' {
        // Replace Oak trees from Plains village.
        final TreePoolElement oakTreePattern = new TreePoolElement(Species.REGISTRY.get(DynamicTrees.OAK), TERRAIN_MATCHING);
        RegularTemplatePoolModifier.village(vanillaProvider, "plains", "trees").replaceTemplate(0, oakTreePattern).registerPool(context);
        RegularTemplatePoolModifier.village(vanillaProvider, "plains", "decor").replaceTemplate(1, oakTreePattern).registerPool(context);
        RegularTemplatePoolModifier.village(vanillaProvider, "plains", "zombie/decor").replaceTemplate(1, oakTreePattern).registerPool(context);

        // Replace Acacia trees from Savanna village.
        final TreePoolElement acaciaTreePattern = new TreePoolElement(Species.REGISTRY.get(DynamicTrees.ACACIA), TERRAIN_MATCHING);
        RegularTemplatePoolModifier.village(vanillaProvider, "savanna", "trees").replaceTemplate(0, acaciaTreePattern).registerPool(context);
        RegularTemplatePoolModifier.village(vanillaProvider, "savanna", "decor").replaceTemplate(1, acaciaTreePattern).registerPool(context);
        RegularTemplatePoolModifier.village(vanillaProvider, "savanna", "zombie/decor").replaceTemplate(1, acaciaTreePattern).registerPool(context);

        // Replace Spruce trees from Snowy village.
        final TreePoolElement spruceTreePattern = new TreePoolElement(Species.REGISTRY.get(DynamicTrees.SPRUCE), TERRAIN_MATCHING);
        RegularTemplatePoolModifier.village(vanillaProvider, "snowy", "trees").replaceTemplate(0, spruceTreePattern).registerPool(context);
        RegularTemplatePoolModifier.village(vanillaProvider, "snowy", "decor").replaceTemplate(3, spruceTreePattern).registerPool(context);
        RegularTemplatePoolModifier.village(vanillaProvider, "snowy", "zombie/decor").replaceTemplate(3, spruceTreePattern).registerPool(context);

        // Replace Spruce and Pine trees from Taiga village.
        RegularTemplatePoolModifier.village(vanillaProvider, "taiga", "decor")
                .replaceTemplate(7, spruceTreePattern)
                .replaceTemplate(8, spruceTreePattern)
                .registerPool(context);
        RegularTemplatePoolModifier.village(vanillaProvider, "taiga", "zombie/decor")
                .replaceTemplate(4, spruceTreePattern)
                .replaceTemplate(5, spruceTreePattern)
                .registerPool(context);
        //~ }
    }

}
