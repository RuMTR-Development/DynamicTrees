package com.dtteam.dynamictrees.registry;

import com.dtteam.dynamictrees.DynamicTrees;
import com.dtteam.dynamictrees.recipe.DendroPotionRecipeHandler;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;import java.util.function.Supplier;
import java.util.function.UnaryOperator;

//? if >= 1.21 {
import net.minecraft.core.component.DataComponentType;
import com.mojang.serialization.MapCodec;
//? } else {
/*import net.minecraft.world.level.storage.loot.Serializer;
*///? }

//? if 1.19.4 {
/*import net.fabricmc.fabric.impl.itemgroup.ItemGroupHelper;
*///? }

//? if >= 1.19.4 {
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.registries.BuiltInRegistries;
//? } else {
/*import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
*///? }

public class FabricRegistryLoader extends RegistryLoader {

    public static void setup (){
        DTRegistries.setup();
        DendroPotionRecipeHandler.getAllDendroRecipes();
    }

    @Override
    public <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> newBlock) {
        //~ if < 1.19.4 'BuiltInRegistries.BLOCK' -> 'Registry.BLOCK'
        T block = Registry.register(BuiltInRegistries.BLOCK, DynamicTrees.location(name), newBlock.get());
        return ()-> block;
    }

    @Override
    public <T extends Item> Supplier<T> registerItem(String name, Supplier<T> newBlock) {
        //~ if < 1.19.4 'BuiltInRegistries.ITEM' -> 'Registry.ITEM'
        T item = Registry.register(BuiltInRegistries.ITEM, DynamicTrees.location(name), newBlock.get());
        return ()-> item;
    }

    @Override
    public <T extends Recipe<?>> Supplier<RecipeSerializer<T>> registerRecipeType(String name, Supplier<RecipeSerializer<T>> newBlock) {
        //~ if < 1.19.4 'BuiltInRegistries.RECIPE_SERIALIZER' -> 'Registry.RECIPE_SERIALIZER'
        RecipeSerializer<T> type = Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, DynamicTrees.location(name), newBlock.get());
        return ()-> type;
    }

    @Override
    public Supplier<CreativeModeTab> registerCreativeTab(String name, Supplier<ItemStack> icon, MutableComponent title, Consumer<Consumer<ItemStack>> displayItems) {
        //? if >= 1.21 {
        CreativeModeTab tab = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, DynamicTrees.location(DynamicTrees.MOD_ID),
                FabricItemGroup.builder().icon(icon).title(title).displayItems((parameters, output) -> displayItems.accept(output::accept)).build());

        return ()-> tab;
        //? } else if >= 1.19.4 {
        /*CreativeModeTab tab = FabricItemGroup.builder(DynamicTrees.location(DynamicTrees.MOD_ID)).icon(icon).title(title).displayItems((parameters, output) -> displayItems.accept(output)).build();

        try {
            ItemGroupHelper.appendItemGroup(tab);
        } catch (IllegalStateException ignored) {}

        return () -> tab;
        *///? } else {
        /*CreativeModeTab tab = FabricItemGroupBuilder.create(DynamicTrees.location(DynamicTrees.MOD_ID))
                .icon(icon)
                .appendItems((output) -> {
                    displayItems.accept(output::add);
                })
                .build();

        return () -> tab;
        *///? }
    }

    @Override
    public <T extends Entity> Supplier<EntityType<T>> registerEntity(String name, EntityType.Builder<T> builder, boolean isTree) {
//        if (isTree)
//            builder.setShouldReceiveVelocityUpdates(true).setTrackingRange(512).setUpdateInterval(Integer.MAX_VALUE);
        //~ if < 1.19.4 'BuiltInRegistries.ENTITY_TYPE' -> 'Registry.ENTITY_TYPE'
        EntityType<T> entityType = Registry.register(BuiltInRegistries.ENTITY_TYPE, DynamicTrees.location(name), builder.build(name));
        return ()-> entityType;
    }

    @Override
    public <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(String name, BlockEntityType.BlockEntitySupplier<? extends T> newBlockEntity, Supplier<Set<Block>> validBlocks) {
        //~ if < 1.19.4 'BuiltInRegistries.BLOCK_ENTITY_TYPE' -> 'Registry.BLOCK_ENTITY_TYPE'
        BlockEntityType<T> entityType = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, DynamicTrees.location(name), new BlockEntityType<>(newBlockEntity, validBlocks.get(), null));
        return ()-> entityType;
    }

    @Override
    public Supplier<SoundEvent> registerSoundEvent(String name) {
        ResourceLocation location = DynamicTrees.location(name);
        //~ if < 1.19.4 'BuiltInRegistries.SOUND_EVENT' -> 'Registry.SOUND_EVENT'
        //~ if < 1.19.4 'SoundEvent.createVariableRangeEvent' -> 'new SoundEvent'
        SoundEvent type = Registry.register(BuiltInRegistries.SOUND_EVENT, location, SoundEvent.createVariableRangeEvent(location));
        return ()-> type;
    }

    //? if >= 1.21 {
    @Override
    public <T> Supplier<DataComponentType<T>> registerDataComponentType(String name, UnaryOperator<DataComponentType.Builder<T>> operator) {
        DataComponentType<T> type = Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, DynamicTrees.location(name), operator.apply(DataComponentType.builder()).build());
        return ()-> type;
    }
    //? }

    @Override
    public <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>, I extends ArgumentTypeInfo<A, T>> Supplier<I> registerCommandArgumentType(String name, Class<A> infoClass, I argumentTypeInfo) {
        ArgumentTypeInfos.BY_CLASS.put(infoClass, argumentTypeInfo);
        //~ if < 1.19.4 'BuiltInRegistries.COMMAND_ARGUMENT_TYPE' -> 'Registry.COMMAND_ARGUMENT_TYPE'
        I type = Registry.register(BuiltInRegistries.COMMAND_ARGUMENT_TYPE, DynamicTrees.location(name), argumentTypeInfo);
        return ()-> type;
    }

    //? if >= 1.21 {
    @Override
    public Supplier<LootItemConditionType> registerLootConditionType(String name, MapCodec<? extends LootItemCondition> serializerFactory) {
        LootItemConditionType type = Registry.register(BuiltInRegistries.LOOT_CONDITION_TYPE, DynamicTrees.location(name), new LootItemConditionType(serializerFactory));
        return ()-> type;
    }

    @Override
    public Supplier<LootPoolEntryType> registerLootPoolEntryType(String name, MapCodec<? extends LootPoolEntryContainer> serializerFactory) {
        LootPoolEntryType type = Registry.register(BuiltInRegistries.LOOT_POOL_ENTRY_TYPE, DynamicTrees.location(name), new LootPoolEntryType(serializerFactory));
        return ()-> type;
    }

    @Override
    public <L extends LootItemFunction> Supplier<LootItemFunctionType<L>> registerLootFunctionType(String name, MapCodec<L> serializerFactory) {
        LootItemFunctionType<L> type = Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, DynamicTrees.location(name), new LootItemFunctionType<>(serializerFactory));
        return ()-> type;
    }
    //? } else {
    /*@Override
    public Supplier<LootItemConditionType> registerLootConditionType(String name, Serializer<? extends LootItemCondition> serializerFactory) {
        //~ if < 1.19.4 'BuiltInRegistries' -> 'Registry'
        LootItemConditionType type = BuiltInRegistries.register(BuiltInRegistries.LOOT_CONDITION_TYPE, DynamicTrees.location(name), new LootItemConditionType(serializerFactory));
        return ()-> type;
    }

    @Override
    public Supplier<LootPoolEntryType> registerLootPoolEntryType(String name, Serializer<? extends LootPoolEntryContainer> serializerFactory) {
        //~ if < 1.19.4 'BuiltInRegistries' -> 'Registry'
        LootPoolEntryType type = BuiltInRegistries.register(BuiltInRegistries.LOOT_POOL_ENTRY_TYPE, DynamicTrees.location(name), new LootPoolEntryType(serializerFactory));
        return ()-> type;
    }

    @Override
    public <L extends LootItemFunction> Supplier<LootItemFunctionType> registerLootFunctionType(String name, Serializer<L> serializerFactory) {
        //~ if < 1.19.4 'BuiltInRegistries' -> 'Registry'
        LootItemFunctionType type = BuiltInRegistries.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, DynamicTrees.location(name), new LootItemFunctionType(serializerFactory));
        return ()-> type;
    }
    *///? }

    @Override
    public <T extends PlacementModifier> Supplier<PlacementModifierType<T>> registerPlacementModifierType(String name, Supplier<PlacementModifierType<T>> supplier) {
        //~ if < 1.19.4 'BuiltInRegistries.PLACEMENT_MODIFIER_TYPE' -> 'Registry.PLACEMENT_MODIFIERS'
        PlacementModifierType<T> type = Registry.register(BuiltInRegistries.PLACEMENT_MODIFIER_TYPE, DynamicTrees.location(name), supplier.get());
        return ()-> type;
    }

    @Override
    public <T extends Feature<?>> Supplier<T> registerFeature(String name, Supplier<T> supplier) {
        //~ if < 1.19.4 'BuiltInRegistries.FEATURE' -> 'Registry.FEATURE'
        T feature = Registry.register(BuiltInRegistries.FEATURE, DynamicTrees.location(name), supplier.get());
        return ()-> feature;
    }

    @Override
    public <T extends BlockStateProvider> Supplier<BlockStateProviderType<T>> registerBlockStateProviderType(String name, Supplier<BlockStateProviderType<T>> supplier) {
        //~ if < 1.19.4 'BuiltInRegistries.BLOCKSTATE_PROVIDER_TYPE' -> 'Registry.BLOCKSTATE_PROVIDER_TYPES'
        BlockStateProviderType<T> type = Registry.register(BuiltInRegistries.BLOCKSTATE_PROVIDER_TYPE, DynamicTrees.location(name), supplier.get());
        return ()-> type;
    }

    @Override
    public <T extends StructurePoolElement> Supplier<StructurePoolElementType<T>> registerStructurePoolElementType(String name, Supplier<StructurePoolElementType<T>> supplier) {
        //~ if < 1.19.4 'BuiltInRegistries.STRUCTURE_POOL_ELEMENT' -> 'Registry.STRUCTURE_POOL_ELEMENT'
        StructurePoolElementType<T> type = Registry.register(BuiltInRegistries.STRUCTURE_POOL_ELEMENT, DynamicTrees.location(name), supplier.get());
        return ()-> type;
    }
}
