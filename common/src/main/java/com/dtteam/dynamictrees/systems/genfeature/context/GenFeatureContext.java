package com.dtteam.dynamictrees.systems.genfeature.context;

import com.dtteam.dynamictrees.api.worldgen.LevelContext;
import com.dtteam.dynamictrees.tree.species.Species;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import java.util.Random;

//? if >= 1.19.2 {
import net.minecraft.util.RandomSource;
//? }

/**
 * @author Harley O'Connor
 */
public abstract class GenFeatureContext {

    private final LevelContext levelContext;
    private final BlockPos pos;
    private final Species species;

    public GenFeatureContext(LevelAccessor level, BlockPos pos, Species species) {
        this.levelContext = LevelContext.create(level);
        this.pos = pos;
        this.species = species;
    }

    public LevelContext levelContext() {
        return levelContext;
    }

    public LevelAccessor level() {
        return levelContext.accessor();
    }

    public BlockPos pos() {
        return pos;
    }

    public Species species() {
        return species;
    }

    //~ if < 1.19.2 'RandomSource' -> 'Random'
    public final RandomSource random() {
        return this.level().getRandom();
    }

}
