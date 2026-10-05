package com.dtteam.dynamictrees.worldgen.holderset;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

//? if >= 1.19.4 {
import net.minecraft.core.HolderOwner;
//? }

//? if >= 1.19.2 {
import net.minecraft.util.RandomSource;
//? }

public class DelayedHolderSet<T> implements HolderSet<T> {
    private final Supplier<HolderSet<T>> holderSetSupplier;

    public DelayedHolderSet(Supplier<HolderSet<T>> holderSetSupplier)
    {
        this.holderSetSupplier = holderSetSupplier;
    }

    @Override
    public Stream<Holder<T>> stream() {
        return this.holderSetSupplier.get().stream();
    }

    @Override
    public int size() {
        return this.holderSetSupplier.get().size();
    }

    @Override
    public Either<TagKey<T>, List<Holder<T>>> unwrap() {
        return this.holderSetSupplier.get().unwrap();
    }

    @Override
    //~ if < 1.19.2 'RandomSource random' -> 'Random random'
    public Optional<Holder<T>> getRandomElement(RandomSource random) {
        return this.holderSetSupplier.get().getRandomElement(random);
    }

    @Override
    public Holder<T> get(int index) {
        return this.holderSetSupplier.get().get(index);
    }

    @Override
    public boolean contains(Holder<T> holder) {
        return this.holderSetSupplier.get().contains(holder);
    }

    //? if >= 1.19.4 {
    @Override
    public boolean canSerializeIn(HolderOwner<T> owner) {
        return this.holderSetSupplier.get().canSerializeIn(owner);
    }

    @Override
    public Optional<TagKey<T>> unwrapKey() {
        return this.holderSetSupplier.get().unwrapKey();
    }
    //? } else {
    /*@Override
    public boolean isValidInRegistry(Registry<T> registry) {
        return this.holderSetSupplier.get().isValidInRegistry(registry);
    }
    *///? }

    @NotNull
    @Override
    public Iterator<Holder<T>> iterator() {
        return this.holderSetSupplier.get().iterator();
    }

    @Override
    public void forEach(Consumer<? super Holder<T>> action) {
        this.holderSetSupplier.get().forEach(action);
    }

    @Override
    public Spliterator<Holder<T>> spliterator() {
        return this.holderSetSupplier.get().spliterator();
    }

//    @Override
//    public void addInvalidationListener(Runnable runnable) {
//        this.holderSetSupplier.get().addInvalidationListener(runnable);
//    }
//
//    @Override
//    public SerializationType serializationType() {
//        return this.holderSetSupplier.get().serializationType();
//    }
}