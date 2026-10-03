package com.dtteam.dynamictrees.worldgen.holderset;

import com.dtteam.dynamictrees.registry.ForgeRegistryLoader;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.function.Supplier;
import java.util.stream.Stream;

//? if >= 1.19.4 {
/*import net.minecraft.core.HolderLookup;
*///? }

//? if >= 1.19.2 {
/*import net.minecraftforge.registries.holdersets.HolderSetType;
import net.minecraftforge.registries.holdersets.ICustomHolderSet;
*///? }

public class TagsRegexMatchHolderSet<T> extends RegexMatchHolderSet<T> {
    //? if >= 1.19.2 {
    /*public static class Type implements HolderSetType {
        @Override
        public <T> Codec<? extends ICustomHolderSet<T>> makeCodec(ResourceKey<? extends Registry<T>> resourceKey, Codec<Holder<T>> codec, boolean b) {
            return RegexMatchHolderSet.mapCodec(resourceKey, TagsRegexMatchHolderSet::new).codec();
        }
    }

    @Override
    public HolderSetType type() {
        return ForgeRegistryLoader.TAGS_REGEX_MATCH_HOLDER_SET_TYPE.get();
    }
    *///? }

    //? if >= 1.19.4 {
    /*public TagsRegexMatchHolderSet(HolderLookup.RegistryLookup<T> registryLookup, String regex) {
        super(registryLookup, regex);
    }
    *///? } else {
    public TagsRegexMatchHolderSet(Registry<T> registry, String regex) {
        super(registry, regex);
    }

    public TagsRegexMatchHolderSet(Supplier<Registry<T>> registrySupplier, String regex) {
        super(registrySupplier, regex);
    }
    //? }

    @Override
    protected Stream<String> getInput(Holder<T> holder) {
        return holder.tags().map(tagKey -> tagKey.location().toString());
    }
}