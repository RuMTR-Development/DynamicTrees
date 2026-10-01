package com.dtteam.dynamictrees.data.tags;

import com.dtteam.dynamictrees.DynamicTrees;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

//? if >= 1.19.4 {
import net.minecraft.core.registries.Registries;
//? }

/**
 * @author Harley O'Connor
 */
public final class DTEntityTypeTags {

    public static final TagKey<EntityType<?>> CAN_PASS_THROUGH_LEAVES = bind("can_pass_through_leaves");
    public static final TagKey<EntityType<?>> FALLING_TREE_DAMAGE_IMMUNE = bind("falling_tree_damage_immune");

    private static TagKey<EntityType<?>> bind(String identifier) {
        //~ if < 1.19.4 'Registries.ENTITY_TYPE' -> 'Registry.ENTITY_TYPE_REGISTRY'
        return TagKey.create(Registries.ENTITY_TYPE, DynamicTrees.location(identifier));
    }
}
