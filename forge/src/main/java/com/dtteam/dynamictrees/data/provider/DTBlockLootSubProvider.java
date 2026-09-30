package com.dtteam.dynamictrees.data.provider;

import com.dtteam.dynamictrees.block.branch.BranchBlock;
import com.dtteam.dynamictrees.block.fruit.Fruit;
import com.dtteam.dynamictrees.block.leaves.LeavesProperties;
import com.dtteam.dynamictrees.block.pod.Pod;
import com.dtteam.dynamictrees.event.DataGenerationStreamEvent;
import com.dtteam.dynamictrees.tree.species.Species;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.BiConsumer;

public class DTBlockLootSubProvider extends BlockLootSubProvider {
    private final String modId;
    private final ExistingFileHelper fileHelper;

    protected DTBlockLootSubProvider(String modId, ExistingFileHelper fileHelper) {
        super(Set.of(), FeatureFlagSet.of());

        this.modId = modId;
        this.fileHelper = fileHelper;
    }

    @Override
    protected void generate() {
        Species.REGISTRY.dataGenerationStream(modId).forEach(this::addVoluntaryTable);

        BuiltInRegistries.BLOCK.stream()
                .filter(block -> block instanceof BranchBlock)
                .map(block -> (BranchBlock) block)
                .filter(block -> BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals(modId))
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
    public void generate(@NotNull BiConsumer<ResourceLocation, LootTable.Builder> output) {
        this.generate();

        this.map.forEach(output);
    }

    private void addVoluntaryTable(Species species) {
        if (species.shouldGenerateVoluntaryDrops()) {
            final ResourceLocation leavesTablePath = species.getVoluntaryDropsPath();
            if (!fileHelper.exists(leavesTablePath, PackType.SERVER_DATA)) {
                this.map.put(leavesTablePath, species.createVoluntaryDrops(null));
            }
        }
    }

    private void addBranchTable(BranchBlock branchBlock) {
        if (branchBlock.shouldGenerateBranchDrops()) {
            final ResourceLocation branchTablePath = branchBlock.getLootTableName();
            if (!fileHelper.exists(branchTablePath, PackType.SERVER_DATA)) {
                this.map.put(branchTablePath, branchBlock.createBranchDrops(null));
            }
        }
    }

    private void addLeavesBlockTable(LeavesProperties leavesProperties) {
        if (leavesProperties.shouldGenerateBlockDrops()) {
            final ResourceLocation leavesBlockTablePath = leavesProperties.getBlockLootTableName();
            if (!fileHelper.exists(leavesBlockTablePath, PackType.SERVER_DATA)) {
                this.map.put(leavesBlockTablePath, leavesProperties.createBlockDrops(null));
            }
        }
    }

    private void addLeavesTable(LeavesProperties leavesProperties) {
        if (leavesProperties.shouldGenerateDrops()) {
            final ResourceLocation leavesTablePath = leavesProperties.getLootTableName();
            if (!fileHelper.exists(leavesTablePath, PackType.SERVER_DATA)) {
                this.map.put(leavesTablePath, leavesProperties.createDrops(null));
            }
        }
    }

    private void addFruitBlockTable(Fruit fruit) {
        if (fruit.shouldGenerateBlockDrops()) {
            final ResourceLocation fruitBlockTablePath = fruit.getBlockDropsPath();
            if (!fileHelper.exists(fruitBlockTablePath, PackType.SERVER_DATA)) {
                this.map.put(fruitBlockTablePath, fruit.createBlockDrops(null));
            }
        }
    }

    private void addPodBlockTable(Pod pod) {
        if (pod.shouldGenerateBlockDrops()) {
            final ResourceLocation fruitBlockTablePath = pod.getBlockDropsPath();
            if (!fileHelper.exists(fruitBlockTablePath, PackType.SERVER_DATA)) {
                this.map.put(fruitBlockTablePath, pod.createBlockDrops(null));
            }
        }
    }

}