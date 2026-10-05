package com.dtteam.dynamictrees.model.loader;

import com.dtteam.dynamictrees.model.geometry.BranchBlockModelGeometry;
import com.dtteam.dynamictrees.model.geometry.SurfaceRootBlockModelGeometry;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

/**
 * @author Harley O'Connor
 */

public class SurfaceRootBlockModelLoader extends BranchBlockModelLoader {

    @Override
    public BranchBlockModelGeometry read(
            //? if >= 1.19.2 {
            JsonObject modelObject, JsonDeserializationContext deserializationContext
            //? } else {
            /*JsonDeserializationContext deserializationContext, JsonObject modelObject
            *///? }
    ) {
        final JsonObject textures = this.getTexturesObject(modelObject);
        return new SurfaceRootBlockModelGeometry(this.getBarkTextureLocation(textures));
    }

    @Override
    protected String getModelTypeName() {
        return "Surface Root";
    }

}