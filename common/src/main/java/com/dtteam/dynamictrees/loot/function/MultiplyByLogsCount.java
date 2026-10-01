package com.dtteam.dynamictrees.loot.function;

import com.dtteam.dynamictrees.loot.DTLootContextParams;
import com.dtteam.dynamictrees.registry.DTRegistries;
import com.dtteam.dynamictrees.systems.nodemapper.NetVolumeNode;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
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
public final class MultiplyByLogsCount extends LootItemConditionalFunction {

    //? if >= 1.21.1 {
    public static final MapCodec<MultiplyByLogsCount> CODEC = RecordCodecBuilder.mapCodec(
            instance -> commonFields(instance)
                    .apply(instance, MultiplyByLogsCount::new));
    //? } else {
    /*public static class Serializer extends LootItemConditionalFunction.Serializer<MultiplyByLogsCount> {
        public Serializer() {
        }

        public void serialize(JsonObject json, MultiplyByLogsCount value, JsonSerializationContext serializationContext) {
            super.serialize(json, value, serializationContext);
        }

        public MultiplyByLogsCount deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootItemCondition[] conditions) {
            return new MultiplyByLogsCount(conditions);
        }
    }
    *///? }

    //? if >= 1.21.1 {
    public MultiplyByLogsCount(List<LootItemCondition> conditions) {
        super(conditions);
    }
    //? } else {
    /*public MultiplyByLogsCount(LootItemCondition[] conditions) {
        super(conditions);
    }
    *///? }

    //? if >= 1.21.1 {
    @Override
    public LootItemFunctionType<? extends LootItemConditionalFunction> getType() {
        return DTRegistries.MULTIPLY_LOGS_COUNT.get();
    }
    //? } else {
    /*@Override
    public LootItemFunctionType getType() {
        return DTRegistries.MULTIPLY_LOGS_COUNT.get();
    }
    *///? }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        final Integer volume = context.getParamOrNull(DTLootContextParams.VOLUME);
        assert volume != null;
        stack.setCount(stack.getCount() * (int) Math.floor((float) volume / NetVolumeNode.Volume.VOXELSPERLOG));
        return stack;
    }

    public static LootItemFunction.Builder multiplyByLogsCount() {
        //? if >= 1.21.1 {
        return () -> new MultiplyByLogsCount(List.of());
        //? } else {
        /*return () -> new MultiplyByLogsCount(new LootItemCondition[0]);
        *///? }
    }

}
