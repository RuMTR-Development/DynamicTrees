package com.dtteam.dynamictrees.recipe;

import com.dtteam.dynamictrees.config.DTConfigs;
import com.dtteam.dynamictrees.registry.DTRegistries;
import com.dtteam.dynamictrees.systems.SeedSaplingRecipe;
import com.dtteam.dynamictrees.tree.species.Species;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

//? if >= 1.21.1 {
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.core.HolderLookup;
//? } else {
/*import net.minecraft.world.inventory.CraftingContainer;
*///? }

//? if >= 1.19.4 {
import net.minecraft.world.item.crafting.CraftingBookCategory;
//? }

public class SeedConversionRecipe extends CustomRecipe {
    //? if >= 1.21.1 {
    public SeedConversionRecipe(CraftingBookCategory pCategory) {
        super(pCategory);
    }
    //? } else if >= 1.19.4 {
    /*public SeedConversionRecipe(ResourceLocation pId, CraftingBookCategory pCategory) {
        super(pId, pCategory);
    }
    *///? } else {
    /*public SeedConversionRecipe(ResourceLocation pId) {
        super(pId);
    }
    *///? }

    @Override
    public boolean matches(
            //? if >= 1.21 {
            CraftingInput craftingInput,
             //? } else {
            /*CraftingContainer craftingInput,
            *///? }

            Level level
    ) {
        if(DTConfigs.COMMON.generateDirtBucketRecipes.get() && hasDirtBucket(craftingInput)) {
            for (Species species : Species.REGISTRY) {
                for (SeedSaplingRecipe recipe : species.getPrimitiveSaplingRecipes()) {
                    if (!recipe.canCraftSaplingToSeed() && !recipe.canCraftSeedToSapling()) {
                        return false;
                    }
                    if (saplingToSeedCondition(craftingInput, recipe)) {
                        return hasExactCount(craftingInput, recipe.getIngredientsForSaplingToSeed());
                    }
                    if (seedToSaplingCondition(craftingInput, species, recipe)) {
                        return hasExactCount(craftingInput, recipe.getIngredientsForSeedToSapling());
                    }
                }
            }
        }
        return false;
    }

    @Override
    public ItemStack assemble(
            //? if >= 1.21.1 {
            CraftingInput craftingInput,
            HolderLookup.Provider registryAccess
             //? } else if >= 1.19.4 {
            /*CraftingContainer craftingInput,
            RegistryAccess registryAccess
            *///? } else {
            /*CraftingContainer craftingInput
            *///? }
    ) {
        for(Species species : Species.REGISTRY) {
            for (SeedSaplingRecipe recipe : species.getPrimitiveSaplingRecipes()) {
                if (saplingToSeedCondition(craftingInput, recipe)) {
                    return species.getSeed().get().getDefaultInstance();
                }
                if (seedToSaplingCondition(craftingInput, species, recipe)) {
                    return  recipe.getSaplingItem().get().getDefaultInstance();
                }
            }
        }
        return ItemStack.EMPTY;
    }

    private static boolean seedToSaplingCondition(
            //? if >= 1.21 {
            CraftingInput craftingInput,
             //? } else {
            /*CraftingContainer craftingInput,
            *///? }
    Species species, SeedSaplingRecipe recipe) {
        return recipe.canCraftSeedToSapling()
                && containsOneOfItem(craftingInput, species.getSeed().get())
                && containsAllIngredients(craftingInput, recipe.getIngredientsForSeedToSapling());
    }

    private static boolean saplingToSeedCondition(
            //? if >= 1.21 {
            CraftingInput craftingInput,
             //? } else {
            /*CraftingContainer craftingInput,
            *///? }
            SeedSaplingRecipe recipe) {
        return recipe.canCraftSaplingToSeed()
                && containsOneOfItem(craftingInput, recipe.getSaplingItem().get())
                && containsAllIngredients(craftingInput, recipe.getIngredientsForSaplingToSeed());
    }

    private static boolean hasExactCount(
            //? if >= 1.21 {
            CraftingInput craftingInput,
             //? } else {
            /*CraftingContainer craftingInput,
            *///? }
            List<Item> ingredients) {
        return notEmptyInput(craftingInput).count() == ingredients.size() + 2;
    }

    private static boolean containsAllIngredients(
            //? if >= 1.21 {
            CraftingInput craftingInput,
             //? } else {
            /*CraftingContainer craftingInput,
            *///? }
            List<Item> ingredients) {
        return new HashSet<>(notEmptyInput(craftingInput).map(ItemStack::getItem).toList()).containsAll(ingredients);
    }

    private static boolean containsOneOfItem(
            //? if >= 1.21 {
            CraftingInput craftingInput,
             //? } else {
            /*CraftingContainer craftingInput,
            *///? }
            Item item) {
        return stacksStream(craftingInput).filter(stack -> stack.is(item)).count() == 1;
    }

    private static boolean hasDirtBucket(
            //? if >= 1.21 {
            CraftingInput craftingInput
             //? } else {
            /*CraftingContainer craftingInput
            *///? }
    ) {
        return stacksStream(craftingInput).anyMatch(itemStack -> itemStack.is(DTRegistries.DIRT_BUCKET.get()));
    }

    private static Stream<ItemStack> notEmptyInput(
            //? if >= 1.21 {
            CraftingInput craftingInput
             //? } else {
            /*CraftingContainer craftingInput
            *///? }
    ) {
        return stacksStream(craftingInput).filter(s -> !s.isEmpty());
    }

    //? if >= 1.21 {
    private static Stream<ItemStack> stacksStream(CraftingInput craftingInput) {
        return craftingInput.items().stream();
    }
    //? } else {
    /*private static Stream<ItemStack> stacksStream(CraftingContainer craftingInput) {
        ItemStack[] stacks = new ItemStack[craftingInput.getContainerSize()];

        for (int i = 0; i < stacks.length; i++) {
            stacks[i] = craftingInput.getItem(i);
        }

        return Arrays.stream(stacks);
    }
    *///? }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return DTRegistries.SEED_CONVERSION_RECIPE_TYPE.get();
    }

}
