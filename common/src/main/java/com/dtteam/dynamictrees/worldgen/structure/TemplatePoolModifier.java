package com.dtteam.dynamictrees.worldgen.structure;

import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

//? if >= 1.21 {
import net.minecraft.data.worldgen.BootstrapContext;
 //? } else {
/*import net.minecraft.data.worldgen.BootstapContext;
*///? }

// @author Harley O'Connor
public interface TemplatePoolModifier {
    TemplatePoolModifier replaceTemplate(int index, StructurePoolElement element);

    TemplatePoolModifier removeTemplate(int index);

    void removeAllTemplates();

    void registerPool(
        //? if >= 1.21 {
        BootstrapContext<StructureTemplatePool> context
         //? } else {
        /*BootstapContext<StructureTemplatePool> context
        *///? }
    );
}
