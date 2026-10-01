package com.dtteam.dynamictrees.api.resource;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.function.BiConsumer;import java.util.function.Consumer;

//? if >= 1.19.4 {
import net.minecraft.server.packs.resources.IoSupplier;
//? } else {
/*import net.minecraft.server.packs.PackType;import net.minecraft.server.packs.resources.Resource.IoSupplier;
*///? }

/**
 * A {@linkplain PackResources resource pack} that reads from the {@code trees} folder.
 *
 * @author Harley O'Connor
 */
public interface TreeResourcePack extends PackResources {
    //? if >= 1.19.4 {
    @SuppressWarnings("ConstantConditions")
    default IoSupplier<InputStream> getResource(ResourceLocation location) {
        return this.getResource(null, location);
    }
    //? } else {
    /*@SuppressWarnings("ConstantConditions")
    default InputStream getResource(ResourceLocation location) {
        try {
            return this.getResource(null, location);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    *///? }

    //? if >= 1.19.4 {
    @SuppressWarnings("ConstantConditions")
    default void listResources(String namespace, String path, ResourceOutput resourceOutput) {
        this.listResources(null, namespace, path, resourceOutput);
    }
    //? } else {
    /*@SuppressWarnings("ConstantConditions")
    default void listResources(String namespace, String path, Consumer<ResourceLocation> resourceOutput) {
        this.getResources(null, namespace, path, (location) -> true).forEach(resourceOutput);
    }
    *///? }

    @SuppressWarnings("ConstantConditions")
    default boolean hasResource(
            ResourceLocation location
    ) {
        //? if >= 1.19.4 {
        return this.getResource(null, location) != null;
        //? } else {
        /*try {
            this.getResource(null, location);
            return true;
        } catch (IOException ignored) {
            return false;
        }
        *///? }
    }

    @SuppressWarnings("ConstantConditions")
    default Set<String> getNamespaces() {
        return this.getNamespaces(null);
    }
}