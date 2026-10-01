package com.dtteam.dynamictrees.loot.entry;

import com.dtteam.dynamictrees.loot.function.MultiplyByLogsCount;
import com.dtteam.dynamictrees.registry.DTRegistries;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;
import java.util.function.Consumer;

//? if >= 1.19.4 {
import net.minecraft.core.registries.BuiltInRegistries;
 //? }

/**
 * @author Harley O'Connor
 */
public final class WeightedItemLootPoolEntry extends LootPoolSingletonContainer {
    //? if >= 1.21.1 {
    public static final MapCodec<WeightedItemLootPoolEntry> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance
                    .group(SimpleWeightedRandomList.codec(WeightedEntry.Wrapper.codec(BuiltInRegistries.ITEM.holderByNameCodec())).fieldOf("items").forGetter(c->c.items))
                    .and(singletonFields(instance))
                    .apply(instance, WeightedItemLootPoolEntry::new));
    //? } else {
    /*public static class Serializer extends LootPoolSingletonContainer.Serializer<WeightedItemLootPoolEntry> {
        //~ if < 1.19.4 'BuiltInRegistries' -> 'Registry'
        private static final Codec<WeightedRandomList<WeightedEntry.Wrapper<Holder<Item>>>> ITEMS_CODEC = SimpleWeightedRandomList.codec(WeightedEntry.Wrapper.codec(BuiltInRegistries.ITEM.holderByNameCodec()));

        public Serializer() {
        }

        @Override
        protected WeightedItemLootPoolEntry deserialize(JsonObject object, JsonDeserializationContext context, int weight, int quality, LootItemCondition[] conditions, LootItemFunction[] functions) {
            WeightedRandomList<WeightedEntry.Wrapper<Holder<Item>>> items = ITEMS_CODEC.parse(JsonOps.INSTANCE, object.get("items"))
                    .resultOrPartial(error -> {
                        throw new IllegalArgumentException("Failed to decode items list: " + error);
                    })
                    .orElseGet(SimpleWeightedRandomList::empty);

            return new WeightedItemLootPoolEntry(items, weight, quality, conditions, functions);
        }

        @Override
        public void serializeCustom(JsonObject object, WeightedItemLootPoolEntry context, JsonSerializationContext conditions) {
            super.serializeCustom(object, context, conditions);

            ITEMS_CODEC.encodeStart(JsonOps.INSTANCE, context.items)
                    .resultOrPartial(error -> {
                        throw new IllegalArgumentException("Failed to encode items list: " + error);
                    })
                    .ifPresent(out -> object.add("items", out));
        }
    }
    *///? }

    private final WeightedRandomList<WeightedEntry.Wrapper<Holder<Item>>> items;

    //? if >= 1.21.1 {
    public WeightedItemLootPoolEntry(WeightedRandomList<WeightedEntry.Wrapper<Holder<Item>>> items, int weight, int quality, List<LootItemCondition> conditions,
                                     List<LootItemFunction> functions) {
        super(weight, quality, conditions, functions);
        this.items = items;
    }
    //? } else {
    /*public WeightedItemLootPoolEntry(WeightedRandomList<WeightedEntry.Wrapper<Holder<Item>>> items, int weight, int quality, LootItemCondition[] conditions,
                                     LootItemFunction[] functions) {
        super(weight, quality, conditions, functions);
        this.items = items;
    }
    *///? }

    @Override
    public LootPoolEntryType getType() {
        return DTRegistries.WEIGHTED_ITEM.get();
    }

    @Override
    protected void createItemStack(Consumer<ItemStack> stackConsumer, LootContext lootContext) {
        //? if >= 1.21.1 {
        items.getRandom(lootContext.getRandom()).ifPresent(wrapper -> stackConsumer.accept(new ItemStack(wrapper.data().value())));
        //? } else {
        /*items.getRandom(lootContext.getRandom()).ifPresent(wrapper -> stackConsumer.accept(new ItemStack(wrapper.getData().value())));
        *///? }
    }


}
