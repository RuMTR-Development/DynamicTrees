package com.dtteam.dynamictrees.data.provider;

import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.Map;import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

//? if >= 1.19.4 {
/*import net.minecraft.data.PackOutput;
import net.minecraft.core.HolderLookup;
*///? } else {
import net.minecraft.world.level.storage.loot.LootTables;
//? }

/**
 * @author Harley O'Connor
 */
public class DTLootTableProvider extends LootTableProvider {
    //? if >= 1.19.4 {
    /*public DTLootTableProvider(PackOutput output, String modId, ExistingFileHelper fileHelper) {
        super(output, Set.of(),
                List.of(new SubProviderEntry(()->new DTBlockLootSubProvider(modId, fileHelper), LootContextParamSets.BLOCK)));
    }
    *///? } else {
    private final String modId;
    private final ExistingFileHelper fileHelper;

    public DTLootTableProvider(DataGenerator generator, String modId, ExistingFileHelper fileHelper) {
        super(generator);

        this.modId = modId;
        this.fileHelper = fileHelper;
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        return List.of(
                Pair.of(() -> new DTBlockLootSubProvider(this.modId, this.fileHelper), LootContextParamSets.BLOCK)
        );
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> tables, ValidationContext ctx) {
        tables.forEach((name, table) -> LootTables.validate(ctx, name, table));
    }
    //? }
}
