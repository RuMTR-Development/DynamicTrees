package com.dtteam.dynamictrees.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

//? if < 1.19.4 {
/*import com.dtteam.dynamictrees.event.handler.ThickBranchRingsHandler;
*///? }

@Mixin(TextureAtlas.class)
public class TextureAtlasMixin {
    //? if < 1.19.4 {
    /*@Inject(at = @At(value = "NEW", target = "(Ljava/util/Set;IIILjava/util/List;)Lnet/minecraft/client/renderer/texture/TextureAtlas$Preparations;"), method = "prepareToStitch")
    private void inject(ResourceManager resourceManager, Stream<ResourceLocation> spriteNames, ProfilerFiller profiler, int mipLevel, CallbackInfoReturnable<TextureAtlas.Preparations> cir, @Local Set<ResourceLocation> ids, @Local List<TextureAtlasSprite> sprites) {
        ThickBranchRingsHandler.inject((TextureAtlas) (Object) this, ids, sprites);
    }
    *///? }
}
