package com.dtteam.dynamictrees.data.generator;

import com.dtteam.dynamictrees.data.DTDataProvider;
import com.dtteam.dynamictrees.data.Generator;
import com.dtteam.dynamictrees.data.provider.DTLangProvider;
import com.dtteam.dynamictrees.tree.species.Species;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Block;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

//? if >= 1.19.4 {
/*import net.minecraft.core.registries.BuiltInRegistries;
*///? } else {
import net.minecraftforge.registries.ForgeRegistries;
//? }

public class SpeciesLangGenerator implements Generator<DTDataProvider.Language, Species> {
    DTLangProvider provider;

    @Override
    public void generate(DTDataProvider.Language prov, Species input, Dependencies dependencies) {
        if (prov instanceof DTLangProvider provider1){
            this.provider = provider1;
            speciesLang(input, input.getLangOverride("species"));
            if(input.hasSeed()) {
                itemLang(input.getSeed().get(), input.getLangOverride("seed"));
            }
            //input.getSapling().ifPresent(sapling -> blockLang(sapling, input.getLangOverride("sapling")));
        }

    }

    @Override
    public Dependencies gatherDependencies(Species input) {
        return new Dependencies();
    }
    protected void itemLang(Item entry, Optional<String> override) {
        if (!(entry instanceof BlockItem) || entry instanceof ItemNameBlockItem) {
            //~ if < 1.19.4 'BuiltInRegistries.ITEM' -> 'ForgeRegistries.ITEMS'
            provider.addItem(() -> entry, override.orElse(checkReplace(ForgeRegistries.ITEMS.getKey(entry))));
        }
    }

    protected void speciesLang(Species entry, Optional<String> override) {
        provider.add(entry.getLocalizedName(), override.orElse(checkReplace(entry.getRegistryName())));
    }

    protected void blockLang(Block entry, Optional<String> blah) {
        //~ if < 1.19.4 'BuiltInRegistries.BLOCK' -> 'ForgeRegistries.BLOCKS'
        provider.addBlock(() -> entry, blah.orElse(checkReplace(ForgeRegistries.BLOCKS.getKey(entry))));
    }

    protected String checkReplace(ResourceLocation registryObject) {
        return Arrays.stream(registryObject.getPath().split("_"))
                .map(StringUtils::capitalize)
                .filter(s -> !s.isBlank())
                .collect(Collectors.joining(" "))
                .trim();
    }
}
