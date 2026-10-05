package com.dtteam.dynamictrees.data.generator;

import com.dtteam.dynamictrees.data.DTDataProvider;
import com.dtteam.dynamictrees.data.Generator;
import com.dtteam.dynamictrees.data.provider.DTItemModelProvider;
import com.dtteam.dynamictrees.tree.family.Family;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;

//? if >= 1.19.4 {
import net.minecraft.core.registries.BuiltInRegistries;
//? } else {
/*import net.minecraftforge.registries.ForgeRegistries;
*///? }

/**
 * @author Harley O'Connor
 */
public class BranchItemModelGenerator implements Generator<DTDataProvider.ItemModel, Family> {

    public static final DependencyKey<Block> PRIMITIVE_LOG_BLOCK = new DependencyKey<>("primitive_log_block");
    public static final DependencyKey<Item> PRIMITIVE_LOG_ITEM = new DependencyKey<>("primitive_log_item");

    @Override
    public void generate(DTDataProvider.ItemModel prov, Family input, Dependencies dependencies) {
        if (prov instanceof DTItemModelProvider provider){
            final ItemModelBuilder builder = provider.withExistingParent(
                    //~ if < 1.19.4 'BuiltInRegistries.ITEM' -> 'ForgeRegistries.ITEMS'
                    String.valueOf(BuiltInRegistries.ITEM.getKey(dependencies.get(PRIMITIVE_LOG_ITEM))),
                    input.getBranchItemParentLocation()
            );
            Block block = dependencies.get(PRIMITIVE_LOG_BLOCK);
            input.addBranchTextures(
                    builder::texture,
                    //~ if < 1.19.4 'BuiltInRegistries.BLOCK' -> 'ForgeRegistries.BLOCKS'
                    provider.block(BuiltInRegistries.BLOCK.getKey(block)),
                    block
            );
        }
    }

    @Override
    public Dependencies gatherDependencies(Family input) {
        return new Dependencies()
                .append(PRIMITIVE_LOG_BLOCK, input.getPrimitiveLog())
                .append(PRIMITIVE_LOG_ITEM, input.getBranchItem());
    }

}