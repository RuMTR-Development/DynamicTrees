package com.dtteam.dynamictrees.worldgen.holderset;

import com.dtteam.dynamictrees.registry.ForgeRegistryLoader;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.holdersets.HolderSetType;
import net.minecraftforge.registries.holdersets.ICustomHolderSet;

import java.util.stream.Stream;

public class TagsRegexMatchHolderSet<T> extends RegexMatchHolderSet<T> {

    public static class Type implements HolderSetType {
        @Override
        public <T> Codec<? extends ICustomHolderSet<T>> makeCodec(ResourceKey<? extends Registry<T>> resourceKey, Codec<Holder<T>> codec, boolean b) {
            return RegexMatchHolderSet.mapCodec(resourceKey, TagsRegexMatchHolderSet::new).codec();
        }
    }

    public TagsRegexMatchHolderSet(HolderLookup.RegistryLookup<T> registryLookup, String regex) {
        super(registryLookup, regex);
    }

    @Override
    protected Stream<String> getInput(Holder<T> holder) {
        return holder.tags().map(tagKey -> tagKey.location().toString());
    }

    @Override
    public HolderSetType type() {
        return ForgeRegistryLoader.TAGS_REGEX_MATCH_HOLDER_SET_TYPE.get();
    }
}