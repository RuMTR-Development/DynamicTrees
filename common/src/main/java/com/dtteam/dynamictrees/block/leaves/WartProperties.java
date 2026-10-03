package com.dtteam.dynamictrees.block.leaves;

import com.dtteam.dynamictrees.api.registry.TypedRegistry;
import com.dtteam.dynamictrees.data.DTLootTableBuilder;
import com.dtteam.dynamictrees.data.tags.DTBlockTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.Collections;
import java.util.List;

//? if >= 1.21.1 {
import net.minecraft.world.level.material.MapColor;
 //? } else {
/*import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.material.Material;
*///? }

//? if >= 1.19.4 {
import net.minecraft.core.HolderLookup;
//? }

/**
 * @author Harley O'Connor
 */
public class WartProperties extends SolidLeavesProperties {

    public static final TypedRegistry.EntryType<LeavesProperties> TYPE = TypedRegistry.newType(WartProperties::new);

    public WartProperties(final ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected String getBlockRegistryNameSuffix() {
        return "_wart";
    }

    @Override
    public BlockBehaviour.Properties getDefaultBlockProperties() {
        //? if >= 1.21.1 {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_RED)
        //? } else {
        /*return BlockBehaviour.Properties.of(Material.PLANT, MaterialColor.COLOR_RED)
        *///? }
                .strength(1.0F)
                .sound(SoundType.WART_BLOCK)
                .randomTicks();
    }

    @Override
    public List<TagKey<Block>> defaultLeavesTags() {
        return Collections.singletonList(DTBlockTags.WART_BLOCKS);
    }

    @Override
    public LootTable.Builder createBlockDrops(
            //? if >= 1.19.4
             HolderLookup.Provider registries
    ) {
        return DTLootTableBuilder.createWartBlockDrops(
                primitiveLeaves.getBlock()

                //? if >= 1.19.4 {
                ,
                registries
                //? }
        );
    }

    @Override
    public LootTable.Builder createDrops(
            //? if >= 1.19.4
             HolderLookup.Provider registries
    ) {
        return DTLootTableBuilder.createWartDrops(
                primitiveLeaves.getBlock()

                //? if >= 1.19.4 {
                ,
                registries
                //? }
        );
    }

}
