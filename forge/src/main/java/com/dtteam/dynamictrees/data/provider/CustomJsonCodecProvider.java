//? if < 1.19.2 {
/*//
// Copyright (c) Forge Development LLC and contributors
// SPDX-License-Identifier: LGPL-2.1-only
//

package com.dtteam.dynamictrees.data.provider;

import com.google.gson.Gson;import com.google.gson.JsonArray;import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;

import java.util.function.BiConsumer;
import cpw.mods.modlauncher.api.LamdbaExceptionUtils;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.data.ExistingFileHelper;import net.minecraftforge.common.data.ExistingFileHelper.ResourceType;
import org.slf4j.Logger;


public class CustomJsonCodecProvider<T> implements DataProvider {
    private static final Logger LOGGER = LogUtils.getLogger();
    protected final DataGenerator dataGenerator;
    protected final ExistingFileHelper existingFileHelper;
    protected final String modid;
    protected final DynamicOps<JsonElement> dynamicOps;
    protected final PackType packType;
    protected final String directory;
    protected final Codec<T> codec;
    protected final Map<ResourceLocation, T> entries;
    protected Map<ResourceLocation, ICondition[]> conditions = Collections.emptyMap();

    public CustomJsonCodecProvider(DataGenerator dataGenerator, ExistingFileHelper existingFileHelper, String modid, DynamicOps<JsonElement> dynamicOps, PackType packType,
                             String directory, Codec<T> codec, Map<ResourceLocation, T> entries) {
        final ResourceType resourceType = new ResourceType(packType, ".json", directory);
        for (ResourceLocation id : entries.keySet()) {
            existingFileHelper.trackGenerated(id, resourceType);
        }
        this.dataGenerator = dataGenerator;
        this.existingFileHelper = existingFileHelper;
        this.modid = modid;
        this.dynamicOps = dynamicOps;
        this.packType = packType;
        this.directory = directory;
        this.codec = codec;
        this.entries = entries;
    }

    public static <T> CustomJsonCodecProvider<T> forDatapackRegistry(DataGenerator dataGenerator, ExistingFileHelper existingFileHelper, String modid,
                                                               RegistryOps<JsonElement> registryOps, ResourceKey<Registry<T>> registryKey, Map<ResourceLocation, T> entries) {
        final ResourceLocation registryId = registryKey.location();
        // Minecraft datapack registry folders are in data/json-namespace/registry-name/
        // Non-vanilla registry folders are data/json-namespace/registry-namespace/registry-name/
        final String registryFolder = registryId.getNamespace().equals("minecraft")
                ? registryId.getPath()
                : registryId.getNamespace() + "/" + registryId.getPath();
        final Codec<T> codec = (Codec<T>) RegistryAccess.REGISTRIES.get(registryKey).codec();
        return new CustomJsonCodecProvider<>(dataGenerator, existingFileHelper, modid, registryOps, PackType.SERVER_DATA, registryFolder, codec, entries);
    }

    @Override
    public void run(final HashCache cache) throws IOException {
        Gson gson = new Gson();

        final Path outputFolder = this.dataGenerator.getOutputFolder();
        final String dataFolder = this.packType.getDirectory();
        gather(LamdbaExceptionUtils.rethrowBiConsumer((id, value) -> {
            final Path path = outputFolder.resolve(String.join("/", dataFolder, id.getNamespace(), this.directory, id.getPath() + ".json"));
            JsonElement encoded = this.codec.encodeStart(this.dynamicOps, value)
                    .getOrThrow(false, msg -> LOGGER.error("Failed to encode {}: {}", path, msg));
            ICondition[] conditions = this.conditions.get(id);
            if (conditions != null && conditions.length > 0)
            {
                if(encoded instanceof JsonObject obj)
                {
                    JsonArray conditionsProp = new JsonArray();

                    for (ICondition condition : conditions) {
                        conditionsProp.add(CraftingHelper.serialize(condition));
                    }

                    obj.add("forge:conditions", conditionsProp);
                }
                else
                {
                    LOGGER.error("Attempted to apply conditions to a type that is not a JsonObject! - Path: {}", path);
                }
            }
            DataProvider.save(gson, cache, encoded, path);
        }));
    }

    protected void gather(BiConsumer<ResourceLocation, T> consumer) {
        this.entries.forEach(consumer);
    }

    @Override
    public String getName() {
        return String.format("%s generator for %s", this.directory, this.modid);
    }

    public CustomJsonCodecProvider<T> setConditions(Map<ResourceLocation, ICondition[]> conditions) {
        this.conditions = conditions;
        return this;
    }
}
*///? }
