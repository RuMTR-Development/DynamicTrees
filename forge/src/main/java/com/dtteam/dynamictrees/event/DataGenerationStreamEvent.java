package com.dtteam.dynamictrees.event;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Map;

//? if >= 1.19.4 {
import net.minecraft.data.loot.LootTableSubProvider;
//? } else {
/*import net.minecraft.data.loot.BlockLoot;
*///? }

public class DataGenerationStreamEvent extends Event implements IModBusEvent {

    private final ExistingFileHelper fileHelper;
    private final Map<ResourceLocation, LootTable.Builder> map;
    private final String modId;

    //? if >= 1.19.4 {
    private final LootTableSubProvider provider;
    //? } else {
    /*private final BlockLoot provider;
    *///? }

    public DataGenerationStreamEvent(
            //? if >= 1.19.4 {
            final LootTableSubProvider tableProvider,
             //? } else {
            /*final BlockLoot tableProvider,
            *///? }

            String modId,
            ExistingFileHelper fileHelper,
            Map<ResourceLocation,
            LootTable.Builder> map
    ) {
        super();
        this.provider = tableProvider;
        this.modId = modId;
        this.fileHelper = fileHelper;
        this.map = map;
    }

    //? if >= 1.19.4 {
    public LootTableSubProvider getProvider() {
        return provider;
    }
    //? } else {
    /*public BlockLoot getProvider() {
        return provider;
    }
    *///? }

    public String getModId() {
        return modId;
    }

    public ExistingFileHelper getFileHelper() {
        return fileHelper;
    }

    public Map<ResourceLocation, LootTable.Builder> getMap() {
        return map;
    }
}
