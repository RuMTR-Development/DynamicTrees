package com.dtteam.dynamictrees.data.provider;

import com.dtteam.dynamictrees.DynamicTrees;
import com.dtteam.dynamictrees.recipe.MegaSeedRecipe;
import com.dtteam.dynamictrees.recipe.SeedConversionRecipe;
import com.dtteam.dynamictrees.registry.DTRegistries;
import com.dtteam.dynamictrees.tree.species.Species;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class DTRecipeProvider extends RecipeProvider {
    private static final ResourceLocation COCOA = DynamicTrees.location("cocoa");

    public DTRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput writer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.COCOA_BEANS, 3)
                .requires(Species.findSpecies(COCOA).getSeed().orElseThrow())
                .unlockedBy("impossible", new Criterion<>(CriteriaTriggers.IMPOSSIBLE, new ImpossibleTrigger.TriggerInstance()))
                .save(writer, DynamicTrees.location("cocoa_beans"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, DTRegistries.DIRT_BUCKET.get())
                .define('#', Items.DIRT)
                .define('U', Items.BUCKET)
                .pattern("#")
                .pattern("U")
                .unlockedBy("impossible", new Criterion<>(CriteriaTriggers.IMPOSSIBLE, new ImpossibleTrigger.TriggerInstance()))
                .save(writer, DynamicTrees.location("dirt_bucket"));

        SpecialRecipeBuilder.special(MegaSeedRecipe::new)
                .save(writer, DynamicTrees.location("mega_seed").toString());

        SpecialRecipeBuilder.special(SeedConversionRecipe::new)
                .save(writer, DynamicTrees.location("seed_conversion").toString());
    }
}
