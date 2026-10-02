package com.dtteam.dynamictrees.treepack;

import com.dtteam.dynamictrees.DynamicTrees;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.forgespi.Environment;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.forgespi.locating.IModFile;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class ForgeModFileContainer extends ModFileContainer {

    public final IModInfo modInfo;
    public final IModFile modFile;

    public ForgeModFileContainer(IModInfo modInfo) {
        this.modInfo = modInfo;
        this.modFile = modInfo.getOwningFile().getFile();
    }

    @Override
    public @NotNull Optional<Path> findResource(String strings) {
        //? if < 1.19.4 {
        if (!FMLEnvironment.production && modInfo.getModId().equals(DynamicTrees.MOD_ID)) {
            var result = FMLPaths.GAMEDIR.get()
                    .getParent()
                    .resolve("build")
                    .resolve("resources")
                    .resolve("main")
                    .resolve(strings);

            return Files.exists(result) ? Optional.of(result) : Optional.empty();
        }
        //? }

        return Optional.of(modFile.findResource(strings));
    }

    @Override
    public @NotNull String getModId() {
        return modInfo.getModId();
    }
}
