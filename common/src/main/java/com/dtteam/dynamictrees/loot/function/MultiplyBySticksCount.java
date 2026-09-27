package com.dtteam.dynamictrees.loot.function;

import com.dtteam.dynamictrees.loot.DTLootContextParams;
import com.dtteam.dynamictrees.registry.DTRegistries;
import com.dtteam.dynamictrees.systems.nodemapper.NetVolumeNode;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;

/**
 * @author Harley O'Connor
 */
public final class MultiplyBySticksCount extends LootItemConditionalFunction {
    //? if >= 1.21 {
    public static final MapCodec<MultiplyBySticksCount> CODEC = RecordCodecBuilder.mapCodec(
            instance -> commonFields(instance)
                    .apply(instance, MultiplyBySticksCount::new));
    //? } else {
    /*public static class Serializer extends LootItemConditionalFunction.Serializer<MultiplyBySticksCount> {
        public Serializer() {}

        @Override
        public MultiplyBySticksCount deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootItemCondition[] conditions) {
            return new MultiplyBySticksCount(conditions);
        }

        @Override
        public void serialize(JsonObject json, MultiplyBySticksCount value, JsonSerializationContext serializationContext) {
            super.serialize(json, value, serializationContext);
        }
    }
    *///? }

    //? if >= 1.21 {
    public MultiplyBySticksCount(List<LootItemCondition> conditions) {
        super(conditions);
    }
    //? } else {
    /*public MultiplyBySticksCount(LootItemCondition[] conditions) {
        super(conditions);
    }
    *///? }

    //? if >= 1.21 {
    @Override
    public LootItemFunctionType<? extends LootItemConditionalFunction> getType() {
        return DTRegistries.MULTIPLY_STICKS_COUNT.get();
    }
    //? } else {
    /*public LootItemFunctionType getType() {
        return DTRegistries.MULTIPLY_STICKS_COUNT.get();
    }
    *///? }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        final Integer volume = context.getParamOrNull(DTLootContextParams.VOLUME);
        assert volume != null;
        stack.setCount(stack.getCount() * 8 * (volume % NetVolumeNode.Volume.VOXELSPERLOG) /
                NetVolumeNode.Volume.VOXELSPERLOG);
        return stack;
    }

    public static LootItemFunction.Builder multiplyBySticksCount() {
        //? if >= 1.21 {
        return () -> new MultiplyBySticksCount(List.of());
        //? } else {
        /*return () -> new MultiplyBySticksCount(new LootItemCondition[0]);
        *///? }
    }

}
