package com.dtteam.dynamictrees.loot.condition;

import com.dtteam.dynamictrees.config.DTConfigs;
import com.dtteam.dynamictrees.loot.DTLootContextParams;
import com.dtteam.dynamictrees.registry.DTRegistries;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

/**
 * @author Harley O'Connor
 */
public final class VoluntarySeedDropChance implements LootItemCondition {
    //? if >= 1.21 {
    public static final MapCodec<VoluntarySeedDropChance> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance
                    .group(Codec.FLOAT.fieldOf("rarity").forGetter((c)->c.rarity))
                    .apply(instance, VoluntarySeedDropChance::new));
    //? } else {
    /*public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<VoluntarySeedDropChance> {
        @Override
        public void serialize(JsonObject json, VoluntarySeedDropChance value, JsonSerializationContext serializationContext) {
            json.addProperty("rarity", value.rarity);
        }

        @Override
        public VoluntarySeedDropChance deserialize(JsonObject json, JsonDeserializationContext serializationContext) {
            float rarity = GsonHelper.getAsFloat(json, "rarity");
            return new VoluntarySeedDropChance(rarity);
        }
    }
    *///? }

    private final float rarity;

    public VoluntarySeedDropChance(float rarity) {
        this.rarity = rarity;
    }

    @Override
    public LootItemConditionType getType() {
        return DTRegistries.VOLUNTARY_SEED_DROP_CHANCE.get();
    }

    @Override
    public boolean test(LootContext context) {
        final Float seasonalSeedDropFactor = context.getParamOrNull(DTLootContextParams.SEASONAL_SEED_DROP_FACTOR);
        assert seasonalSeedDropFactor != null;
        double minimumDropRate = DTConfigs.SERVER.minSeasonalVoluntarySeedDropRate.get();
        double adjustedSeasonalSeedDropFactor = Math.min(seasonalSeedDropFactor + minimumDropRate, 1.0F);
        return rarity * DTConfigs.SERVER.voluntarySeedDropRate.get() * adjustedSeasonalSeedDropFactor > context.getRandom().nextFloat();
    }

    public static LootItemCondition.Builder voluntarySeedDropChance() {
        return () -> new VoluntarySeedDropChance(1.0F);
    }

    public static LootItemCondition.Builder voluntarySeedDropChance(float rarity) {
        return () -> new VoluntarySeedDropChance(rarity);
    }

}
