package com.dtteam.dynamictrees.registry;

import com.dtteam.dynamictrees.DynamicTrees;
import com.dtteam.dynamictrees.api.registry.RegistryHandler;
import com.dtteam.dynamictrees.worldgen.biomemodifier.AddDynamicTreesBiomeModifier;
import com.dtteam.dynamictrees.worldgen.biomemodifier.RunFeatureCancellersBiomeModifier;
import com.dtteam.dynamictrees.worldgen.holderset.IncludesExcludesHolderSet;
import com.dtteam.dynamictrees.worldgen.holderset.NameRegexMatchHolderSet;
import com.dtteam.dynamictrees.worldgen.holderset.TagsRegexMatchHolderSet;
import com.google.common.base.Suppliers;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
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
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.holdersets.HolderSetType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

//? if >= 1.19.4 {
/*import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.event.CreativeModeTabEvent;
*///? } else {
import net.minecraftforge.registries.ForgeRegistries;
//? }

//? if >= 1.19.2 {
/*import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
*///? } else {
import net.minecraft.commands.synchronization.ArgumentSerializer;
import net.minecraft.commands.synchronization.ArgumentTypes;
//? }

public class ForgeRegistryLoader extends RegistryLoader {
    //~ if < 1.19.2 'ENTITY_TYPES' -> 'ENTITIES'
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, DynamicTrees.MOD_ID);
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, DynamicTrees.MOD_ID);

    //? if >= 1.19.4 {
    /*public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, DynamicTrees.MOD_ID);
    public static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIER_TYPES = DeferredRegister.create(Registries.PLACEMENT_MODIFIER_TYPE, DynamicTrees.MOD_ID);
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, DynamicTrees.MOD_ID);
    public static final DeferredRegister<BlockStateProviderType<?>> BLOCK_STATE_PROVIDER_TYPES = DeferredRegister.create(Registries.BLOCK_STATE_PROVIDER_TYPE, DynamicTrees.MOD_ID);
    public static final DeferredRegister<StructurePoolElementType<?>> STRUCTURE_POOL_ELEMENT_TYPES = DeferredRegister.create(Registries.STRUCTURE_POOL_ELEMENT, DynamicTrees.MOD_ID);
    public static final DeferredRegister<ArgumentTypeInfo<?, ?>> ARGUMENT_TYPES = DeferredRegister.create(Registries.COMMAND_ARGUMENT_TYPE, DynamicTrees.MOD_ID);
    public static final DeferredRegister<LootItemConditionType> LOOT_CONDITION_TYPES = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, DynamicTrees.MOD_ID);
    public static final DeferredRegister<LootPoolEntryType> LOOT_POOL_ENTRY_TYPES = DeferredRegister.create(Registries.LOOT_POOL_ENTRY_TYPE, DynamicTrees.MOD_ID);
    public static final DeferredRegister<LootItemFunctionType> LOOT_FUNCTION_TYPES = DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, DynamicTrees.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZER = DeferredRegister.create(Registries.RECIPE_SERIALIZER, DynamicTrees.MOD_ID);

    public static final List<Consumer<CreativeModeTabEvent.Register>> CREATIVE_TABS = new ArrayList<>();
    *///? } else {
    public static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIER_TYPES = DeferredRegister.create(Registry.PLACEMENT_MODIFIER_REGISTRY, DynamicTrees.MOD_ID);
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, DynamicTrees.MOD_ID);
    public static final DeferredRegister<BlockStateProviderType<?>> BLOCK_STATE_PROVIDER_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_STATE_PROVIDER_TYPES, DynamicTrees.MOD_ID);
    public static final DeferredRegister<StructurePoolElementType<?>> STRUCTURE_POOL_ELEMENT_TYPES = DeferredRegister.create(Registry.STRUCTURE_POOL_ELEMENT_REGISTRY, DynamicTrees.MOD_ID);
    public static final DeferredRegister<LootItemConditionType> LOOT_CONDITION_TYPES = DeferredRegister.create(Registry.LOOT_CONDITION_TYPE.key(), DynamicTrees.MOD_ID);
    public static final DeferredRegister<LootPoolEntryType> LOOT_POOL_ENTRY_TYPES = DeferredRegister.create(Registry.LOOT_POOL_ENTRY_TYPE.key(), DynamicTrees.MOD_ID);
    public static final DeferredRegister<LootItemFunctionType> LOOT_FUNCTION_TYPES = DeferredRegister.create(Registry.LOOT_FUNCTION_TYPE.key(), DynamicTrees.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, DynamicTrees.MOD_ID);

    //~ if < 1.19.2 'BLOCK_ENTITY_TYPES' -> 'BLOCK_ENTITIES'
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, DynamicTrees.MOD_ID);

    //? if >= 1.19.2
    //public static final DeferredRegister<ArgumentTypeInfo<?, ?>> ARGUMENT_TYPES = DeferredRegister.create(ForgeRegistries.COMMAND_ARGUMENT_TYPES, DynamicTrees.MOD_ID);
    //? }

    public static void setup(IEventBus modBus) {
        BLOCK_ENTITY_TYPES.register(modBus);
        ENTITY_TYPES.register(modBus);
        FEATURES.register(modBus);
        PLACEMENT_MODIFIER_TYPES.register(modBus);
        SOUND_EVENTS.register(modBus);
        BLOCK_STATE_PROVIDER_TYPES.register(modBus);
        STRUCTURE_POOL_ELEMENT_TYPES.register(modBus);
        LOOT_POOL_ENTRY_TYPES.register(modBus);
        LOOT_CONDITION_TYPES.register(modBus);
        LOOT_FUNCTION_TYPES.register(modBus);
        RECIPE_SERIALIZER.register(modBus);

        //? if >= 1.19.2
        //ARGUMENT_TYPES.register(modBus);

        //MinecraftForge
        BIOME_MODIFIER_SERIALIZERS.register(modBus);
        HOLDER_SET_TYPES.register(modBus);

        DTRegistries.setup();
    }

    @Override
    public <T extends Block> Supplier<T> registerBlock (String name, Supplier<T> newBlock){
        Supplier<T> sup = Suppliers.memoize(newBlock::get);
        RegistryHandler.addBlock(DynamicTrees.location(name), sup);
        return sup;
    }

    @Override
    public <T extends Item> Supplier<T> registerItem (String name, Supplier<T> newBlock){
        Supplier<T> sup = Suppliers.memoize(newBlock::get);
        RegistryHandler.addItem(DynamicTrees.location(name), sup);
        return sup;
    }

    @Override
    public <T extends Recipe<?>> Supplier<RecipeSerializer<T>> registerRecipeType(String name, Supplier<RecipeSerializer<T>> newBlock) {
        return RECIPE_SERIALIZER.register(name, newBlock);
    }

    @Override
    public Supplier<CreativeModeTab> registerCreativeTab(String name, Supplier<ItemStack> icon, MutableComponent title, Consumer<Consumer<ItemStack>> displayItems) {
        //? if >= 1.19.4 {
        /*AtomicReference<CreativeModeTab> tab = new AtomicReference<>();

        CREATIVE_TABS.add(e -> {
            CreativeModeTab newTab = e.registerCreativeModeTab(new ResourceLocation(DynamicTrees.MOD_ID, name), builder ->
                    builder.icon(icon)
                            .title(title)
                            .displayItems(displayItems)
                            .build());

            tab.set(newTab);
        });

        return tab::get;
        *///? } else {
        ResourceLocation location = ResourceLocation.fromNamespaceAndPath(DynamicTrees.MOD_ID, name);

        //? if >= 1.19.2 {
        /*String label = location.toLanguageKey();
        *///? } else {
        String label = String.format("%s.%s", location.getNamespace(), location.getPath().replace('/', '.'));
        //? }

        CreativeModeTab tab = new CreativeModeTab(label) {
            @Override
            public ItemStack makeIcon() {
                return icon.get();
            }

            @Override
            public void fillItemList(NonNullList<ItemStack> items) {
                displayItems.accept(items::add);
            }

            @Override
            public Component getDisplayName() {
                return title;
            }
        };

        return () -> tab;
        //? }

    }

    @Override
    public <T extends Entity> Supplier<EntityType<T>> registerEntity(String name, EntityType.Builder<T> builder, boolean isTree) {
        if (isTree)
            builder.setShouldReceiveVelocityUpdates(true).setTrackingRange(512).setUpdateInterval(Integer.MAX_VALUE);
        return ENTITY_TYPES.register(name, () -> builder.build(name));
    }

    @Override
    public <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(String name, BlockEntityType.BlockEntitySupplier<? extends T> newBlockEntity, Supplier<Set<Block>> validBlocks) {
        return BLOCK_ENTITY_TYPES.register(name, () ->
                new BlockEntityType<>(newBlockEntity, validBlocks.get(), null));
    }

    @Override
    public Supplier<SoundEvent> registerSoundEvent(String name) {
        //~ if < 1.19.4 'SoundEvent.createVariableRangeEvent' -> 'new SoundEvent'
        return SOUND_EVENTS.register(name, () -> new SoundEvent(DynamicTrees.location(name)));
    }

    //? if >= 1.19.2 {
    /*@Override
    public <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>, I extends ArgumentTypeInfo<A, T>> Supplier<I> registerCommandArgumentType (String name, Class<A> infoClass, I argumentTypeInfo){
        return ARGUMENT_TYPES.register(name, () -> ArgumentTypeInfos.registerByClass(infoClass, argumentTypeInfo));
    }*///? } else {
    @Override
    public <A extends ArgumentType<?>, I extends ArgumentSerializer<A>> Supplier<I> registerCommandArgumentType(String name, Class<A> infoClass, I argumentTypeInfo) {
        ArgumentTypes.register(DynamicTrees.location(name).toString(), infoClass, argumentTypeInfo);
        return () -> argumentTypeInfo;
    }
    //? }

    @Override
    public Supplier<LootItemConditionType> registerLootConditionType(String name, Serializer<? extends LootItemCondition> serializerFactory) {
        return LOOT_CONDITION_TYPES.register(name, () -> new LootItemConditionType(serializerFactory));
    }

    @Override
    public Supplier<LootPoolEntryType> registerLootPoolEntryType(String name, Serializer<? extends LootPoolEntryContainer> serializerFactory) {
        return LOOT_POOL_ENTRY_TYPES.register(name, () -> new LootPoolEntryType(serializerFactory));
    }

    @Override
    public <L extends LootItemFunction> Supplier<LootItemFunctionType> registerLootFunctionType(String name, Serializer<L> serializerFactory) {
        return LOOT_FUNCTION_TYPES.register(name, () -> new LootItemFunctionType(serializerFactory));
    }

    ///////////////////////////////////////////
    // WORLD GEN
    ///////////////////////////////////////////

    @Override
    public <T extends PlacementModifier> Supplier<PlacementModifierType<T>> registerPlacementModifierType (String name, Supplier<PlacementModifierType<T>> supplier){
        return PLACEMENT_MODIFIER_TYPES.register(name, supplier);
    }

    @Override
    public <T extends Feature<?>> Supplier<T> registerFeature (String name, Supplier<T> supplier){
        return FEATURES.register(name, supplier);
    }

    @Override
    public <T extends BlockStateProvider> Supplier<BlockStateProviderType<T>> registerBlockStateProviderType (String name, Supplier<BlockStateProviderType<T>> supplier){
        return BLOCK_STATE_PROVIDER_TYPES.register(name, supplier);
    }

    @Override
    public <T extends StructurePoolElement> Supplier<StructurePoolElementType<T>> registerStructurePoolElementType (String name, Supplier<StructurePoolElementType<T>> supplier){
        return STRUCTURE_POOL_ELEMENT_TYPES.register(name, supplier);
    }

    ///////////////////////////////////////////
    // NEO FORGE ONLY
    ///////////////////////////////////////////

    public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, DynamicTrees.MOD_ID);
    public static final DeferredRegister<HolderSetType> HOLDER_SET_TYPES = DeferredRegister.create(ForgeRegistries.Keys.HOLDER_SET_TYPES, DynamicTrees.MOD_ID);

    public static final Supplier<Codec<AddDynamicTreesBiomeModifier>> ADD_DYNAMIC_TREES_BIOME_MODIFIER =
            BIOME_MODIFIER_SERIALIZERS.register("add_dynamic_trees", () -> MapCodec.unit(AddDynamicTreesBiomeModifier::new).codec());
    public static final Supplier<Codec<RunFeatureCancellersBiomeModifier>> RUN_FEATURE_CANCELLERS_BIOME_MODIFIER =
            BIOME_MODIFIER_SERIALIZERS.register("run_feature_cancellers", () -> MapCodec.unit(RunFeatureCancellersBiomeModifier::new).codec());
    public static final Supplier<HolderSetType> INCLUDES_EXCLUDES_HOLDER_SET_TYPE = HOLDER_SET_TYPES.register("includes_excludes", IncludesExcludesHolderSet.Type::new);
    public static final Supplier<HolderSetType> NAME_REGEX_MATCH_HOLDER_SET_TYPE = HOLDER_SET_TYPES.register("name_regex_match", NameRegexMatchHolderSet.Type::new);
    public static final Supplier<HolderSetType> TAGS_REGEX_MATCH_HOLDER_SET_TYPE = HOLDER_SET_TYPES.register("tags_regex_match", TagsRegexMatchHolderSet.Type::new);

}
