package com.dtteam.dynamictrees.data.generator;

import com.dtteam.dynamictrees.block.branch.BranchBlock;
import com.dtteam.dynamictrees.data.DTDataProvider;
import com.dtteam.dynamictrees.data.Generator;
import com.dtteam.dynamictrees.data.builder.BranchLoaderBuilder;
import com.dtteam.dynamictrees.data.provider.DTBlockStateProvider;
import com.dtteam.dynamictrees.tree.family.Family;
import net.minecraft.world.level.block.Block;

import java.util.Objects;

//? if >= 1.19.4 {
/*import net.minecraft.core.registries.BuiltInRegistries;
*///? } else {
import net.minecraftforge.registries.ForgeRegistries;
//? }

/**
 * @author Harley O'Connor
 */
public class BranchStateGenerator implements Generator<DTDataProvider.BlockState, Family> {

    public static final DependencyKey<BranchBlock> BRANCH = new DependencyKey<>("branch");
    public static final DependencyKey<Block> PRIMITIVE_LOG = new DependencyKey<>("primitive_log");

    @Override
    public void generate(DTDataProvider.BlockState prov, Family input, Dependencies dependencies) {
        if (prov instanceof DTBlockStateProvider provider) {
            final BranchBlock branch = dependencies.get(BRANCH);
            final BranchLoaderBuilder builder = provider.models().getBuilder(
                    //~ if < 1.19.4 'BuiltInRegistries.BLOCK' -> 'ForgeRegistries.BLOCKS'
                    Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(branch)).getPath()
            ).customLoader(BranchLoaderBuilder.branchBuilders.get(input.getBranchLoader()));
            Block block = dependencies.get(PRIMITIVE_LOG);
            //~ if < 1.19.4 'BuiltInRegistries.BLOCK' -> 'ForgeRegistries.BLOCKS'
            input.addBranchTextures(builder::texture, provider.block(ForgeRegistries.BLOCKS.getKey(block)), block);
            provider.simpleBlock(branch, builder.end());
        }
    }

    @Override
    public Dependencies gatherDependencies(Family input) {
        return new Dependencies()
                .append(BRANCH, input.getBranch())
                .append(PRIMITIVE_LOG, input.getPrimitiveLog());
    }

}
