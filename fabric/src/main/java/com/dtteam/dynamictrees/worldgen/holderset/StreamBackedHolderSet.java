package com.dtteam.dynamictrees.worldgen.holderset;

import com.mojang.datafixers.util.Either;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.tags.TagKey;

import java.util.*;
import java.util.stream.Collectors;

//? if >= 1.19.2 {
import net.minecraft.util.RandomSource;
//? }

public abstract class StreamBackedHolderSet<T> implements HolderSet<T> {
    public List<Holder<T>> contents() {
        return this.stream().collect(Collectors.toList());
    }

    public Set<Holder<T>> contentsSet() {
        return this.stream().collect(Collectors.toSet());
    }

    @Override
    public int size() {
        return this.contents().size();
    }

    @Override
    public Spliterator<Holder<T>> spliterator() {
        return this.stream().spliterator();
    }

    @Override
    public Iterator<Holder<T>> iterator() {
        return this.stream().iterator();
    }

    @Override
    //~ if < 1.19.2 'RandomSource' -> 'Random'
    public Optional<Holder<T>> getRandomSourceElement(RandomSource random) {
        return Util.getRandomSafe(this.contents(), random);
    }

    @Override
    public Holder<T> get(int index) {
        return this.contents().get(index);
    }

    @Override
    public Either<TagKey<T>, List<Holder<T>>> unwrap() {
        return Either.right(this.contents());
    }

    @Override
    public boolean contains(Holder<T> holder) {
        return this.stream().anyMatch(h -> Objects.equals(h, holder));
    }

    //? if >= 1.19.4 {
    @Override
    public Optional<TagKey<T>> unwrapKey() {
        return Optional.empty();
    }
    //? }
}
