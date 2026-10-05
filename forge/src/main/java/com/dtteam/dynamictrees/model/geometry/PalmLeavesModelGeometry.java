package com.dtteam.dynamictrees.model.geometry;

import com.dtteam.dynamictrees.model.baked.LargePalmLeavesBakedModel;
import com.dtteam.dynamictrees.model.baked.MediumPalmLeavesBakedModel;
import com.dtteam.dynamictrees.model.baked.SmallPalmLeavesBakedModel;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

//? if >= 1.19.2 {
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;
//? } else {
/*import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.geometry.IModelGeometry;
*///? }

//~ if < 1.19.2 'IUnbakedGeometry' -> 'IModelGeometry'
public class PalmLeavesModelGeometry implements IUnbakedGeometry<PalmLeavesModelGeometry> {

    protected final ResourceLocation frondsResLoc;

    private final int frondType;

    public PalmLeavesModelGeometry(final ResourceLocation frondsResLoc, int type){
        this.frondsResLoc = frondsResLoc;
        this.frondType = type;
    }

    @Override
    public BakedModel bake(
            //~ if < 1.19.2 'IGeometryBakingContext' -> 'IModelConfiguration'
            IGeometryBakingContext context,

            //~ if < 1.19.4 'ModelBaker' -> 'ModelBakery'
            ModelBaker modelBaker,

            Function<Material, TextureAtlasSprite> spriteGetter,
            ModelState modelState,
            ItemOverrides itemOverrides,
            ResourceLocation resourceLocation
    ) {
        return switch (frondType) {
            default -> new LargePalmLeavesBakedModel(frondsResLoc, spriteGetter);
            case 1 -> new MediumPalmLeavesBakedModel(frondsResLoc, spriteGetter);
            case 2 -> new SmallPalmLeavesBakedModel(frondsResLoc, spriteGetter);
        };
    }

    //? if < 1.19.2 {
    /*@Override
    public Collection<Material> getTextures(IModelConfiguration iModelConfiguration, Function<ResourceLocation, UnbakedModel> function, Set<Pair<String, String>> set) {
        return List.of();
    }
    *///? } else if < 1.19.4 {
    /*@Override
    public Collection<Material> getMaterials(IGeometryBakingContext iGeometryBakingContext, Function<ResourceLocation, UnbakedModel> function, Set<Pair<String, String>> set) {
        return List.of();
    }
    *///? }

}