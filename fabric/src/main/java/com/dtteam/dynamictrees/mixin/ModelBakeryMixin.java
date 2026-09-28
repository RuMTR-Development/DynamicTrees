package com.dtteam.dynamictrees.mixin;

import com.dtteam.dynamictrees.model.DTModelModifier;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;

import java.util.function.BiFunction;
import java.util.function.Function;

@Mixin(ModelBakery.class)
public class ModelBakeryMixin {
    //? if < 1.21 {
    /*@WrapOperation(method = "method_45877", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/ModelBakery$ModelBakerImpl;bake(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/client/resources/model/ModelState;)Lnet/minecraft/client/resources/model/BakedModel;"))
    private BakedModel wrapSingleOuterBake(@Coerce ModelBaker baker, ResourceLocation location, ModelState transform, Operation<BakedModel> original, BiFunction<ResourceLocation, Material, TextureAtlasSprite> textureGetterOriginal) {
        ModelResourceLocation modelId = location instanceof ModelResourceLocation
                ? (ModelResourceLocation) location
                : new ModelResourceLocation(location, "inventory");

        if (modelId.equals(ModelBakery.MISSING_MODEL_LOCATION)) {
            return baker.bake(location, transform);
        }

        Function<Material, TextureAtlasSprite> textureGetter = material -> textureGetterOriginal.apply(location, material);
        BakedModel model = original.call(baker, location, transform);
        return DTModelModifier.modifyModelAfterBake(model, modelId, textureGetter);
    }
    *///? }
}
