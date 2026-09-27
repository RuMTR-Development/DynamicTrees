package com.dtteam.dynamictrees.loot.entry;

import com.dtteam.dynamictrees.loot.DTLootContextParams;
import com.dtteam.dynamictrees.registry.DTRegistries;
import com.dtteam.dynamictrees.tree.species.Species;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author Harley O'Connor
 */
public final class SeedItemLootPoolEntry extends LootPoolSingletonContainer {
    //? if >= 1.21 {
    public static final MapCodec<SeedItemLootPoolEntry> CODEC = RecordCodecBuilder.mapCodec(
            instance -> singletonFields(instance)
                    .apply(instance, SeedItemLootPoolEntry::new));


    public SeedItemLootPoolEntry(int weight, int quality, List<LootItemCondition> conditions,
                                 List<LootItemFunction> functions) {
        super(weight, quality, conditions, functions);
    }
    //? } else {
    /*public static class Serializer extends LootPoolSingletonContainer.Serializer<SeedItemLootPoolEntry> {
        public Serializer() {}

        @Override
        protected SeedItemLootPoolEntry deserialize(JsonObject object, JsonDeserializationContext context, int weight, int quality, LootItemCondition[] conditions, LootItemFunction[] functions) {
            return new SeedItemLootPoolEntry(weight, quality, conditions, functions);
        }

        @Override
        public void serializeCustom(JsonObject object, SeedItemLootPoolEntry context, JsonSerializationContext conditions) {
            super.serializeCustom(object, context, conditions);
        }
    }

    public SeedItemLootPoolEntry(int weight, int quality, LootItemCondition[] conditions, LootItemFunction[] functions) {
        super(weight, quality, conditions, functions);
    }
    *///? }

    @Override
    public LootPoolEntryType getType() {
        return DTRegistries.SEED_ITEM.get();
    }

    @Override
    protected void createItemStack(Consumer<ItemStack> stackConsumer, LootContext context) {
        final Species species = context.getParamOrNull(DTLootContextParams.SPECIES);
        assert species != null;
        stackConsumer.accept(species.shouldDropSeeds() ? species.getSeedStack(1) : ItemStack.EMPTY);
    }

    public static LootPoolSingletonContainer.Builder<?> lootTableSeedItem() {
        return simpleBuilder(SeedItemLootPoolEntry::new);
    }

}
