package com.dtteam.dynamictrees.platform;

import com.dtteam.dynamictrees.api.registry.RegistryHandler;
import com.dtteam.dynamictrees.block.sapling.PottedSaplingBlockEntity;
import com.dtteam.dynamictrees.platform.services.IRegistryHelper;
import com.dtteam.dynamictrees.registry.ForgeRegistryHandler;
import com.dtteam.dynamictrees.registry.ForgeRegistryLoader;
import com.dtteam.dynamictrees.registry.PottedSaplingBlockEntityF;
import com.dtteam.dynamictrees.registry.RegistryLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class NeoForgeRegistryHelper implements IRegistryHelper {

    private static RegistryLoader registriesInstance;
    @Override
    public RegistryLoader getRegistryLoader() {
        if (registriesInstance == null){
            registriesInstance = new ForgeRegistryLoader();
        }
        return registriesInstance;
    }

    @Override
    public RegistryHandler newRegistryHandler() {
        return new ForgeRegistryHandler();
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<PottedSaplingBlockEntity> getPottedSaplingBlockEntity() {
        return PottedSaplingBlockEntityF::new;
    }

    @Override
    public PottedSaplingBlockEntity newPottedSaplingBlockEntity(BlockPos pPos, BlockState pState) {
        return new PottedSaplingBlockEntityF(pPos, pState);
    }

}