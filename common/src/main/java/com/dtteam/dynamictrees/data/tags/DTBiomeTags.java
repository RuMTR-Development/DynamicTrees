package com.dtteam.dynamictrees.data.tags;

import com.dtteam.dynamictrees.DynamicTrees;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

//? if >= 1.19.4 {
import net.minecraft.core.registries.Registries;
//? }

public class DTBiomeTags {

    public static final TagKey<Biome> IS_BEE_NEST_COMMON = bind("is_bee_nest_common");
    public static final TagKey<Biome> IS_BEE_NEST_UNCOMMON = bind("is_bee_nest_uncommon");
    public static final TagKey<Biome> IS_BEE_NEST_RARE = bind("is_bee_nest_rare");
    public static final TagKey<Biome> IS_BEE_NEST_GUARANTEED = bind("is_bee_nest_guaranteed");

    private static TagKey<Biome> bind(String identifier) {
        //~ if < 1.19.4 'Registries.BIOME' -> 'Registry.BIOME_REGISTRY'
        return TagKey.create(Registries.BIOME, DynamicTrees.location(identifier));
    }

}
