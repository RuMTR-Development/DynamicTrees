package com.dtteam.dynamictrees.deserialization.deserializer;

import com.dtteam.dynamictrees.deserialization.JsonMapWrapper;
import com.dtteam.dynamictrees.deserialization.JsonPropertyAppliers;
import com.dtteam.dynamictrees.deserialization.applier.Applier;
import com.dtteam.dynamictrees.deserialization.applier.PropertyApplierResult;
import com.dtteam.dynamictrees.deserialization.applier.VoidApplier;
import com.dtteam.dynamictrees.deserialization.result.JsonResult;
import com.dtteam.dynamictrees.deserialization.result.Result;
import com.dtteam.dynamictrees.platform.Services;
import com.dtteam.dynamictrees.worldgen.IDTBiomeHolderSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.ResourceLocationException;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import org.apache.logging.log4j.LogManager;

import java.util.*;
import java.util.function.Predicate;import java.util.function.Supplier;import java.util.stream.Collectors;

//? if >= 1.19.4 {
import net.minecraft.core.registries.Registries;
//? }

/**
 * @author Harley O'Connor
 */
public final class BiomeListDeserializer implements JsonDeserializer<IDTBiomeHolderSet> {

    public static final Supplier<Registry<Biome>> DELAYED_BIOME_REGISTRY = () -> {
        MinecraftServer currentServer = Services.MISC.getCurrentServer();
        if (currentServer == null) {
            //? if >=1.19.4 {
            throw new IllegalStateException("Queried biome registry too early; server does not exist yet!");
            //?} else {
            /*return BuiltinRegistries.BIOME;
            *///?}
        }

        //~ if < 1.19.4 'Registries.BIOME' -> 'Registry.BIOME_REGISTRY'
        return currentServer.registryAccess().registryOrThrow(Registries.BIOME);
    };

    //? if < 1.19.2
    //private static HolderSet<Biome> legacySubstituteTag(ResourceLocation tagLocation) {
        Predicate<Biome.BiomeCategory> biomeFilter;

        switch (tagLocation.toString()) {
            case "minecraft:is_overworld" -> biomeFilter = category -> category != Biome.BiomeCategory.NETHER && category != Biome.BiomeCategory.THEEND;
            case "minecraft:is_nether" -> biomeFilter = category -> category == Biome.BiomeCategory.NETHER;
            case "minecraft:is_end" -> biomeFilter = category -> category == Biome.BiomeCategory.THEEND;
            case "minecraft:is_river" -> biomeFilter = category -> category == Biome.BiomeCategory.RIVER;
            case "minecraft:is_ocean" -> biomeFilter = category -> category == Biome.BiomeCategory.OCEAN;
            case "minecraft:is_beach" -> biomeFilter = category -> category == Biome.BiomeCategory.BEACH;
            case "minecraft:is_forest" -> biomeFilter = category -> category == Biome.BiomeCategory.FOREST;
            case "c:is_swamp" -> biomeFilter = category -> category == Biome.BiomeCategory.SWAMP;
            case "minecraft:is_savanna" -> biomeFilter = category -> category == Biome.BiomeCategory.SAVANNA;
            case "minecraft:is_jungle" -> biomeFilter = category -> category == Biome.BiomeCategory.JUNGLE;
            case "c:is_plains" -> biomeFilter = category -> category == Biome.BiomeCategory.PLAINS;
            case "c:is_tree/coniferous", "c:is_taiga", "c:tree_coniferous" -> biomeFilter = category -> category == Biome.BiomeCategory.TAIGA;
            default -> biomeFilter = category -> false;
        }

