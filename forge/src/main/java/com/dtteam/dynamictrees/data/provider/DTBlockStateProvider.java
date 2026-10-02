package com.dtteam.dynamictrees.data.provider;

import com.dtteam.dynamictrees.api.registry.Registry;
import com.dtteam.dynamictrees.data.DTDataProvider;
import com.google.common.collect.ImmutableList;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Collection;
import java.util.List;

//? if >= 1.19.4 {
/*import net.minecraft.data.PackOutput;
*///? }

/**
 * @author Harley O'Connor
 */
public class DTBlockStateProvider extends BlockStateProvider implements DTDataProvider.BlockState {

    private final String modId;
    private final List<Registry<?>> registries;

    //? if >= 1.19.4 {
    /*public DTBlockStateProvider(PackOutput output, String modId, ExistingFileHelper fileHelper,
                                Collection<Registry<?>> registries) {
        super(output, modId, fileHelper);
        this.modId = modId;
        this.registries = ImmutableList.copyOf(registries);
    }
    *///? } else {
    public DTBlockStateProvider(DataGenerator gen, String modId, ExistingFileHelper exFileHelper, List<Registry<?>> registries) {
        super(gen, modId, exFileHelper);
        this.modId = modId;
        this.registries = registries;
    }
    //? }

    @Override
    protected void registerStatesAndModels() {
        this.registries.forEach(registry ->
                registry.dataGenerationStream(this.modId).forEach(entry ->
                        entry.generateStateData(this)
                )
        );
    }

}
