package com.dtteam.dynamictrees.data.generator;

import com.dtteam.dynamictrees.data.DTDataProvider;
import com.dtteam.dynamictrees.data.Generator;
import com.dtteam.dynamictrees.data.provider.DTItemModelProvider;
import com.dtteam.dynamictrees.item.Seed;
import com.dtteam.dynamictrees.tree.species.Species;

//? if >= 1.19.4 {
import net.minecraft.core.registries.BuiltInRegistries;
//? } else {
/*import net.minecraftforge.registries.ForgeRegistries;
*///? }

/**
 * @author Harley O'Connor
 */
public class SeedItemModelGenerator implements Generator<DTDataProvider.ItemModel, Species> {

    public static final DependencyKey<Seed> SEED = new DependencyKey<>("seed");

    @Override
    public void generate(DTDataProvider.ItemModel prov, Species input, Dependencies dependencies) {
        if (prov instanceof DTItemModelProvider provider){
            final Seed seed = dependencies.get(SEED);
            //~ if < 1.19.4 'BuiltInRegistries.ITEM' -> 'ForgeRegistries.ITEMS' {
            provider.withExistingParent(String.valueOf(BuiltInRegistries.ITEM.getKey(seed)), seed.getSpecies().getSeedParentModelLocation())
                    .texture("layer0", seed.getSpecies().getTexturePath(Species.SEED).orElse(provider.item(BuiltInRegistries.ITEM.getKey(seed))));
            //~ }
        }
    }

    @Override
    public Dependencies gatherDependencies(Species input) {
        return new Dependencies()
                .append(SEED, input.getSeed());
    }

}
