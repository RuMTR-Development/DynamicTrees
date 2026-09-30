package com.dtteam.dynamictrees.platform;

import com.dtteam.dynamictrees.platform.services.IPlatformHelper;
import com.dtteam.dynamictrees.treepack.ModFileContainer;
import com.dtteam.dynamictrees.treepack.ForgeModFileContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

import java.util.List;
import java.util.stream.Collectors;

public class NeoForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {

        return "MinecraftForge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }

    public List<ModFileContainer> getMods(){
        return ModList.get().getMods().stream().map((ForgeModFileContainer::new)).collect(Collectors.toList());
    }
}