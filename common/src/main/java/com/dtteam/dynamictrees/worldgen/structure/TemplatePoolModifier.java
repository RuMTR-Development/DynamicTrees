package com.dtteam.dynamictrees.worldgen.structure;

import com.dtteam.dynamictrees.data.CustomBootstrapContext;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

//? if >= 1.21 {
import net.minecraft.data.worldgen.BootstrapContext;
//? } else if >= 1.19.4 {
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
         //? } else if >= 1.19.4 {
        /*BootstapContext<StructureTemplatePool> context
        *///? } else {
        /*CustomBootstrapContext<StructureTemplatePool>context
        *///? }
    );
}
