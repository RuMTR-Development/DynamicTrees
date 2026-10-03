package com.dtteam.dynamictrees.data;

import com.dtteam.dynamictrees.loot.DTLootParameterSets;
import com.dtteam.dynamictrees.loot.condition.SeasonalSeedDropChance;
import com.dtteam.dynamictrees.loot.condition.VoluntarySeedDropChance;
import com.dtteam.dynamictrees.loot.entry.SeedItemLootPoolEntry;
import com.dtteam.dynamictrees.loot.function.MultiplyByLogsCount;
import com.dtteam.dynamictrees.loot.function.MultiplyBySticksCount;
import com.dtteam.dynamictrees.utility.ItemUtils;
import net.minecraft.advancements.critereon.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.LimitCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.List;

//? if >= 1.21.1 {
import net.minecraft.core.registries.Registries;
//? }

//? if >= 1.19.4 {
import net.minecraft.core.HolderLookup;
//? }

public class DTLootTableBuilder {

    protected static LootItemCondition.Builder hasSilkTouch(
            //? if >= 1.19.4
             HolderLookup.Provider registries
    ) {
        //? if >= 1.21
         HolderLookup.RegistryLookup<Enchantment> registrylookup = registries.lookupOrThrow(Registries.ENCHANTMENT);

        return MatchTool.toolMatches(
                ItemPredicate.Builder.item()
                        //? if >= 1.21.1 {
                        .withSubPredicate(
                                ItemSubPredicates.ENCHANTMENTS,
                                ItemEnchantmentsPredicate.enchantments(
                                        List.of(new EnchantmentPredicate(registrylookup.getOrThrow(Enchantments.SILK_TOUCH), MinMaxBounds.Ints.atLeast(1)))
                                )
                        )
                        //? } else {
                        /*.hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1)))
                        *///? }
        );
    }

    private static LootItemCondition.Builder hasNoShearsOrSilkTouch(
            //? if >= 1.19.4
             HolderLookup.Provider registries
    ) {
        return hasShearsOrSilkTouch(
                //? if >= 1.19.4
                 registries
        ).invert();
    }

