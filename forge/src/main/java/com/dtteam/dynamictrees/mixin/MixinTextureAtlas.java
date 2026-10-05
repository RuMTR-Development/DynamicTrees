package com.dtteam.dynamictrees.mixin;

import com.dtteam.dynamictrees.DynamicTrees;
import com.dtteam.dynamictrees.client.ThickBranchRingsSprite;
import com.dtteam.dynamictrees.tree.family.Family;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.texture.Stitcher;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.inventory.InventoryMenu;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Stream;

@Mixin(TextureAtlas.class)
public class MixinTextureAtlas {
    //? if < 1.19.4 {
    /*@SuppressWarnings("unchecked")
    @Shadow
    @Final
    private ResourceLocation location;

    @Unique
    private static Map<ResourceLocation, ThickBranchRingsSprite.Params> THICK_BRANCH_SPRITES;

    @Inject(method = "prepareToStitch", at = @At("HEAD"))
    public void onPrepare(ResourceManager resourceManager, Stream<ResourceLocation> spriteNames, ProfilerFiller profiler, int mipLevel, CallbackInfoReturnable<TextureAtlas.Preparations> cir) {
        if (!this.location.equals(InventoryMenu.BLOCK_ATLAS)) {
            return;
        }

        THICK_BRANCH_SPRITES = new HashMap<>();

        Family.REGISTRY.getAllFor(DynamicTrees.MOD_ID).forEach(
                family -> {
                    for (var baseLocation : family.topBranchTextureLocations()) {
                        var params = new ThickBranchRingsSprite.Params(baseLocation);

                        THICK_BRANCH_SPRITES.put(params.thickLocation, params);
                    }
                }
        );
    }

    @Inject(method = "prepareToStitch", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/Stitcher;registerSprite(Lnet/minecraft/client/renderer/texture/TextureAtlasSprite$Info;)V", ordinal = 1))
    public void onRegisterSprite(ResourceManager resourceManager, Stream<ResourceLocation> spriteNames, ProfilerFiller profiler, int mipLevel, CallbackInfoReturnable<TextureAtlas.Preparations> cir, @Local Stitcher stitcher) {
        if (!this.location.equals(InventoryMenu.BLOCK_ATLAS)) {
            return;
        }

        for (var params : THICK_BRANCH_SPRITES.values()) {
            stitcher.registerSprite(params.info);
        }
    }

    @WrapOperation(method = "getBasicSpriteInfos", at = @At(value = "INVOKE", target = "Lnet/minecraft/resources/ResourceLocation;equals(Ljava/lang/Object;)Z"))
    public boolean onGetBasicSpriteInfo(ResourceLocation instance, Object other, Operation<Boolean> original) {
        if (!this.location.equals(InventoryMenu.BLOCK_ATLAS)) {
            return original.call(instance, other);
        }

        if (other instanceof ResourceLocation && THICK_BRANCH_SPRITES.containsKey(other)) {
            return true;
        }

        return original.call(instance, other);
    }

    @Inject(method = "lambda$getLoadedSprites$4", at = @At(value = "HEAD"), cancellable = true)
    public void onGetLoadedSprite(int mipLevel, Queue queue, List list, ResourceManager resourceManager, TextureAtlasSprite.Info info, int storageX, int storageY, int x, int y, CallbackInfo ci) {
        if (!this.location.equals(InventoryMenu.BLOCK_ATLAS)) {
            return;
        }

        var params = THICK_BRANCH_SPRITES.get(info.name());

        if (params == null) {
            return;
        }

        ci.cancel();

        params.storageX = storageX;
        params.storageY = storageY;
        params.x = x;
        params.y = y;
    }

    @Inject(method = "getLoadedSprites", at = @At("TAIL"))
    public void onGetLoadedSpritesEnd(ResourceManager resourceManager, Stitcher stitcher, int mipLevel, CallbackInfoReturnable<List<TextureAtlasSprite>> cir) {
        if (!this.location.equals(InventoryMenu.BLOCK_ATLAS)) {
            return;
        }

        for (var params : THICK_BRANCH_SPRITES.values()) {
            var original = cir.getReturnValue()
                    .stream()
                    .filter(sprite -> sprite.getName().equals(params.baseLocation))
                    .findFirst()
                    .orElse(null);

            if (original == null) {
                continue;
            }

            var sprite = new ThickBranchRingsSprite(
                    (TextureAtlas) (Object) this,
                    params.info,
                    mipLevel,
                    params.storageX,
                    params.storageY,
                    params.x,
                    params.y,
                    original
            );

            cir.getReturnValue().add(sprite);
        }
    }
    *///? }

}
