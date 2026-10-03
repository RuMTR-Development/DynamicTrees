package com.dtteam.dynamictrees.worldgen.holderset;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Stream;

//? if >= 1.19.4 {
/*import net.minecraft.core.HolderOwner;
import net.minecraft.core.HolderLookup;
*///? }

//? if >= 1.19.2 {
/*import net.minecraftforge.registries.holdersets.ICustomHolderSet;
*///? }

public abstract class RegexMatchHolderSet<T>
        extends StreamBackedHolderSet<T>

        //? if >= 1.19.2
        //implements ICustomHolderSet<T>
{
    //? if >= 1.19.2 {
    /*protected static <T> MapCodec<? extends ICustomHolderSet<T>> mapCodec(
            ResourceKey<? extends Registry<T>> registryKey,

            //? if >= 1.19.4 {
            BiFunction<HolderLookup.RegistryLookup<T>, String, RegexMatchHolderSet<T>> factory
            //? } else {
            BiFunction<Registry<T>, String, RegexMatchHolderSet<T>> factory
            //? }
    ) {
        return RecordCodecBuilder.<RegexMatchHolderSet<T>>mapCodec(builder -> builder.group(
                //? if >= 1.19.4 {
                RegistryOps.retrieveRegistryLookup(registryKey).forGetter(RegexMatchHolderSet::registryLookup),
                //? } else {
                RegistryOps.retrieveRegistry(registryKey).forGetter(RegexMatchHolderSet::registry),
                //? }

                Codec.STRING.fieldOf("regex").forGetter(RegexMatchHolderSet::regex)
        ).apply(builder, factory));
    }
    *///? }

    //? if >= 1.19.4 {
    /*private final HolderLookup.RegistryLookup<T> registryLookup;
    *///? } else {
    private final Supplier<Registry<T>> registrySupplier;
    //? }

    private final String regex;
    private Pattern pattern;

    //? if >= 1.19.4 {
    /*public RegexMatchHolderSet(HolderLookup.RegistryLookup<T> registryLookup, String regex) {
        this.registryLookup = registryLookup;
        this.regex = regex;
    }

    public final HolderLookup.RegistryLookup<T> registryLookup() {
        return this.registryLookup;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Stream<Holder<T>> stream() {
        return (Stream<Holder<T>>) (Stream<?>) this.registryLookup.listElements()
                .filter(holder ->
                        this.getInput(holder).anyMatch(input ->
                                this.getPattern().matcher(input).matches())
                );
    }

    @Override
    public boolean canSerializeIn(HolderOwner<T> owner) {
        return this.registryLookup.canSerializeIn(owner);
    }
    *///? } else {
    public RegexMatchHolderSet(Registry<T> registry, String regex) {
        this(() -> registry, regex);
    }

    public RegexMatchHolderSet(Supplier<Registry<T>> registrySupplier, String regex) {
        this.registrySupplier = registrySupplier;
        this.regex = regex;
    }

    public final Supplier<Registry<T>> registrySupplier() {
        return this.registrySupplier;
    }

    public Registry<T> registry() {
        return this.registrySupplier.get();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Stream<Holder<T>> stream() {
        return (Stream<Holder<T>>) (Stream<?>) this.registry().holders().filter(holder -> this.getInput(holder).anyMatch(input -> this.getPattern().matcher(input).matches()));
    }

    @Override
    public boolean isValidInRegistry(Registry<T> registry) {
        return true;
    }
    //? }

    public final String regex() {
        return this.regex;
    }

    private Pattern getPattern() {
        if (this.pattern == null) {
            this.pattern = Pattern.compile(this.regex);
        }

        return this.pattern;
    }

    /**
     * Gets the stream of input data from the holder to use for regex matching.
     * If any string matches, the holder will be included in the set.
     */
    protected abstract Stream<String> getInput(Holder<T> holder);
}