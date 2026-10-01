package com.dtteam.dynamictrees.data;

import net.minecraft.resources.ResourceKey;

public interface CustomBootstrapContext<T> {
    T register(ResourceKey<T> key, T value);
}
