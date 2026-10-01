package com.dtteam.dynamictrees.loot;

import com.dtteam.dynamictrees.tree.species.Species;
import com.dtteam.dynamictrees.utility.ResourceLocationUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;

//? if >= 1.21.1 {
import net.minecraft.server.ReloadableServerRegistries;
import net.minecraft.core.registries.Registries;
//?} else {
/*import net.minecraft.world.level.storage.loot.LootTables;
*///?}


/**
 * @author Harley O'Connor
 */
public final class LootTableSupplier {

    private final String path;
    private final ResourceLocation name;
    private final ResourceLocation baseName;

    public LootTableSupplier(String basePath, ResourceLocation name) {
        this.path = basePath;
        this.name = name;
        this.baseName = ResourceLocationUtils.prefix(name, path);
    }

    //? if >= 1.21.1 {
    public LootTable get(ReloadableServerRegistries.Holder lootTables, Species species) {
        final LootTable speciesOverrideTable = lootTables.getLootTable(ResourceKey.create(Registries.LOOT_TABLE, getName(species)));
        if (speciesOverrideTable != LootTable.EMPTY) {
            return speciesOverrideTable;
        }
        return lootTables.getLootTable(ResourceKey.create(Registries.LOOT_TABLE, baseName));
    }//?} else {
    /*public LootTable get(LootTables lootTables, Species species) {
        final LootTable speciesOverrideTable = lootTables.get(getName(species));
        if (speciesOverrideTable != LootTable.EMPTY) {
            return speciesOverrideTable;
        }
        return lootTables.get(baseName);
    }
    *///?}

    public ResourceLocation getName(Species species) {
        final ResourceLocation speciesName = species.getRegistryName();
        return ResourceLocationUtils.surround(name, path, "/" + speciesName.getNamespace() + "/" + speciesName.getPath());
    }

    public ResourceLocation getName() {
        return baseName;
    }


}
