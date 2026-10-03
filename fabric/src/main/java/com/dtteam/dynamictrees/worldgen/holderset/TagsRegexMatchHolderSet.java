package com.dtteam.dynamictrees.worldgen.holderset;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;

import java.util.function.Supplier;
import java.util.stream.Stream;

//? if >= 1.19.4 {
import net.minecraft.core.HolderLookup;
//? }

public class TagsRegexMatchHolderSet<T> extends RegexMatchHolderSet<T> {

    //? if >= 1.19.4 {
    public TagsRegexMatchHolderSet(HolderLookup.RegistryLookup<T> registryLookup, String regex) {
        super(registryLookup, regex);
    }
    //? } else {
    /*public TagsRegexMatchHolderSet(Registry<T> registry, String regex) {
        super(registry, regex);
    }

    public TagsRegexMatchHolderSet(Supplier<Registry<T>> registrySupplier, String regex) {
        super(registrySupplier, regex);
    }
    *///? }

    @Override
    protected Stream<String> getInput(Holder<T> holder) {
        return holder.tags().map(tagKey ->
                tagKey.location().toString()
        );
    }
}
