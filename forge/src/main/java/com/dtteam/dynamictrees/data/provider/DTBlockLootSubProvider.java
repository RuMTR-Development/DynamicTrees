package com.dtteam.dynamictrees.data.provider;

import com.dtteam.dynamictrees.block.branch.BranchBlock;
import com.dtteam.dynamictrees.block.fruit.Fruit;
import com.dtteam.dynamictrees.block.leaves.LeavesProperties;
import com.dtteam.dynamictrees.block.pod.Pod;
import com.dtteam.dynamictrees.event.DataGenerationStreamEvent;
import com.dtteam.dynamictrees.tree.species.Species;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.StreamSupport;

//? if >= 1.19.4 {
/*import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
*///? } else {
import net.minecraft.data.loot.BlockLoot;
import net.minecraftforge.registries.ForgeRegistries;
//? }

//~ if < 1.19.4 'extends BlockLootSubProvider' -> 'extends BlockLoot'
public class DTBlockLootSubProvider extends BlockLoot {
    private final String modId;
    private final ExistingFileHelper fileHelper;

    protected DTBlockLootSubProvider(String modId, ExistingFileHelper fileHelper) {
        //? if >= 1.19.4
        //super(Set.of(), FeatureFlagSet.of());

        this.modId = modId;
        this.fileHelper = fileHelper;
    }

    @Override

    //? if >= 1.19.4 {
    /*protected void generate()
    *///? } else {
    protected void addTables()
    //? }

    {
        Species.REGISTRY.dataGenerationStream(modId).forEach(this::addVoluntaryTable);

        //~ if < 1.19.4 'BuiltInRegistries.BLOCK.stream()' -> 'StreamSupport.stream(ForgeRegistries.BLOCKS.spliterator(), false)'
        StreamSupport.stream(ForgeRegistries.BLOCKS.spliterator(), false)
                .filter(block -> block instanceof BranchBlock)
                .map(block -> (BranchBlock) block)
                //~ if < 1.19.4 'BuiltInRegistries.BLOCK' -> 'ForgeRegistries.BLOCKS'
                .filter(block -> ForgeRegistries.BLOCKS.getKey(block).getNamespace().equals(modId))
                .forEach(this::addBranchTable);

        LeavesProperties.REGISTRY.dataGenerationStream(modId).forEach(leavesProperties -> {
            addLeavesBlockTable(leavesProperties);
            addLeavesTable(leavesProperties);
        });

        Fruit.REGISTRY.dataGenerationStream(modId).forEach(this::addFruitBlockTable);
        Pod.REGISTRY.dataGenerationStream(modId).forEach(this::addPodBlockTable);

        ModLoader.get().postEvent(new DataGenerationStreamEvent(this, modId, fileHelper, map));
    }

    @Override

    //? if >= 1.19.4 {
    /*public void generate(@NotNull BiConsumer<ResourceLocation, LootTable.Builder> output)
    *///? } else {
    public void accept(@NotNull BiConsumer<ResourceLocation, LootTable.Builder> output)
    //? }
    {
        //? if >= 1.19.4 {
        /*this.generate();
        *///? } else {
        this.addTables();
        //? }

        this.map.forEach(output);
    }

    private void addVoluntaryTable(Species species) {
        if (species.shouldGenerateVoluntaryDrops()) {
            final ResourceLocation leavesTablePath = species.getVoluntaryDropsPath();
            if (!fileHelper.exists(leavesTablePath, PackType.SERVER_DATA)) {
                this.map.put(leavesTablePath, species.createVoluntaryDrops(
                        //? if >= 1.19.4
                        //null
                ));
            }
        }
    }

    private void addBranchTable(BranchBlock branchBlock) {
        if (branchBlock.shouldGenerateBranchDrops()) {
            final ResourceLocation branchTablePath = branchBlock.getLootTableName();
            if (!fileHelper.exists(branchTablePath, PackType.SERVER_DATA)) {
                this.map.put(branchTablePath, branchBlock.createBranchDrops(
                        //? if >= 1.19.4
                        //null
                ));
            }
        }
    }

    private void addLeavesBlockTable(LeavesProperties leavesProperties) {
        if (leavesProperties.shouldGenerateBlockDrops()) {
            final ResourceLocation leavesBlockTablePath = leavesProperties.getBlockLootTableName();
            if (!fileHelper.exists(leavesBlockTablePath, PackType.SERVER_DATA)) {
                this.map.put(leavesBlockTablePath, leavesProperties.createBlockDrops(
                        //? if >= 1.19.4
                        //null
                ));
            }
        }
    }

    private void addLeavesTable(LeavesProperties leavesProperties) {
        if (leavesProperties.shouldGenerateDrops()) {
            final ResourceLocation leavesTablePath = leavesProperties.getLootTableName();
            if (!fileHelper.exists(leavesTablePath, PackType.SERVER_DATA)) {
                this.map.put(leavesTablePath, leavesProperties.createDrops(
                        //? if >= 1.19.4
                        //null
                ));
            }
        }
    }

    private void addFruitBlockTable(Fruit fruit) {
        if (fruit.shouldGenerateBlockDrops()) {
            final ResourceLocation fruitBlockTablePath = fruit.getBlockDropsPath();
            if (!fileHelper.exists(fruitBlockTablePath, PackType.SERVER_DATA)) {
                this.map.put(fruitBlockTablePath, fruit.createBlockDrops(
                        //? if >= 1.19.4
                        //null
                ));
            }
        }
    }

    private void addPodBlockTable(Pod pod) {
        if (pod.shouldGenerateBlockDrops()) {
            final ResourceLocation fruitBlockTablePath = pod.getBlockDropsPath();
            if (!fileHelper.exists(fruitBlockTablePath, PackType.SERVER_DATA)) {
                this.map.put(fruitBlockTablePath, pod.createBlockDrops(
                        //? if >= 1.19.4
                        //null
                ));
            }
        }
    }

}