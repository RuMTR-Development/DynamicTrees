package com.dtteam.dynamictrees.entity.animation;

import com.dtteam.dynamictrees.DynamicTrees;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;

//? if >= 1.19.4 {
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageType;
//? }

public class AnimationConstants {
    public static final float TREE_GRAVITY = 0.03f;
    public static final float TREE_ELASTICITY = 0.25f;

    //? if >= 1.19.4 {
    public static final ResourceKey<DamageType> TREE_DAMAGE_TYPE = ResourceKey.create(Registries.DAMAGE_TYPE, DynamicTrees.location("falling_tree"));
    //? } else {
    /*public static class FallingTreeDamageSource extends DamageSource {
        protected FallingTreeDamageSource() {
            super("falling_tree");
        }
    }
    *///? }

    public static DamageSource treeDamage(RegistryAccess registryAccess) {
        //? if >= 1.19.4 {
        return new DamageSource(registryAccess.registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(TREE_DAMAGE_TYPE));
        //? } else {
        /*return new FallingTreeDamageSource();
        *///? }
    }
}
