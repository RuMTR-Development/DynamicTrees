package com.dtteam.dynamictrees.event;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Map;

public class DataGenerationStreamEvent extends Event implements IModBusEvent {

    private final ExistingFileHelper fileHelper;
    private final Map<ResourceLocation, LootTable.Builder> map;
    private final LootTableSubProvider provider;
    private final String modId;

    public DataGenerationStreamEvent(final LootTableSubProvider tableProvider, String modId, ExistingFileHelper fileHelper, Map<ResourceLocation, LootTable.Builder> map) {
        super();
        this.provider = tableProvider;
        this.modId = modId;
        this.fileHelper = fileHelper;
        this.map = map;
    }

    public LootTableSubProvider getProvider() {
        return provider;
    }

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
