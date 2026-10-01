package com.dtteam.dynamictrees.worldgen.holderset;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;import net.minecraft.core.Registry;import net.minecraft.resources.ResourceKey;

import java.util.Objects;
import java.util.Optional;import java.util.regex.Pattern;
import java.util.stream.Stream;

//? if >= 1.19.4 {
import net.minecraft.core.HolderOwner;
 //? }

public abstract class RegexMatchHolderSet<T> extends StreamBackedHolderSet<T> {

    private final HolderLookup.RegistryLookup<T> registryLookup;
    private final String regex;
    private Pattern pattern;

    public RegexMatchHolderSet(HolderLookup.RegistryLookup<T> registryLookup, String regex) {
        this.registryLookup = registryLookup;
        this.regex = regex;
    }

    public final HolderLookup.RegistryLookup<T> registryLookup() {
        return this.registryLookup;
    }

    public final String regex() {
        return this.regex;
    }

    private Pattern getPattern() {
        if (this.pattern == null) {
            this.pattern = Pattern.compile(this.regex);
        }
        return this.pattern;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Stream<Holder<T>> stream() {
        return (Stream<Holder<T>>) (Stream<?>) this.registryLookup.listElements()
                //? if < 1.19.4 {
                /*.map(key -> this.registryLookup.get(key).orElse(null))
                .filter(Objects::nonNull)
                *///? }
                .filter(holder ->
                    this.getInput(holder).anyMatch(input ->
                            this.getPattern().matcher(input).matches())
                );
    }

    //? if >= 1.19.4 {
    @Override
    public boolean canSerializeIn(HolderOwner<T> owner) {
        return this.registryLookup.canSerializeIn(owner);
    }
    //? } else {
    /*@Override
    public boolean isValidInRegistry(Registry<T> registry) {
        return true;
    }
    *///? }

    protected abstract Stream<String> getInput(Holder<T> holder);
}