    private static LootItemCondition.Builder hasShearsOrSilkTouch(
            //? if >= 1.19.4
             HolderLookup.Provider registries
    ) {
        LootItemCondition.Builder hasShears = MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS));
        return hasShears.or(hasSilkTouch(
                //? if >= 1.19.4
                 registries
        ));
    }

    protected static LootTable.Builder createSelfDropDispatchTable(Block block, LootItemCondition.Builder conditionBuilder, LootPoolEntryContainer.Builder<?> alternativeBuilder) {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(block).when(conditionBuilder).otherwise(alternativeBuilder)));
    }

    public static LootTable.Builder createLeavesBlockDrops(
            Block primitiveLeavesBlock,
            float[] seedChances,
            Item stickItem

            //? if >= 1.19.4 {
            ,
            HolderLookup.Provider registries
            //? }
    ) {
        return createSelfDropDispatchTable(
                primitiveLeavesBlock,
                hasShearsOrSilkTouch(
                        //? if >= 1.19.4
                         registries
                ),
                SeedItemLootPoolEntry.lootTableSeedItem()
                        .when(ExplosionCondition.survivesExplosion())

                        //? if >= 1.21.1 {
                        .when(BonusLevelTableCondition.bonusLevelFlatChance(ItemUtils.getEnchantment(Enchantments.FORTUNE, registries), seedChances))
                        //? } else {
                        /*.when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, seedChances))
                        *///? }

                        .when(SeasonalSeedDropChance.seasonalSeedDropChance())
        ).withPool(
                LootPool.lootPool().setRolls(ConstantValue.exactly(1)).when(
                        hasNoShearsOrSilkTouch(
                                //? if >= 1.19.4
                                 registries
                        ))
                        .add(LootItem.lootTableItem(stickItem)
                                .apply(SetItemCountFunction.setCount(
                                        UniformGenerator.between(1.0F, 2.0F)
                                ))
                                .apply(ApplyExplosionDecay.explosionDecay())

                                //? if >= 1.21.1 {
                                .when(BonusLevelTableCondition.bonusLevelFlatChance(ItemUtils.getEnchantment(Enchantments.FORTUNE, registries), 0.02F,
                                        0.022222223F, 0.025F, 0.033333335F, 0.1F)))
                                //? } else {
                                /*.when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 0.02F,
                                        0.022222223F, 0.025F, 0.033333335F, 0.1F)))
                                *///? }
        ).setParamSet(DTLootParameterSets.LEAVES_BLOCK);
    }

    public static LootTable.Builder createPalmLeavesBlockDrops(
            Block primitiveLeavesBlock,
            float[] seedChances

            //? if >= 1.19.4 {
            ,
            HolderLookup.Provider registries
            //? }
   ) {
        return createSelfDropDispatchTable(
                primitiveLeavesBlock,
                hasShearsOrSilkTouch(
                        //? if >= 1.19.4
                         registries
                ),
                SeedItemLootPoolEntry.lootTableSeedItem()
                        .when(ExplosionCondition.survivesExplosion())

                        //? if >= 1.21.1 {
                        .when(BonusLevelTableCondition.bonusLevelFlatChance(ItemUtils.getEnchantment(Enchantments.FORTUNE, registries), seedChances))
                        //? } else {
                        /*.when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, seedChances))
                        *///? }

                        .when(SeasonalSeedDropChance.seasonalSeedDropChance())
        ).setParamSet(DTLootParameterSets.LEAVES_BLOCK);
    }

    public static LootTable.Builder createWartBlockDrops(
            Block primitiveWartBlock

            //? if >= 1.19.4 {
            ,
            HolderLookup.Provider registries
            //? }
    ) {
        return LootTable.lootTable().withPool(
                LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(primitiveWartBlock))
                        .when(ExplosionCondition.survivesExplosion())
        );
    }

    public static LootTable.Builder createLeavesDrops(
            float[] seedChances,
            LootContextParamSet parameterSet

            //? if >= 1.19.4 {
            ,
            HolderLookup.Provider registries
            //? }
    ) {
        return createLeavesDrops(
                seedChances,
                parameterSet,
                Items.STICK

                //? if >= 1.19.4 {
                ,
                registries
                //? }
        );
    }
    public static LootTable.Builder createLeavesDrops(
            float[] seedChances,
            LootContextParamSet parameterSet,
            Item stickItem

            //? if >= 1.19.4 {
            ,
            HolderLookup.Provider registries
            //? }
    ) {
        return LootTable.lootTable().withPool(
                LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(
                        SeedItemLootPoolEntry.lootTableSeedItem()
                                .when(ExplosionCondition.survivesExplosion())

                                //? if >= 1.21.1 {
                                .when(BonusLevelTableCondition.bonusLevelFlatChance(ItemUtils.getEnchantment(Enchantments.FORTUNE, registries), seedChances))
                                //? } else {
                                /*.when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, seedChances))
                                *///? }

                                .when(SeasonalSeedDropChance.seasonalSeedDropChance())
                )
        ).withPool(
                LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(
                        LootItem.lootTableItem(Items.STICK)
                                .apply(SetItemCountFunction.setCount(
                                        UniformGenerator.between(1.0F, 2.0F)
                                ))
                                .apply(ApplyExplosionDecay.explosionDecay())
                                .when(BonusLevelTableCondition.bonusLevelFlatChance(
                                        //? if >= 1.21.1 {
                                        ItemUtils.getEnchantment(Enchantments.FORTUNE, registries), 0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F
                                        //? } else {
                                        /*Enchantments.BLOCK_FORTUNE, 0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F
                                        *///? }
                                ))
                )
        ).setParamSet(parameterSet);
    }

    public static LootTable.Builder createPalmLeavesDrops(
            float[] seedChances,
            LootContextParamSet parameterSet

            //? if >= 1.19.4 {
            ,
            HolderLookup.Provider registries
            //? }
    ) {
        return LootTable.lootTable().withPool(
                LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(
                        SeedItemLootPoolEntry.lootTableSeedItem()
                                .when(ExplosionCondition.survivesExplosion())

                                //? if >= 1.21.1 {
                                .when(BonusLevelTableCondition.bonusLevelFlatChance(ItemUtils.getEnchantment(Enchantments.FORTUNE, registries), seedChances))
                                //? } else {
                                /*.when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, seedChances))
                                *///? }

                                .when(SeasonalSeedDropChance.seasonalSeedDropChance())
                )
        ).setParamSet(parameterSet);
    }

    public static LootTable.Builder createWartDrops(
            Block primitiveWartBlock

            //? if >= 1.19.4 {
            ,
            HolderLookup.Provider registries
            //? }
    ) {
        return LootTable.lootTable().withPool(
                LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(primitiveWartBlock))
                        .when(ExplosionCondition.survivesExplosion())
                        .when(BonusLevelTableCondition.bonusLevelFlatChance(
                                //? if >= 1.21.1 {
                                ItemUtils.getEnchantment(Enchantments.FORTUNE, registries), 0.1F, 0.1333333F, 0.1666666F, 0.2F
                                //? } else {
                                /*Enchantments.BLOCK_FORTUNE, 0.1F, 0.1333333F, 0.1666666F, 0.2F
                                *///? }
                        ))
        );
    }

    public static LootTable.Builder createVoluntaryDrops(
            Item seedItem

            //? if >= 1.19.4 {
            ,
            HolderLookup.Provider registries
            //? }
    ) {
        return LootTable.lootTable().withPool(
                LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(
                        LootItem.lootTableItem(seedItem)
                                .when(VoluntarySeedDropChance.voluntarySeedDropChance())
                )
        ).setParamSet(DTLootParameterSets.VOLUNTARY);
    }

    public static LootTable.Builder createBranchDrops(
            Block primitiveLogBlock,
            Item stickItem

            //? if >= 1.19.4 {
            ,
            HolderLookup.Provider registries
            //? }
    ) {
        return LootTable.lootTable().withPool(
                LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(
                        LootItem.lootTableItem(primitiveLogBlock)
                                .apply(MultiplyByLogsCount.multiplyByLogsCount())
                                .apply(ApplyExplosionDecay.explosionDecay())
                )
        ).withPool(
                LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(
                        LootItem.lootTableItem(stickItem)
                                .apply(MultiplyBySticksCount.multiplyBySticksCount())
                                .apply(ApplyExplosionDecay.explosionDecay())
                )
        ).setParamSet(DTLootParameterSets.BRANCHES);
    }

    public static LootTable.Builder createFruitPodDrops(
            Block fruitBlock,
            Item fruitItem,
            IntegerProperty ageProperty,
            int matureAge,
            int count

            //? if >= 1.19.4 {
            ,
            HolderLookup.Provider registries
            //? }
    ) {
        return createFruitPodDrops(
                fruitBlock,
                fruitItem,
                ageProperty,
                matureAge,
                count,
                count

                //? if >= 1.19.4 {
                ,
                registries
                //? }
        );
    }
    public static LootTable.Builder createFruitPodDrops(
            Block fruitBlock,
            Item fruitItem,
            IntegerProperty ageProperty,
            int matureAge,
            int countMin,
            int countMax

            //? if >= 1.19.4 {
            ,
            HolderLookup.Provider registries
            //? }
    ) {
        //Select a number provider depending on the range.
        // If both numbers are the same then use a constant value, otherwise use an uniform range.
        NumberProvider numberProvider = (countMin == countMax) ?
                ConstantValue.exactly(countMax) :
                UniformGenerator.between(countMin, countMax);
        //Apply the count to the item builder only if it's not just 1.
        LootPoolSingletonContainer.Builder<?> itemBuilder = LootItem.lootTableItem(fruitItem);
        if (!(countMin == countMax && countMax == 1)){
            itemBuilder.apply(SetItemCountFunction.setCount(numberProvider));
            //If the min count is negative, then cap it up to 0.
            if (countMin < 0)
                itemBuilder.apply(LimitCount.limitCount(IntRange.lowerBound(0)));
        }
        //finally, return the table builder
        return LootTable.lootTable().withPool(
                LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(
                        itemBuilder
                                .apply(ApplyExplosionDecay.explosionDecay())
                                .when(LootItemBlockStatePropertyCondition
                                        .hasBlockStateProperties(fruitBlock)
                                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                                .hasProperty(ageProperty, matureAge))
                                )
                )
        ).setParamSet(LootContextParamSets.BLOCK);
    }

}
