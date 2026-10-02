package com.dtteam.dynamictrees.data.generator;

import com.dtteam.dynamictrees.block.leaves.DynamicLeavesBlock;
import com.dtteam.dynamictrees.block.leaves.LeavesProperties;
import com.dtteam.dynamictrees.data.DTDataProvider;
import com.dtteam.dynamictrees.data.Generator;
import com.dtteam.dynamictrees.data.provider.DTBlockStateProvider;
import net.minecraft.world.level.block.Block;

//? if >= 1.19.4 {
/*import net.minecraft.core.registries.BuiltInRegistries;
*///? } else {
import net.minecraftforge.registries.ForgeRegistries;
//? }

/**
 * @author Harley O'Connor
 */
public class LeavesStateGenerator implements Generator<DTDataProvider.BlockState, LeavesProperties> {

    public static final DependencyKey<DynamicLeavesBlock> LEAVES = new DependencyKey<>("leaves");
    public static final DependencyKey<Block> PRIMITIVE_LEAVES = new DependencyKey<>("primitive_leaves");

    @Override
    public void generate(DTDataProvider.BlockState prov, LeavesProperties input, Dependencies dependencies) {
        if (prov instanceof DTBlockStateProvider provider){
            provider.simpleBlock(dependencies.get(LEAVES), provider.models().getExistingFile(
                    input.getModelPath(LeavesProperties.LEAVES).orElse(
                            //~ if < 1.19.4 'BuiltInRegistries.BLOCK' -> 'ForgeRegistries.BLOCKS'
                            provider.block(ForgeRegistries.BLOCKS.getKey(dependencies.get(PRIMITIVE_LEAVES)))
                    )
            ));
        }
    }

    @Override
    public Dependencies gatherDependencies(LeavesProperties input) {
        return new Dependencies()
                .append(LEAVES, input.getDynamicLeavesBlock())
                .append(PRIMITIVE_LEAVES, input.getPrimitiveLeavesBlock());
    }

}
