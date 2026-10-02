package com.dtteam.dynamictrees.worldgen.holderset;

import com.dtteam.dynamictrees.registry.ForgeRegistryLoader;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.holdersets.HolderSetType;
import net.minecraftforge.registries.holdersets.ICustomHolderSet;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class NameRegexMatchHolderSet<T> extends RegexMatchHolderSet<T> {

    public static class Type implements HolderSetType {
        @Override
        public <T> Codec<? extends ICustomHolderSet<T>> makeCodec(ResourceKey<? extends Registry<T>> resourceKey, Codec<Holder<T>> codec, boolean b) {
            return RegexMatchHolderSet.mapCodec(resourceKey, NameRegexMatchHolderSet::new).codec();
        }
    }

    //? if >= 1.19.4 {
    /*public NameRegexMatchHolderSet(HolderLookup.RegistryLookup<T> registryLookup, String regex) {
        super(registryLookup, regex);
    }
    *///? } else {
    public NameRegexMatchHolderSet(Registry<T> registry, String regex) {
        super(registry, regex);
    }

    public NameRegexMatchHolderSet(Supplier<Registry<T>> registrySupplier, String regex) {
        super(registrySupplier, regex);
    }
    //? }

    @Override
    protected Stream<String> getInput(Holder<T> holder) {
        return holder.unwrapKey().stream().map(key -> key.location().toString());
    }

    @Override
    public HolderSetType type() {
        return ForgeRegistryLoader.NAME_REGEX_MATCH_HOLDER_SET_TYPE.get();
    }
}