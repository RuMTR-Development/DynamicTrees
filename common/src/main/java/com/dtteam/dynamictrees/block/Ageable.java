package com.dtteam.dynamictrees.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Random;

//? if >= 1.19.2 {
import net.minecraft.util.RandomSource;
//? }

@FunctionalInterface
public interface Ageable {

    /**
     * @param level The level
     * @param pos   the position of this block that is being aged
     * @param state the state of this block
     * @param rand  random number generator
     * @return -1 if block was destroyed after the ageing, otherwise the hydro value of the block
     */
    //~ if < 1.19.2 'RandomSource' -> 'Random'
    int age(LevelAccessor level, BlockPos pos, BlockState state, RandomSource rand, boolean worldgen);

}
