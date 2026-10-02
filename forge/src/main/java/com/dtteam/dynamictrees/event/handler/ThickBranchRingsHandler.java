//? if < 1.19.4 {
package com.dtteam.dynamictrees.event.handler;

import com.dtteam.dynamictrees.DynamicTrees;
import com.dtteam.dynamictrees.api.registry.Registry;
import com.dtteam.dynamictrees.block.leaves.LeavesProperties;
import com.dtteam.dynamictrees.block.soil.SoilProperties;
import com.dtteam.dynamictrees.client.ThickBranchRingsSprite;
import com.dtteam.dynamictrees.tree.family.Family;
import com.dtteam.dynamictrees.tree.species.Species;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;import net.minecraftforge.eventbus.api.SubscribeEvent;import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

@Mod.EventBusSubscriber(modid = DynamicTrees.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ThickBranchRingsHandler {
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

    @SubscribeEvent
    public static void onTextureStitchPre(TextureStitchEvent.Pre event) {
        List<Registry<Family>> registries = getRegistries();

        for (Registry<Family> familyRegistry : registries) {
            familyRegistry.dataGenerationStream(DynamicTrees.MOD_ID).forEach(
                    family -> ThickBranchRingsHandler.registerBase(family, event)
            );
        }
    }

    @SubscribeEvent
    public static void onTextureStitchPost(TextureStitchEvent.Post event) {
        if (!event.getAtlas().location().equals(InventoryMenu.BLOCK_ATLAS)) {
            return;
        }

        List<Registry<Family>> registries = getRegistries();

        for (Registry<Family> familyRegistry : registries) {
            familyRegistry.getAllFor(DynamicTrees.MOD_ID).forEach(
                    family -> ThickBranchRingsHandler.registerThick(family, event.getAtlas())
            );
        }
    }

    private static void registerBase(Family family, TextureStitchEvent.Pre registry) {
        for (ResourceLocation baseLocation : family.topBranchTextureLocations()) {
            registry.addSprite(baseLocation);
        }
    }

    private static void registerThick(Family family, TextureAtlas atlas) {
        for (ResourceLocation baseLocation : family.topBranchTextureLocations()) {
            ResourceLocation thickLocation = ResourceLocation.fromNamespaceAndPath(baseLocation.getNamespace(), baseLocation.getPath() + "_thick");

            if (atlas.sprites.contains(thickLocation)) {
                continue;
            }

            TextureAtlasSprite base = atlas.getSprite(baseLocation);

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

            atlas.sprites.add(thickLocation);
            atlas.texturesByName.put(thickLocation, thick);
        }
    }
}
//? }
