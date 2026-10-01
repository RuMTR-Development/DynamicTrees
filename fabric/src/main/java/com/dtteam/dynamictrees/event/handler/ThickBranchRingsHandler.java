//? if < 1.19.4 {
/*package com.dtteam.dynamictrees.event.handler;

import com.dtteam.dynamictrees.DynamicTrees;
import com.dtteam.dynamictrees.api.registry.Registry;
import com.dtteam.dynamictrees.block.leaves.LeavesProperties;
import com.dtteam.dynamictrees.block.soil.SoilProperties;
import com.dtteam.dynamictrees.client.ThickBranchRingsSprite;
import com.dtteam.dynamictrees.tree.family.Family;
import com.dtteam.dynamictrees.tree.species.Species;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public class ThickBranchRingsHandler {
    public static void register() {
        ClientSpriteRegistryCallback.event(InventoryMenu.BLOCK_ATLAS).register(ThickBranchRingsHandler::onBlockAtlas);
    }

    @SuppressWarnings("unchecked")
    private static List<Registry<Family>> castToFamilyRegistries(Registry<?>... registries){
        return Arrays.stream(registries)
                .filter(registry->Family.class.isAssignableFrom(registry.getType()))
                .map(registry->(Registry<Family>)registry).toList();
    }

    private static List<Registry<Family>> getRegistries() {
        return castToFamilyRegistries(
                SoilProperties.REGISTRY,
                Family.REGISTRY,
                Species.REGISTRY,
                LeavesProperties.REGISTRY
        );
    }

    private static void onBlockAtlas(TextureAtlas atlas, ClientSpriteRegistryCallback.Registry registry) {
        List<Registry<Family>> registries = getRegistries();

        for (Registry<Family> familyRegistry : registries) {
            familyRegistry.dataGenerationStream(DynamicTrees.MOD_ID).forEach(
                    family -> ThickBranchRingsHandler.registerBase(family, registry)
            );
        }
    }

    public static void inject(TextureAtlas atlas, Set<ResourceLocation> ids, List<TextureAtlasSprite> sprites) {
        if (!atlas.location().equals(InventoryMenu.BLOCK_ATLAS)) {
            return;
        }

        List<Registry<Family>> registries = getRegistries();

        for (Registry<Family> familyRegistry : registries) {
            familyRegistry.dataGenerationStream(DynamicTrees.MOD_ID).forEach(
                    family -> ThickBranchRingsHandler.registerThick(family, atlas, ids, sprites)
            );
        }
    }

    private static void registerBase(Family family, ClientSpriteRegistryCallback.Registry registry) {
        for (ResourceLocation baseLocation : family.topBranchTextureLocations()) {
            registry.register(baseLocation);
        }
    }

    private static void registerThick(Family family, TextureAtlas atlas, Set<ResourceLocation> ids, List<TextureAtlasSprite> sprites) {
        for (ResourceLocation baseLocation : family.topBranchTextureLocations()) {
            ResourceLocation thickLocation = new ResourceLocation(baseLocation.getNamespace(), baseLocation.getPath() + "_thick");
            TextureAtlasSprite base = sprites.stream().filter(sprite -> sprite.getName().equals(baseLocation)).findAny().orElse(null);

            if (base == null) {
                continue;
            }

            ThickBranchRingsSprite thick = new ThickBranchRingsSprite(
                    atlas,
                    thickLocation,
                    base,
                    0,
                    base.getX(),
                    base.getY(),
                    base.getX(),
                    base.getY()
            );

            ids.add(thickLocation);
            sprites.add(thick);
        }
    }
}
*///? }
