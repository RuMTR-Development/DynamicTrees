package com.dtteam.dynamictrees.platform;

import com.dtteam.dynamictrees.DynamicTrees;
import com.dtteam.dynamictrees.entity.FallingTreeEntity;
import com.dtteam.dynamictrees.model.FallingTreeEntityModel;
import com.dtteam.dynamictrees.model.FallingTreeEntityModelFabric;
import com.dtteam.dynamictrees.platform.services.IClientHelper;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

//? if >= 1.19.4 {
import net.minecraft.client.renderer.texture.SpriteContents;
//? }

public class FabricClientHelper implements IClientHelper {

    @Override
    public int getPixelRGBA(TextureAtlasSprite sprite, int x, int y) {
        try {
            //? if >= 1.19.4 {
            SpriteContents contents = sprite.contents();
            NativeImage image = contents.originalImage;
            //? } else {
            /*NativeImage image = sprite.mainImage[0];
            *///? }

            if (image != null) {
                return image.getPixelRGBA(x, y);
            }
            return 0;
        } catch (Exception e) {
            DynamicTrees.LOG.warn("Failed to get pixel from sprite: {}", e.getMessage());
            return 0;
        }
    }

    @Override
    public FallingTreeEntityModel newFallingTreeEntityModel(FallingTreeEntity entity) {
        return new FallingTreeEntityModelFabric(entity);
    }

}
