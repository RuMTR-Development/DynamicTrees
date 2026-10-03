//? if < 1.19.2 {
package com.dtteam.dynamictrees.worldgen.holderset;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;

import java.util.List;
import java.util.stream.Stream;

public class OrHolderSet<T> extends StreamBackedHolderSet<T> {

    private final List<HolderSet<T>> values;

    public OrHolderSet(List<HolderSet<T>> values) {
        this.values = values;
    }

    @Override
    public Stream<Holder<T>> stream() {
        return this.values.stream().flatMap(HolderSet::stream).distinct();
    }

    @Override
    public boolean isValidInRegistry(Registry<T> registry) {
        return this.values.stream().allMatch(set -> set.isValidInRegistry(registry));
    }
}
//? }
