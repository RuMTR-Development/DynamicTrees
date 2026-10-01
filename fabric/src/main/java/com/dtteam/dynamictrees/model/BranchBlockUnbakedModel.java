package com.dtteam.dynamictrees.model;

import com.dtteam.dynamictrees.model.baked.BasicBranchBlockBakedModel;
import com.dtteam.dynamictrees.model.baked.ThickBranchBlockBakedModel;
import com.dtteam.dynamictrees.tree.family.Family;
import com.mojang.datafixers.util.Pair;import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;import java.util.Set;import java.util.function.Function;

//? if >= 1.19.4 {
import net.minecraft.client.resources.model.ModelBaker;
//? } else {
/*import net.minecraft.client.resources.model.ModelBakery;
*///? }

public class BranchBlockUnbakedModel implements UnbakedModel {

    protected final ResourceLocation barkTextureLocation;
    protected final ResourceLocation ringsTextureLocation;
    protected final ResourceLocation familyName;
    protected final boolean forceThickness;

    public BranchBlockUnbakedModel(ResourceLocation barkTextureLocation, ResourceLocation ringsTextureLocation, @Nullable ResourceLocation familyName, boolean forceThickness) {
        this.barkTextureLocation = barkTextureLocation;
        this.ringsTextureLocation = ringsTextureLocation;
        this.familyName = familyName;
        this.forceThickness = forceThickness;
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return Collections.emptyList();
    }

    //? if >= 1.19.4 {
    @Override
    public void resolveParents(Function<ResourceLocation, UnbakedModel> resolver) {

    }
    //? } else {
    /*@Override
    public Collection<Material> getMaterials(Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
        return List.of();
    }
    *///? }

    @Override
    public BakedModel bake(
            //? if >= 1.19.4 {
            ModelBaker baker,
            //? } else {
            /*ModelBakery baker,
            *///? }

            Function<Material, TextureAtlasSprite> spriteGetter,
            ModelState state

            //? if < 1.21 {
            /*,
            ResourceLocation location
            *///? }
    ) {
        TextureAtlasSprite barkSprite = spriteGetter.apply(new Material(InventoryMenu.BLOCK_ATLAS, barkTextureLocation));
        TextureAtlasSprite ringsSprite = spriteGetter.apply(new Material(InventoryMenu.BLOCK_ATLAS, ringsTextureLocation));

        Family family = familyName != null ? Family.REGISTRY.get(familyName) : null;
        boolean useThickModel = forceThickness || (family != null && family.isThick());

        if (useThickModel) {
            //? if >= 1.19.4 {
            ResourceLocation thickRingsLocation = ringsTextureLocation.withSuffix("_thick");
            //? } else {
            /*ResourceLocation thickRingsLocation = new ResourceLocation(ringsTextureLocation.getNamespace(), ringsTextureLocation.getPath() + "_thick");
            *///? }

            TextureAtlasSprite thickRingsSprite = spriteGetter.apply(new Material(InventoryMenu.BLOCK_ATLAS, thickRingsLocation));
            return new ThickBranchBlockBakedModel(barkSprite, ringsSprite, thickRingsSprite);
        }

        return new BasicBranchBlockBakedModel(barkSprite, ringsSprite);
    }
}
