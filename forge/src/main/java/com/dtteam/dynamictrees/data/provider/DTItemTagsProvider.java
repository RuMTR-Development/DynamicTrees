package com.dtteam.dynamictrees.data.provider;

import com.dtteam.dynamictrees.DynamicTrees;
import com.dtteam.dynamictrees.data.tags.DTItemTags;
import com.dtteam.dynamictrees.tree.family.Family;
import com.dtteam.dynamictrees.tree.species.Species;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

//? if >= 1.19.4 {
/*import net.minecraft.data.PackOutput;
import net.minecraft.core.HolderLookup;
*///? }

/**
 * @author Harley O'Connor
 */
public class DTItemTagsProvider extends ItemTagsProvider {
    //? if >= 1.19.4 {
    /*public DTItemTagsProvider(PackOutput output, String modId, CompletableFuture<HolderLookup.Provider> lookupProvider,
                              CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper fileHelper) {
        super(output, lookupProvider, blockTags, modId, fileHelper);
    }
    *///? } else {
    public DTItemTagsProvider(DataGenerator generator, BlockTagsProvider blockTagsProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, blockTagsProvider, modId, existingFileHelper);
    }
    //? }

    @Override
    protected void addTags(
            //? if >= 1.19.4
            //HolderLookup.Provider provider
    ) {
        if (this.modId.equals(DynamicTrees.MOD_ID)) {
            this.addDTOnlyTags();
        }
        this.addDTTags();
    }

    private void addDTOnlyTags() {
        this.tag(DTItemTags.BRANCHES)
                .addTag(DTItemTags.BRANCHES_THAT_BURN)
                .addTag(DTItemTags.FUNGUS_BRANCHES);

        this.tag(DTItemTags.SEEDS)
                .addTag(DTItemTags.FUNGUS_CAPS);

        this.tag(ItemTags.SAPLINGS)
                .addTag(DTItemTags.SEEDS);

        this.tag(DTItemTags.ENHANCED_FERTILIZER)
                .addOptional(new ResourceLocation("create:tree_fertilizer"));

        this.tag(DTItemTags.FERTILIZER)
                .add(Items.BONE_MEAL)
                .addOptionalTag(new ResourceLocation("c:fertilizer"));
    }

    protected void addDTTags() {
        Family.REGISTRY.dataGenerationStream(this.modId).forEach(family ->
                family.addGeneratedItemTags(this::tag));

        Species.REGISTRY.dataGenerationStream(this.modId).forEach(species ->
                species.addGeneratedItemTags(this::tag));
    }

    @Override
    public String getName() {
        return modId + " DT Item Tags";
    }

}
