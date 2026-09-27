package com.dtteam.dynamictrees.loot.function;

import com.dtteam.dynamictrees.registry.DTRegistries;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;

/**
 * @author Harley O'Connor
 */
public final class MultiplyCount extends LootItemConditionalFunction {
    //? if >= 1.21 {
    public static final MapCodec<MultiplyCount> CODEC = RecordCodecBuilder.mapCodec(
            instance -> commonFields(instance)
                    .and(Codec.FLOAT.fieldOf("multiplier").forGetter(c->c.multiplier))
                    .apply(instance, MultiplyCount::new));
    //? } else {
    /*public static class Serializer extends LootItemConditionalFunction.Serializer<MultiplyCount> {
        public Serializer() {
        }

        public void serialize(JsonObject json, MultiplyCount value, JsonSerializationContext serializationContext) {
            super.serialize(json, value, serializationContext);
            json.addProperty("multiplier", value.multiplier);
        }

        public MultiplyCount deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootItemCondition[] conditions) {
            float multiplier = GsonHelper.getAsFloat(object, "multiplier");
            return new MultiplyCount(conditions, multiplier);
        }
    }
    *///? }

    private final float multiplier;

    //? if >= 1.21 {
    public MultiplyCount(List<LootItemCondition> conditions, float multiplier) {
        super(conditions);
        this.multiplier = multiplier;
    }
    //? } else {
    /*public MultiplyCount(LootItemCondition[] conditions, float multiplier) {
        super(conditions);
        this.multiplier = multiplier;
    }
    *///? }

    //? if >= 1.21 {
    @Override
    public LootItemFunctionType<? extends LootItemConditionalFunction> getType() {
        return DTRegistries.MULTIPLY_COUNT.get();
    }
    //? } else {
    /*@Override
    public LootItemFunctionType getType() {
        return DTRegistries.MULTIPLY_COUNT.get();
    }
    *///? }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        stack.setCount((int) (stack.getCount() * multiplier));
        return stack;
    }

    public static LootItemFunction.Builder multiplyCount() {
        //? if >= 1.21 {
        return () -> new MultiplyCount(List.of(), 1.0F);
        //? } else {
        /*return () -> new MultiplyCount(new LootItemCondition[0], 1.0F);
        *///? }
    }
}
