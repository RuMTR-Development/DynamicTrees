package com.dtteam.dynamictrees.deserialization.deserializer;

import com.dtteam.dynamictrees.deserialization.JsonDeserializers;
import com.dtteam.dynamictrees.deserialization.result.Result;
import com.google.gson.JsonElement;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

//? if >= 1.21 {
import net.minecraft.world.level.material.MapColor;
 //? } else {
/*import net.minecraft.world.level.material.MaterialColor;
*///? }

/**
 * @author Harley O'Connor
 */
//? if >= 1.21 {
public final class MapColorDeserializer implements JsonDeserializer<MapColor> {

    private static final Map<ResourceLocation, MapColor> MATERIAL_COLORS =
            Util.make(new HashMap<>(), MapColors -> {
                MapColors.put(new ResourceLocation("none"), MapColor.NONE);
                MapColors.put(new ResourceLocation("grass"), MapColor.GRASS);
                MapColors.put(new ResourceLocation("sand"), MapColor.SAND);
                MapColors.put(new ResourceLocation("wool"), MapColor.WOOL);
                MapColors.put(new ResourceLocation("fire"), MapColor.FIRE);
                MapColors.put(new ResourceLocation("ice"), MapColor.ICE);
                MapColors.put(new ResourceLocation("metal"), MapColor.METAL);
                MapColors.put(new ResourceLocation("plant"), MapColor.PLANT);
                MapColors.put(new ResourceLocation("snow"), MapColor.SNOW);
                MapColors.put(new ResourceLocation("clay"), MapColor.CLAY);
                MapColors.put(new ResourceLocation("dirt"), MapColor.DIRT);
                MapColors.put(new ResourceLocation("stone"), MapColor.STONE);
                MapColors.put(new ResourceLocation("water"), MapColor.WATER);
                MapColors.put(new ResourceLocation("wood"), MapColor.WOOD);
                MapColors.put(new ResourceLocation("quartz"), MapColor.QUARTZ);
                MapColors.put(new ResourceLocation("color_orange"), MapColor.COLOR_ORANGE);
                MapColors.put(new ResourceLocation("color_magenta"), MapColor.COLOR_MAGENTA);
                MapColors.put(new ResourceLocation("color_light_blue"), MapColor.COLOR_LIGHT_BLUE);
                MapColors.put(new ResourceLocation("color_yellow"), MapColor.COLOR_YELLOW);
                MapColors.put(new ResourceLocation("color_light_green"), MapColor.COLOR_LIGHT_GREEN);
                MapColors.put(new ResourceLocation("color_pink"), MapColor.COLOR_PINK);
                MapColors.put(new ResourceLocation("color_gray"), MapColor.COLOR_GRAY);
                MapColors.put(new ResourceLocation("color_light_gray"), MapColor.COLOR_LIGHT_GRAY);
                MapColors.put(new ResourceLocation("color_cyan"), MapColor.COLOR_CYAN);
                MapColors.put(new ResourceLocation("color_purple"), MapColor.COLOR_PURPLE);
                MapColors.put(new ResourceLocation("color_blue"), MapColor.COLOR_BLUE);
                MapColors.put(new ResourceLocation("color_brown"), MapColor.COLOR_BROWN);
                MapColors.put(new ResourceLocation("color_green"), MapColor.COLOR_GREEN);
                MapColors.put(new ResourceLocation("color_red"), MapColor.COLOR_RED);
                MapColors.put(new ResourceLocation("color_black"), MapColor.COLOR_BLACK);
                MapColors.put(new ResourceLocation("gold"), MapColor.GOLD);
                MapColors.put(new ResourceLocation("diamond"), MapColor.DIAMOND);
                MapColors.put(new ResourceLocation("lapis"), MapColor.LAPIS);
                MapColors.put(new ResourceLocation("emerald"), MapColor.EMERALD);
                MapColors.put(new ResourceLocation("podzol"), MapColor.PODZOL);
                MapColors.put(new ResourceLocation("nether"), MapColor.NETHER);
                MapColors.put(new ResourceLocation("terracotta_white"), MapColor.TERRACOTTA_WHITE);
                MapColors.put(new ResourceLocation("terracotta_orange"), MapColor.TERRACOTTA_ORANGE);
                MapColors.put(new ResourceLocation("terracotta_magenta"), MapColor.TERRACOTTA_MAGENTA);
                MapColors.put(new ResourceLocation("terracotta_light_blue"), MapColor.TERRACOTTA_LIGHT_BLUE);
                MapColors.put(new ResourceLocation("terracotta_yellow"), MapColor.TERRACOTTA_YELLOW);
                MapColors.put(new ResourceLocation("terracotta_light_green"), MapColor.TERRACOTTA_LIGHT_GREEN);
                MapColors.put(new ResourceLocation("terracotta_pink"), MapColor.TERRACOTTA_PINK);
                MapColors.put(new ResourceLocation("terracotta_gray"), MapColor.TERRACOTTA_GRAY);
                MapColors.put(new ResourceLocation("terracotta_light_gray"), MapColor.TERRACOTTA_LIGHT_GRAY);
                MapColors.put(new ResourceLocation("terracotta_cyan"), MapColor.TERRACOTTA_CYAN);
                MapColors.put(new ResourceLocation("terracotta_purple"), MapColor.TERRACOTTA_PURPLE);
                MapColors.put(new ResourceLocation("terracotta_blue"), MapColor.TERRACOTTA_BLUE);
                MapColors.put(new ResourceLocation("terracotta_brown"), MapColor.TERRACOTTA_BROWN);
                MapColors.put(new ResourceLocation("terracotta_green"), MapColor.TERRACOTTA_GREEN);
                MapColors.put(new ResourceLocation("terracotta_red"), MapColor.TERRACOTTA_RED);
                MapColors.put(new ResourceLocation("terracotta_black"), MapColor.TERRACOTTA_BLACK);
                MapColors.put(new ResourceLocation("crimson_nylium"), MapColor.CRIMSON_NYLIUM);
                MapColors.put(new ResourceLocation("crimson_stem"), MapColor.CRIMSON_STEM);
                MapColors.put(new ResourceLocation("crimson_hyphae"), MapColor.CRIMSON_HYPHAE);
                MapColors.put(new ResourceLocation("warped_nylium"), MapColor.WARPED_NYLIUM);
                MapColors.put(new ResourceLocation("warped_stem"), MapColor.WARPED_STEM);
                MapColors.put(new ResourceLocation("warped_hyphae"), MapColor.WARPED_HYPHAE);
                MapColors.put(new ResourceLocation("warped_wart_block"), MapColor.WARPED_WART_BLOCK);
                MapColors.put(new ResourceLocation("deepslate"), MapColor.DEEPSLATE);
                MapColors.put(new ResourceLocation("raw_iron"), MapColor.RAW_IRON);
                MapColors.put(new ResourceLocation("glow_lichen"), MapColor.GLOW_LICHEN);
            });

