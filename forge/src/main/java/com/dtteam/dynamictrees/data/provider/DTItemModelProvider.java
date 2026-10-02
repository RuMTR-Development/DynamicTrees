package com.dtteam.dynamictrees.data.provider;

import com.dtteam.dynamictrees.api.registry.Registry;
import com.dtteam.dynamictrees.data.DTDataProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.List;

//? if >= 1.19.4 {
/*import net.minecraft.data.PackOutput;
*///? }

/**
 * @author Harley O'Connor
 */
public class DTItemModelProvider extends ItemModelProvider implements DTDataProvider.ItemModel {

    private final List<Registry<?>> registries;

    //? if >= 1.19.4 {
    /*public DTItemModelProvider(PackOutput output, String modId, ExistingFileHelper fileHelper, List<Registry<?>> registries) {
        super(output, modId, fileHelper);
        this.registries = registries;
    }
    *///? } else {
    public DTItemModelProvider(DataGenerator generator, String modId, ExistingFileHelper existingFileHelper, List<Registry<?>> registries) {
        super(generator, modId, existingFileHelper);
        this.registries = registries;
    }
    //? }

    @Override
    protected void registerModels() {
        this.registries.forEach(registry ->
                registry.dataGenerationStream(this.modid).forEach(entry ->
                        entry.generateItemModelData(this)
                )
        );
    }

}
