package com.dtteam.dynamictrees.data.provider;

import com.dtteam.dynamictrees.DynamicTrees;
import com.dtteam.dynamictrees.recipe.MegaSeedRecipe;
import com.dtteam.dynamictrees.registry.DTRegistries;
import com.dtteam.dynamictrees.tree.species.Species;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

//? if >= 1.19.4 {
import net.minecraft.data.PackOutput;
//? } else {
/*import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
*///? }

public class DTRecipeProvider extends RecipeProvider {
    private static final ResourceLocation COCOA = DynamicTrees.location("cocoa");

    //? if >= 1.19.4 {
    /*public DTRecipeProvider(PackOutput output) {
        super(output);
    }*///? } else {
    public DTRecipeProvider(DataGenerator generator) {
        super(generator);
    }
    //? }

    @Override
    //~ if < 1.19.4 'buildRecipes' -> 'buildCraftingRecipes'
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> writer) {
        ShapelessRecipeBuilder.shapeless(
                        //? if >= 1.19.4
                        RecipeCategory.MISC,

                        Items.COCOA_BEANS,
                        3
        )
                .requires(Species.findSpecies(COCOA).getSeed().orElseThrow())
                .unlockedBy("impossible", new ImpossibleTrigger.TriggerInstance())
                .save(writer, DynamicTrees.location("cocoa_beans"));

        ShapedRecipeBuilder.shaped(
                        //? if >= 1.19.4
                        RecipeCategory.MISC,

                        DTRegistries.DIRT_BUCKET.get()
        )
                .define('#', Items.DIRT)
                .define('U', Items.BUCKET)
                .pattern("#")
                .pattern("U")
                .unlockedBy("impossible", new ImpossibleTrigger.TriggerInstance())
                .save(writer, DynamicTrees.location("dirt_bucket"));

        SpecialRecipeBuilder.special(
                //? if < 1.19.4
                //(SimpleRecipeSerializer<?>)

                DTRegistries.MEGA_SEED_RECIPE_TYPE.get()
        )
                .save(writer, DynamicTrees.location("mega_seed").toString());

        SpecialRecipeBuilder.special(
                        //? if < 1.19.4
                        //(SimpleRecipeSerializer<?>)

                        DTRegistries.SEED_CONVERSION_RECIPE_TYPE.get()
        )
                .save(writer, DynamicTrees.location("seed_conversion").toString());
    }
}
