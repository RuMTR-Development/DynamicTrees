package com.dtteam.dynamictrees.model.loader;

import com.dtteam.dynamictrees.model.geometry.PalmLeavesModelGeometry;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.ResourceLocationException;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//? if >= 1.19.2 {
import net.minecraftforge.client.model.geometry.IGeometryLoader;
 //? } else {
/*import net.minecraftforge.client.model.IModelLoader;
*///? }

//~ if < 1.19.2 'IGeometryLoader' -> 'IModelLoader'
public class PalmLeavesModelLoader implements IGeometryLoader<PalmLeavesModelGeometry> {

    public static final Logger LOGGER = LogManager.getLogger();

    private static final String FROND = "frond";
    private static final String TEXTURES = "textures";

    private final int frondType;

    public PalmLeavesModelLoader(int type){
        frondType = type;
    }

    @Override
    public PalmLeavesModelGeometry read(
            //? if >= 1.19.2 {
            JsonObject modelObject, JsonDeserializationContext deserializationContext
             //? } else {
            /*JsonDeserializationContext deserializationContext, JsonObject modelObject
            *///? }
    ) {
        final JsonObject textures = this.getTexturesObject(modelObject);
        return new PalmLeavesModelGeometry(getTextureLocation(textures, FROND), frondType);
    }

    protected ResourceLocation getTextureLocation (final JsonObject textureObject, final String textureElement) {
        try {
            return this.getResLocOrThrow(this.getOrThrow(textureObject, textureElement));
        } catch (final RuntimeException e) {
            LOGGER.error("{} missing or did not have valid \"{}\" texture location element, using missing " +
                    "texture.", this.getModelTypeName(), textureElement);
            return MissingTextureAtlasSprite.getLocation();
        }
    }

    protected JsonObject getTexturesObject (final JsonObject modelContents) {
        if (!modelContents.has(TEXTURES) || !modelContents.get(TEXTURES).isJsonObject())
            this.throwRequiresElement(TEXTURES, "Json Object");

        return modelContents.getAsJsonObject(TEXTURES);
    }

    protected ResourceLocation getResLocOrThrow(final String resLocStr) {
        try {
            return ResourceLocation.parse(resLocStr);
        } catch (ResourceLocationException e) {
            throw new RuntimeException(e);
        }
    }

    protected String getOrThrow(final JsonObject jsonObject, final String identifier) {
        if (jsonObject.get(identifier) == null || !jsonObject.get(identifier).isJsonPrimitive() ||
                !jsonObject.get(identifier).getAsJsonPrimitive().isString())
            this.throwRequiresElement(identifier, "String");

        return jsonObject.get(identifier).getAsString();
    }

    protected void throwRequiresElement (final String element, final String expectedType) {
        throw new RuntimeException(this.getModelTypeName() + " requires a valid \"" + element + "\" element of " +
                "type " + expectedType + ".");
    }

    /**
     * @return The type of model the class is loading. Useful for warnings when using sub-classes.
     */
    protected String getModelTypeName () {
        return "Palm Fronds";
    }

    //? if < 1.19.2 {
    /*@Override
    public void onResourceManagerReload(ResourceManager resourceManager) {

    }
    *///? }
}