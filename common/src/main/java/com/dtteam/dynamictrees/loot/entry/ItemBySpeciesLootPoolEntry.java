package com.dtteam.dynamictrees.loot.entry;

import com.dtteam.dynamictrees.loot.DTLootContextParams;
import com.dtteam.dynamictrees.registry.DTRegistries;
import com.dtteam.dynamictrees.tree.species.Species;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

//? if >= 1.19.4 {
import net.minecraft.core.registries.BuiltInRegistries;
//? }

/**
 * @author Harley O'Connor
 */
public final class ItemBySpeciesLootPoolEntry extends LootPoolSingletonContainer {
    //? if >= 1.21.1 {
    public static final MapCodec<ItemBySpeciesLootPoolEntry> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance
                    .group(Codec.unboundedMap(ResourceLocation.CODEC, BuiltInRegistries.ITEM.holderByNameCodec()).fieldOf("name_by_species").forGetter(c->c.items))
                    .and(singletonFields(instance))
                    .apply(instance, ItemBySpeciesLootPoolEntry::new));

    public ItemBySpeciesLootPoolEntry(Map<ResourceLocation, Holder<Item>> items, int weight, int quality, List<LootItemCondition> conditions,
                                      List<LootItemFunction> functions) {
        super(weight, quality, conditions, functions);
        this.items = items;
    }
    //? } else {
    /*public static class Serializer extends LootPoolSingletonContainer.Serializer<ItemBySpeciesLootPoolEntry> {
        //~ if < 1.19.4 'BuiltInRegistries' -> 'Registry'
        private static final Codec<Map<ResourceLocation, Holder<Item>>> ITEMS_CODEC = Codec.unboundedMap(ResourceLocation.CODEC, BuiltInRegistries.ITEM.holderByNameCodec());

        public Serializer() {}

        @Override
        protected ItemBySpeciesLootPoolEntry deserialize(JsonObject object, JsonDeserializationContext context, int weight, int quality, LootItemCondition[] conditions, LootItemFunction[] functions) {
            Map<ResourceLocation, Holder<Item>> items = ITEMS_CODEC.parse(JsonOps.INSTANCE, object.get("name_by_species"))
                    .resultOrPartial(error -> {
                        throw new IllegalArgumentException("Failed to decode items list: " + error);
                    })
                    .orElseGet(HashMap::new);

            return new ItemBySpeciesLootPoolEntry(items, weight, quality, conditions, functions);
        }

        @Override
        public void serializeCustom(JsonObject object, ItemBySpeciesLootPoolEntry context, JsonSerializationContext conditions) {
            super.serializeCustom(object, context, conditions);

            ITEMS_CODEC.encodeStart(JsonOps.INSTANCE, context.items)
                    .resultOrPartial(error -> {
                        throw new IllegalArgumentException("Failed to encode items list: " + error);
                    })
                    .ifPresent(out -> object.add("name_by_species", out));
        }
    }

    public ItemBySpeciesLootPoolEntry(Map<ResourceLocation, Holder<Item>> items, int weight, int quality, LootItemCondition[] conditions,
                                      LootItemFunction[] functions) {
        super(weight, quality, conditions, functions);
        this.items = items;
    }
    *///? }

    /** Map of items to set, keyed by the name of the species of tree. */
    private final Map<ResourceLocation, Holder<Item>> items;


    @Override
    public LootPoolEntryType getType() {
        return DTRegistries.ITEM_BY_SPECIES.get();
    }

    @Override
    protected void createItemStack(Consumer<ItemStack> stackConsumer, LootContext context) {
        final Species species = context.getParamOrNull(DTLootContextParams.SPECIES);
        assert species != null;
        Holder<Item> itemHolder = items.get(species.getRegistryName());
        Item item = itemHolder == null ? Items.AIR : itemHolder.value();
        stackConsumer.accept(new ItemStack(item));
    }

}
