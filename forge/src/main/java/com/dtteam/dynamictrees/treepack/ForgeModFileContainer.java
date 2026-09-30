package com.dtteam.dynamictrees.treepack;

import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.forgespi.locating.IModFile;
import org.jetbrains.annotations.NotNull;

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
        return Optional.of(modFile.findResource(strings));
    }

    @Override
    public @NotNull String getModId() {
        return modInfo.getModId();
    }
}