    public static void registerMapColor(ResourceLocation name, MapColor MapColor) {
        MATERIAL_COLORS.putIfAbsent(name, MapColor);
    }

    @Override
    public Result<MapColor, JsonElement> deserialize(JsonElement input) {
        return JsonDeserializers.RESOURCE_LOCATION.deserialize(input)
                .map(MATERIAL_COLORS::get, "Could not get material color from \"{}\".");
    }
}
//? } else {
/*public final class MapColorDeserializer implements JsonDeserializer<MaterialColor> {

    private static final Map<ResourceLocation, MaterialColor> MATERIAL_COLORS =
            Util.make(new HashMap<>(), MaterialColors -> {
                MaterialColors.put(new ResourceLocation("none"), MaterialColor.NONE);
                MaterialColors.put(new ResourceLocation("grass"), MaterialColor.GRASS);
                MaterialColors.put(new ResourceLocation("sand"), MaterialColor.SAND);
                MaterialColors.put(new ResourceLocation("wool"), MaterialColor.WOOL);
                MaterialColors.put(new ResourceLocation("fire"), MaterialColor.FIRE);
                MaterialColors.put(new ResourceLocation("ice"), MaterialColor.ICE);
                MaterialColors.put(new ResourceLocation("metal"), MaterialColor.METAL);
                MaterialColors.put(new ResourceLocation("plant"), MaterialColor.PLANT);
                MaterialColors.put(new ResourceLocation("snow"), MaterialColor.SNOW);
                MaterialColors.put(new ResourceLocation("clay"), MaterialColor.CLAY);
                MaterialColors.put(new ResourceLocation("dirt"), MaterialColor.DIRT);
                MaterialColors.put(new ResourceLocation("stone"), MaterialColor.STONE);
                MaterialColors.put(new ResourceLocation("water"), MaterialColor.WATER);
                MaterialColors.put(new ResourceLocation("wood"), MaterialColor.WOOD);
                MaterialColors.put(new ResourceLocation("quartz"), MaterialColor.QUARTZ);
                MaterialColors.put(new ResourceLocation("color_orange"), MaterialColor.COLOR_ORANGE);
                MaterialColors.put(new ResourceLocation("color_magenta"), MaterialColor.COLOR_MAGENTA);
                MaterialColors.put(new ResourceLocation("color_light_blue"), MaterialColor.COLOR_LIGHT_BLUE);
                MaterialColors.put(new ResourceLocation("color_yellow"), MaterialColor.COLOR_YELLOW);
                MaterialColors.put(new ResourceLocation("color_light_green"), MaterialColor.COLOR_LIGHT_GREEN);
                MaterialColors.put(new ResourceLocation("color_pink"), MaterialColor.COLOR_PINK);
                MaterialColors.put(new ResourceLocation("color_gray"), MaterialColor.COLOR_GRAY);
                MaterialColors.put(new ResourceLocation("color_light_gray"), MaterialColor.COLOR_LIGHT_GRAY);
                MaterialColors.put(new ResourceLocation("color_cyan"), MaterialColor.COLOR_CYAN);
                MaterialColors.put(new ResourceLocation("color_purple"), MaterialColor.COLOR_PURPLE);
                MaterialColors.put(new ResourceLocation("color_blue"), MaterialColor.COLOR_BLUE);
                MaterialColors.put(new ResourceLocation("color_brown"), MaterialColor.COLOR_BROWN);
                MaterialColors.put(new ResourceLocation("color_green"), MaterialColor.COLOR_GREEN);
                MaterialColors.put(new ResourceLocation("color_red"), MaterialColor.COLOR_RED);
                MaterialColors.put(new ResourceLocation("color_black"), MaterialColor.COLOR_BLACK);
                MaterialColors.put(new ResourceLocation("gold"), MaterialColor.GOLD);
                MaterialColors.put(new ResourceLocation("diamond"), MaterialColor.DIAMOND);
                MaterialColors.put(new ResourceLocation("lapis"), MaterialColor.LAPIS);
                MaterialColors.put(new ResourceLocation("emerald"), MaterialColor.EMERALD);
                MaterialColors.put(new ResourceLocation("podzol"), MaterialColor.PODZOL);
                MaterialColors.put(new ResourceLocation("nether"), MaterialColor.NETHER);
                MaterialColors.put(new ResourceLocation("terracotta_white"), MaterialColor.TERRACOTTA_WHITE);
                MaterialColors.put(new ResourceLocation("terracotta_orange"), MaterialColor.TERRACOTTA_ORANGE);
                MaterialColors.put(new ResourceLocation("terracotta_magenta"), MaterialColor.TERRACOTTA_MAGENTA);
                MaterialColors.put(new ResourceLocation("terracotta_light_blue"), MaterialColor.TERRACOTTA_LIGHT_BLUE);
                MaterialColors.put(new ResourceLocation("terracotta_yellow"), MaterialColor.TERRACOTTA_YELLOW);
                MaterialColors.put(new ResourceLocation("terracotta_light_green"), MaterialColor.TERRACOTTA_LIGHT_GREEN);
                MaterialColors.put(new ResourceLocation("terracotta_pink"), MaterialColor.TERRACOTTA_PINK);
                MaterialColors.put(new ResourceLocation("terracotta_gray"), MaterialColor.TERRACOTTA_GRAY);
                MaterialColors.put(new ResourceLocation("terracotta_light_gray"), MaterialColor.TERRACOTTA_LIGHT_GRAY);
                MaterialColors.put(new ResourceLocation("terracotta_cyan"), MaterialColor.TERRACOTTA_CYAN);
                MaterialColors.put(new ResourceLocation("terracotta_purple"), MaterialColor.TERRACOTTA_PURPLE);
                MaterialColors.put(new ResourceLocation("terracotta_blue"), MaterialColor.TERRACOTTA_BLUE);
                MaterialColors.put(new ResourceLocation("terracotta_brown"), MaterialColor.TERRACOTTA_BROWN);
                MaterialColors.put(new ResourceLocation("terracotta_green"), MaterialColor.TERRACOTTA_GREEN);
                MaterialColors.put(new ResourceLocation("terracotta_red"), MaterialColor.TERRACOTTA_RED);
                MaterialColors.put(new ResourceLocation("terracotta_black"), MaterialColor.TERRACOTTA_BLACK);
                MaterialColors.put(new ResourceLocation("crimson_nylium"), MaterialColor.CRIMSON_NYLIUM);
                MaterialColors.put(new ResourceLocation("crimson_stem"), MaterialColor.CRIMSON_STEM);
                MaterialColors.put(new ResourceLocation("crimson_hyphae"), MaterialColor.CRIMSON_HYPHAE);
                MaterialColors.put(new ResourceLocation("warped_nylium"), MaterialColor.WARPED_NYLIUM);
                MaterialColors.put(new ResourceLocation("warped_stem"), MaterialColor.WARPED_STEM);
                MaterialColors.put(new ResourceLocation("warped_hyphae"), MaterialColor.WARPED_HYPHAE);
                MaterialColors.put(new ResourceLocation("warped_wart_block"), MaterialColor.WARPED_WART_BLOCK);
                MaterialColors.put(new ResourceLocation("deepslate"), MaterialColor.DEEPSLATE);
                MaterialColors.put(new ResourceLocation("raw_iron"), MaterialColor.RAW_IRON);
                MaterialColors.put(new ResourceLocation("glow_lichen"), MaterialColor.GLOW_LICHEN);
            });

    public static void registerMapColor(ResourceLocation name, MaterialColor mapColor) {
        MATERIAL_COLORS.putIfAbsent(name, mapColor);
    }

    @Override
    public Result<MaterialColor, JsonElement> deserialize(JsonElement input) {
        return JsonDeserializers.RESOURCE_LOCATION.deserialize(input)
                .map(MATERIAL_COLORS::get, "Could not get material color from \"{}\".");
    }
}
*///? }