        return HolderSet.direct(DELAYED_BIOME_REGISTRY.get().holders().filter(biome -> {
            var category = Biome.getBiomeCategory(biome);
            return biomeFilter.test(category);
        }).collect(Collectors.toList()));
    }

    private static final Applier<IDTBiomeHolderSet, String> TAG_APPLIER = (biomeList, tagRegex) -> {
        tagRegex = tagRegex.toLowerCase(Locale.ENGLISH);
        final boolean notOperator = usingNotOperator(tagRegex);
        if (notOperator)
            tagRegex = tagRegex.substring(1);
        //We gotta get rid of those #, they don't matter for the regex.
        tagRegex = tagRegex.replaceAll("#","");
        try {
            //? if >= 1.21 {
            ResourceLocation tagLocation = ResourceLocation.parse(tagRegex);
            //? } else {
            /*ResourceLocation tagLocation = new ResourceLocation(tagRegex);
            *///? }

            //? if >= 1.19.2 {
            //~ if < 1.19.4 'Registries.BIOME' -> 'Registry.BIOME_REGISTRY'
            TagKey<Biome> tagKey = TagKey.create(Registries.BIOME, tagLocation);

            // TODO UPDATE: This is used as a regex in 1.19.2. Double check!!!
            biomeList.addDelayedHolderSet(
                    (notOperator ? biomeList.getExcludeComponents() : biomeList.getIncludeComponents()),
                    () -> DELAYED_BIOME_REGISTRY.get().getOrCreateTag(tagKey));
            //? } else {
            /*biomeList.addDelayedHolderSet(
                    (notOperator ? biomeList.getExcludeComponents() : biomeList.getIncludeComponents()),
                    () -> legacySubstituteTag(tagLocation));
            *///? }
        } catch (ResourceLocationException e) {
            return PropertyApplierResult.failure(e.getMessage());
        }

        return PropertyApplierResult.success();
    };

    private static final VoidApplier<IDTBiomeHolderSet, String> NAME_APPLIER = (biomeList, nameRegex) -> {
        nameRegex = nameRegex.toLowerCase(Locale.ENGLISH);
        final boolean notOperator = usingNotOperator(nameRegex);
        if (notOperator)
            nameRegex = nameRegex.substring(1);

        String finalNameRegex = nameRegex;
        biomeList.addNameRegexMatch(
                (notOperator ? biomeList.getExcludeComponents() : biomeList.getIncludeComponents()),
                DELAYED_BIOME_REGISTRY, finalNameRegex);
    };

    private static boolean usingNotOperator(String categoryString) {
        return categoryString.charAt(0) == '!';
    }

    private static final VoidApplier<IDTBiomeHolderSet, JsonArray> NAMES_OR_APPLIER = (biomeList, json) -> {
        final List<String> nameRegexes = JsonResult.forInput(json)
                .mapEachIfArray(String.class, (Result.SimpleMapper<String, String>) String::toLowerCase)
                .orElse(Collections.emptyList(), LogManager.getLogger()::error, LogManager.getLogger()::warn);

        List<HolderSet<Biome>> orIncludes = new ArrayList<>();
        List<HolderSet<Biome>> orExcludes = new ArrayList<>();
        nameRegexes.forEach(nameRegex -> {
            nameRegex = nameRegex.toLowerCase(Locale.ENGLISH);
            final boolean notOperator = usingNotOperator(nameRegex);
            if (notOperator)
                nameRegex = nameRegex.substring(1);

            String finalNameRegex = nameRegex;
            biomeList.addNameRegexMatch(
                    (notOperator ? orExcludes : orIncludes),
                    DELAYED_BIOME_REGISTRY, finalNameRegex);
        });

        if (!orIncludes.isEmpty())
            biomeList.addOr(biomeList.getIncludeComponents(), orIncludes);
        if (!orExcludes.isEmpty())
            biomeList.addOr(biomeList.getExcludeComponents(), orExcludes);
    };

    private static final VoidApplier<IDTBiomeHolderSet, JsonArray> TAGS_OR_APPLIER = (biomeList, json) -> {
        final List<String> nameRegexes = JsonResult.forInput(json)
                .mapEachIfArray(String.class, (Result.SimpleMapper<String, String>) String::toLowerCase)
                .orElse(Collections.emptyList(), LogManager.getLogger()::error, LogManager.getLogger()::warn);

        List<HolderSet<Biome>> orIncludes = new ArrayList<>();
        List<HolderSet<Biome>> orExcludes = new ArrayList<>();
        nameRegexes.forEach(tagRegex -> {
            tagRegex = tagRegex.toLowerCase(Locale.ENGLISH);
            final boolean notOperator = usingNotOperator(tagRegex);
            if (notOperator)
                tagRegex = tagRegex.substring(1);
            if (tagRegex.charAt(0) == '#')
                tagRegex = tagRegex.substring(1);

            //? if >= 1.19.2 {
            biomeList.addTagsRegexMatch(
                    (notOperator ? orExcludes : orIncludes),
                    DELAYED_BIOME_REGISTRY, tagRegex);

            //? } else {
            /*ResourceLocation tagLocation = ResourceLocation.tryParse(tagRegex);

            if (tagLocation != null) {
                biomeList.addDelayedHolderSet(
                        (notOperator ? orExcludes : orIncludes),
                        () -> legacySubstituteTag(tagLocation));
            }
            *///? }
        });

        if (!orIncludes.isEmpty())
            biomeList.addOr(biomeList.getIncludeComponents(), orIncludes);
        if (!orExcludes.isEmpty())
            biomeList.addOr(biomeList.getExcludeComponents(), orExcludes);
    };

    private final VoidApplier<IDTBiomeHolderSet, JsonObject> andOperator =
            (biomes, jsonObject) -> applyAllAppliers(jsonObject, biomes);

    private final VoidApplier<IDTBiomeHolderSet, JsonArray> orOperator = (biomeList, json) -> {
        List<HolderSet<Biome>> appliedList = new LinkedList<>();

        JsonResult.forInput(json)
                .mapEachIfArray(JsonObject.class, object -> {
                    IDTBiomeHolderSet subList = Services.MISC.newDTBiomeHolderSet();
                    applyAllAppliers(object, subList);
                    appliedList.add(subList);
                    return object;
                })
                .orElse(null, LogManager.getLogger()::error, LogManager.getLogger()::warn);

        if (!appliedList.isEmpty())
            biomeList.addOr(biomeList.getIncludeComponents(), appliedList);
    };

    private final VoidApplier<IDTBiomeHolderSet, JsonObject> notOperator = (biomeList, jsonObject) -> {
        final IDTBiomeHolderSet notBiomeList = Services.MISC.newDTBiomeHolderSet();
        applyAllAppliers(jsonObject, notBiomeList);
        biomeList.getExcludeComponents().add(notBiomeList);
    };

    private final JsonPropertyAppliers<IDTBiomeHolderSet> appliers = new JsonPropertyAppliers<>(IDTBiomeHolderSet.class);

    public BiomeListDeserializer() {
        registerAppliers();
    }

    private void registerAppliers() {
        this.appliers
                .register("tag", String.class, TAG_APPLIER)
                .registerArrayApplier("tags", String.class, TAG_APPLIER)
                .register("tags_or", JsonArray.class, TAGS_OR_APPLIER)
                .register("name", String.class, NAME_APPLIER)
                .registerArrayApplier("names", String.class, NAME_APPLIER)
                .register("names_or", JsonArray.class, NAMES_OR_APPLIER)
                .registerArrayApplier("AND", JsonObject.class, andOperator)
                .register("OR", JsonArray.class, orOperator)
                .register("NOT", JsonObject.class, notOperator);
    }

    private void applyAllAppliers(JsonObject json, IDTBiomeHolderSet biomes) {
        appliers.applyAll(new JsonMapWrapper(json), biomes);
    }

    @Override
    public Result<IDTBiomeHolderSet, JsonElement> deserialize(final JsonElement input) {
        return JsonResult.forInput(input)
                .mapIfType(String.class, biomeName -> {
                    IDTBiomeHolderSet biomes = Services.MISC.newDTBiomeHolderSet();
                    biomes.addNameRegexMatch(biomes.getIncludeComponents(), DELAYED_BIOME_REGISTRY, biomeName.toLowerCase(Locale.ENGLISH));
                    return biomes;
                })
                .elseMapIfType(JsonObject.class, selectorObject -> {
                    final IDTBiomeHolderSet biomes = Services.MISC.newDTBiomeHolderSet();
                    // Apply from all appliers
                    applyAllAppliers(selectorObject, biomes);
                    return biomes;
                }).elseTypeError();
    }

}