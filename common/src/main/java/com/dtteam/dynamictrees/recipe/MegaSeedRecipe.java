package com.dtteam.dynamictrees.recipe;

import com.dtteam.dynamictrees.config.DTConfigs;
import com.dtteam.dynamictrees.item.Seed;
import com.dtteam.dynamictrees.registry.DTRegistries;
import com.dtteam.dynamictrees.tree.species.Species;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

//? if >= 1.21.1 {
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.core.HolderLookup;
//? } else {
/*import net.minecraft.core.RegistryAccess;
*///? }

//? if >= 1.19.4 {
import net.minecraft.world.item.crafting.CraftingBookCategory;
//? }

public class MegaSeedRecipe extends CustomRecipe {
    //? if >= 1.21.1 {
    public MegaSeedRecipe(CraftingBookCategory pCategory) {
        super(pCategory);
    }
    //? } else if >= 1.19.4 {
    /*public MegaSeedRecipe(ResourceLocation pId, CraftingBookCategory pCategory) {
        super(pId, pCategory);
    }
    *///? } else {
    /*public MegaSeedRecipe(ResourceLocation pId) {
        super(pId);
    }
    *///? }

    @Override
    public boolean matches(
            //? if >= 1.21.1 {
            CraftingInput craftingInput,
            //? } else {
            /*CraftingContainer craftingInput,
            *///? }

            Level level
    ) {
        if(DTConfigs.COMMON.generateMegaSeedRecipe.get() && atLeastHasSeed(craftingInput)){
            for(Species species : Species.REGISTRY) {
                if(recipeMatchCondition(craftingInput, species)) {
                    return nonEmptyStacksStream(craftingInput).count() == 4;
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
        for (Species species : Species.REGISTRY) {
            if (recipeMatchCondition(craftingInput, species)) {
                return new ItemStack(species.getSeed().get());
            }
        }
        return ItemStack.EMPTY;
    }

    private static boolean atLeastHasSeed(
            //? if >= 1.21.1 {
            CraftingInput craftingInput
             //? } else {
            /*CraftingContainer craftingInput
            *///? }
    ) {
        //? if >=1.21 {
        return craftingInput.items().stream().anyMatch(s -> !s.isEmpty() && s.getItem() instanceof Seed);
        //? } else if >= 1.19.2 {
        /*return craftingInput.hasAnyMatching(s -> !s.isEmpty() && s.getItem() instanceof Seed);
        *///? } else {
        /*return nonEmptyStacksStream(craftingInput).anyMatch(s -> s.getItem() instanceof Seed);
        *///? }
    }

    private static boolean recipeMatchCondition(
            //? if >= 1.21 {
            CraftingInput craftingInput,
             //? } else {
            /*CraftingContainer craftingInput,
            *///? }

            Species species
    ) {
        return species.isMegaSpecies() && species.hasSeed()
                && species.getPreMegaSpecies().canCraftMegaSeed()
                && allItemsMatchSeed(craftingInput, species.getPreMegaSpecies());
    }

    private static boolean allItemsMatchSeed(
            //? if >= 1.21 {
            CraftingInput craftingInput,
             //? } else {
            /*CraftingContainer craftingInput,
            *///? }

            Species species
    ) {
        return nonEmptyStacksStream(craftingInput).allMatch(stack -> stack.is(species.getSeed().get()));
    }

    //? if >= 1.21 {
    private static Stream<ItemStack> nonEmptyStacksStream(CraftingInput craftingInput) {
        return craftingInput.items().stream().filter(stack -> !stack.isEmpty());
    }
    //? } else {
    /*private static Stream<ItemStack> nonEmptyStacksStream(CraftingContainer craftingInput) {
        ItemStack[] stacks = new ItemStack[craftingInput.getContainerSize()];

        for (int i = 0; i < stacks.length; i++) {
            stacks[i] = craftingInput.getItem(i);
        }

        return Arrays.stream(stacks).filter(stack -> !stack.isEmpty());
    }
    *///? }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return DTRegistries.MEGA_SEED_RECIPE_TYPE.get();
    }

}