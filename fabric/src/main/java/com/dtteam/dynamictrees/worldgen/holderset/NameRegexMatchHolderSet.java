package com.dtteam.dynamictrees.worldgen.holderset;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;

import java.util.function.Supplier;
import java.util.stream.Stream;

//? if >= 1.19.4 {
import net.minecraft.core.HolderLookup;
//? }

public class NameRegexMatchHolderSet<T> extends RegexMatchHolderSet<T> {
    //? if >= 1.19.4 {
    public NameRegexMatchHolderSet(HolderLookup.RegistryLookup<T> registryLookup, String regex) {
        super(registryLookup, regex);
    }
    //? } else {
    /*public NameRegexMatchHolderSet(Registry<T> registry, String regex) {
        super(registry, regex);
    }

    public NameRegexMatchHolderSet(Supplier<Registry<T>> registrySupplier, String regex) {
        super(registrySupplier, regex);
    }
    *///? }

    @Override
    protected Stream<String> getInput(Holder<T> holder) {
        return holder.unwrapKey().stream().map(key -> key.location().toString());
    }
}
