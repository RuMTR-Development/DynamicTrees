package com.dtteam.dynamictrees.recipe;

import com.dtteam.dynamictrees.config.DTConfigs;
import com.dtteam.dynamictrees.item.DendroPotion;
import com.dtteam.dynamictrees.registry.DTRegistries;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//? if >= 1.19.4 {
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
//? } else {
/*import net.minecraftforge.registries.ForgeRegistries;
*///? }

public class DendroPotionRecipeHandler {

    private static final List<DendroBrewingRecipe> brewingRecipes = new ArrayList<>();

    public static List<DendroBrewingRecipe> getAllDendroRecipes() {
        if (!brewingRecipes.isEmpty()) return brewingRecipes;

        final ItemStack baseStack = setPotion(new ItemStack(Items.POTION), DTConfigs.COMMON.biocharBrewingBase.get());
        brewingRecipes.add(getRecipe(baseStack, new ItemStack(Items.CHARCOAL), getPotionStack(DendroPotion.DendroPotionType.BIOCHAR)));

        //Regular potions
        for (int i = 1; i < DendroPotion.DendroPotionType.values().length; i++) {
            final DendroPotion.DendroPotionType type = DendroPotion.DendroPotionType.values()[i];

            if (!type.isActive()) continue;

            brewingRecipes.add(getRecipe(type.getIngredient(), type));
        }

        return brewingRecipes;
    }

    public static ItemStack setPotion(ItemStack pStack, String potionName) {
        //? if >= 1.19.4 {
        Optional<Holder.Reference<Potion>> potion = BuiltInRegistries.POTION.getHolder(ResourceKey.create(Registries.POTION, ResourceLocation.parse(potionName)));

        potion.ifPresent(holder -> PotionUtils.setPotion(pStack, holder.value()));
        //? } else {
        /*Potion potion = ForgeRegistries.POTIONS.getValue(ResourceLocation.parse(potionName));

        if (potion != null) {
            PotionUtils.setPotion(pStack, potion);
        }
        *///? }

        return pStack;
    }

    private static DendroBrewingRecipe getRecipe(ItemStack ingredient, DendroPotion.DendroPotionType typeOut) {
        return getRecipe(getPotionStack(typeOut.getBasePotionType()), ingredient, getPotionStack(typeOut));
    }

    private static DendroBrewingRecipe getRecipe(ItemStack stackIn, ItemStack ingredientStack, ItemStack stackOut) {
        return new DendroBrewingRecipe(stackIn, ingredientStack, stackOut);
    }

    private static ItemStack getPotionStack(DendroPotion.DendroPotionType type) {
        return DendroPotion.applyIndexTag(new ItemStack(DTRegistries.DENDRO_POTION.get()), type.getIndex());
    }

}
