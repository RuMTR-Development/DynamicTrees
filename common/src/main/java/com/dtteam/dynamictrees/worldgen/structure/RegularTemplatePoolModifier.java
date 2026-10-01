package com.dtteam.dynamictrees.worldgen.structure;

import com.dtteam.dynamictrees.data.CustomBootstrapContext;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;import net.minecraft.core.RegistryAccess;import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

//? if >= 1.21 {
import net.minecraft.data.worldgen.BootstrapContext;
//? } else if >= 1.19.4 {
/*import net.minecraft.data.worldgen.BootstapContext;
*///? }

//? if >= 1.19.4 {
import net.minecraft.core.registries.Registries;
//? }

// @author Harley O'Connor
public class RegularTemplatePoolModifier implements TemplatePoolModifier {
    private final ResourceKey<StructureTemplatePool> key;
    private final StructureTemplatePool templatePool;

    private RegularTemplatePoolModifier(ResourceKey<StructureTemplatePool> key, StructureTemplatePool templatePool) {
        this.key = key;
        this.templatePool = templatePool;
    }

    public TemplatePoolModifier replaceTemplate(int index, StructurePoolElement element) {
        if (templatePool.rawTemplates.size() <= index) return this;
        Pair<StructurePoolElement, Integer> removedRawTemplate = templatePool.rawTemplates.remove(index);
        var elementFinal = new DTCancelVanillaTreePoolElement(element, removedRawTemplate.getFirst());
        templatePool.rawTemplates.add(index, Pair.of(elementFinal, removedRawTemplate.getSecond()));
        templatePool.templates.replaceAll(template -> {
            if (template == removedRawTemplate.getFirst()) {
                return elementFinal;
            }
            return template;
        });
        return this;
    }

    @Override
    public TemplatePoolModifier removeTemplate(int index) {
        Pair<StructurePoolElement, Integer> removedRawTemplate = templatePool.rawTemplates.remove(index);
        templatePool.templates.removeIf(template -> template == removedRawTemplate.getFirst());
        return this;
    }

    @Override
    public void removeAllTemplates() {
        templatePool.rawTemplates.clear();
        templatePool.templates.clear();
    }

    @Override
    public void registerPool(
            //? if >= 1.21 {
            BootstrapContext<StructureTemplatePool> context
            //? } else if >= 1.19.4 {
            /*BootstapContext<StructureTemplatePool> context
            *///? } else {
            /*CustomBootstrapContext<StructureTemplatePool> context
            *///? }
    ) {
        context.register(this.key, this.templatePool);
    }

    public static TemplatePoolModifier village(
            //? if >= 1.19.4
             HolderLookup.Provider lookupProvider,

            String type,
            String patternGroup
    ) {
        //? if >= 1.21 {
        ResourceLocation patternName = ResourceLocation.parse("village/" + type + "/" + patternGroup);
        //? } else {
        /*ResourceLocation patternName = new ResourceLocation("village/" + type + "/" + patternGroup);
        *///? }

        return create(
                //? if >= 1.19.4 {
                lookupProvider,
                ResourceKey.create(Registries.TEMPLATE_POOL, patternName)
                //? } else {
                /*ResourceKey.create(Registry.TEMPLATE_POOL_REGISTRY, patternName)
                *///? }
        );
    }

    public static TemplatePoolModifier create(
            //? if >= 1.19.4
             HolderLookup.Provider lookupProvider,

            ResourceKey<StructureTemplatePool> key
    ) {
        //? if >= 1.19.4 {
        StructureTemplatePool pattern = lookupProvider.lookupOrThrow(Registries.TEMPLATE_POOL).getOrThrow(key).value();
        //? } else {
        /*StructureTemplatePool pattern = RegistryAccess.BUILTIN.get().registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY).getOrThrow(key);
        *///? }

        // if (pattern == null) {
        //     VillageTreeReplacement.LOGGER.error("Could not find StructureTemplatePool with name {}.", patternName);
        //     return TemplatePoolModifier.NULL;
        // }
        return new RegularTemplatePoolModifier(key, new StructureTemplatePool(
                //? if < 1.19.4
                //pattern.getName(),

                pattern.getFallback(),
                pattern.rawTemplates
        ));
    }

}
