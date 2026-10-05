package com.dtteam.dynamictrees.model.geometry;

import com.dtteam.dynamictrees.model.baked.SurfaceRootBlockBakedModel;
import com.dtteam.dynamictrees.model.loader.SurfaceRootBlockModelLoader;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

//? if >= 1.19.2 {
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
//? } else {
/*import net.minecraftforge.client.model.IModelConfiguration;
*///? }

/**
 * Bakes {@link SurfaceRootBlockBakedModel} from bark texture location given by {@link SurfaceRootBlockModelLoader}.
 *
 * @author Harley O'Connor
 */
public class SurfaceRootBlockModelGeometry extends BranchBlockModelGeometry {
    public SurfaceRootBlockModelGeometry(final ResourceLocation barkResLoc) {
        super(barkResLoc, null, null, false);
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
        return new SurfaceRootBlockBakedModel(this.barkTextureLocation, spriteGetter);
    }

}