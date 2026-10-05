package com.dtteam.dynamictrees.model.geometry;

import com.dtteam.dynamictrees.model.baked.BasicRootsBlockBakedModel;
import com.dtteam.dynamictrees.model.baked.SurfaceRootBlockBakedModel;
import com.dtteam.dynamictrees.model.loader.SurfaceRootBlockModelLoader;
import com.dtteam.dynamictrees.tree.family.Family;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

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
public class RootsBlockModelGeometry extends BranchBlockModelGeometry {

    public RootsBlockModelGeometry(@Nullable final ResourceLocation barkTextureLocation, @Nullable final ResourceLocation ringsTextureLocation, @Nullable final ResourceLocation familyName) {
        super(barkTextureLocation, ringsTextureLocation, familyName, false);
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
        return new BasicRootsBlockBakedModel(context, this.barkTextureLocation, this.ringsTextureLocation, spriteGetter);
    }

    @Override
    protected boolean useThickModel(final Family family) {
        return false;
    }

}