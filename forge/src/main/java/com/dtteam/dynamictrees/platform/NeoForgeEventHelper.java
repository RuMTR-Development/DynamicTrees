package com.dtteam.dynamictrees.platform;

import com.dtteam.dynamictrees.api.registry.AbstractRegistry;
import com.dtteam.dynamictrees.api.registry.RegistryEntry;
import com.dtteam.dynamictrees.api.registry.TypedRegistry;
import com.dtteam.dynamictrees.api.resource.TreeResourceManager;
import com.dtteam.dynamictrees.api.resource.loading.StagedApplierResourceLoader;
import com.dtteam.dynamictrees.api.worldgen.PoissonDiscProvider;
import com.dtteam.dynamictrees.deserialization.JsonPropertyAppliers;
import com.dtteam.dynamictrees.deserialization.PropertyAppliers;
import com.dtteam.dynamictrees.event.*;
import com.dtteam.dynamictrees.item.Seed;
import com.dtteam.dynamictrees.platform.services.IEventHelper;
import com.dtteam.dynamictrees.systems.genfeature.context.PostGenerationContext;
import com.dtteam.dynamictrees.tree.species.Species;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.common.MinecraftForge;

public class NeoForgeEventHelper implements IEventHelper {

    @Override
    public <V extends RegistryEntry<V>> void postRegistryEvent(AbstractRegistry<V> registry) {
        ModLoader.get().postEvent(new RegistryEvent<>(registry));
    }

    @Override
    public <V extends RegistryEntry<V>> void postTypedRegistryEvent(TypedRegistry<V> registry) {
        ModLoader.get().postEvent(new TypeRegistryEvent<V>(registry));
    }

    @Override
    public void postAddResourceLoadersEventPre(TreeResourceManager resourceManager) {
        ModLoader.get().postEvent(new AddResourceLoadersEvent.Pre(resourceManager));
    }

    @Override
    public void postAddResourceLoadersEventPost(TreeResourceManager resourceManager) {
        ModLoader.get().postEvent(new AddResourceLoadersEvent.Post(resourceManager));
    }

    @Override
    public void postJsonDeserializerRegistryEvent() {
        ModLoader.get().postEvent(new JsonDeserializerRegistryEvent());
    }

    @Override
    public <O, I> void postApplierEvent(StagedApplierResourceLoader.ApplierStage stage, PropertyAppliers<O, I> appliers, String identifier) {
        switch (stage){
            case LOAD -> ModLoader.get().postEvent(new ApplierRegistryEvent.Load<>(appliers, identifier));
            case GATHER_DATA -> ModLoader.get().postEvent(new ApplierRegistryEvent.GatherData<>(appliers, identifier));
            case SETUP -> ModLoader.get().postEvent(new ApplierRegistryEvent.Setup<>(appliers, identifier));
            case RELOAD -> ModLoader.get().postEvent(new ApplierRegistryEvent.Reload<>(appliers, identifier));
            case COMMON -> ModLoader.get().postEvent(new ApplierRegistryEvent.Common<>(appliers, identifier));
        }
    }

    @Override
    public <O> void postBiomeEntryApplierEvent(JsonPropertyAppliers<O> appliers, String identifier) {
        ModLoader.get().postEvent(new BiomeEntryApplierRegistryEvent<>(appliers, identifier));
    }

    @Override
    public <O> void postCancellationApplierEvent(JsonPropertyAppliers<O> appliers, String identifier) {
        ModLoader.get().postEvent(new CancellationApplierRegistryEvent<>(appliers, identifier));
    }

    @Override
    public void postSpeciesPostGenerationEvent(PostGenerationContext context) {
        MinecraftForge.EVENT_BUS.post(new SpeciesPostGenerationEvent(context.level(), context.species(), context.pos(), context.endPoints(), context.initialDirtState()));
    }

    ///////////////////////////////////////////
    //CANCELLABLE EVENTS
    ///////////////////////////////////////////

    public boolean postTransitionSaplingToTreeEvent(Species species, Level level, BlockPos pos) {
        return MinecraftForge.EVENT_BUS.post(new TransitionSaplingToTreeEvent(species, level, pos));
    }

    @Override
    public boolean canCropGrow(Level level, BlockPos pos, BlockState state, boolean doGrow) {
            return ForgeHooks.onCropsGrowPre(level, pos, state, doGrow);
    }

    @Override
    public void cropGrowPost(Level level, BlockPos pos, BlockState state) {
        ForgeHooks.onCropsGrowPost(level, pos, state);
    }

    @Override
    public Species.BiomeSuitabilityEventResult postBiomeSuitabilityEvent(Level level, Biome biome, Species species, BlockPos pos) {
        BiomeSuitabilityEvent suitabilityEvent = new BiomeSuitabilityEvent(level, biome, species, pos);
        MinecraftForge.EVENT_BUS.post(suitabilityEvent);
        return new Species.BiomeSuitabilityEventResult(suitabilityEvent.isHandled(), suitabilityEvent.getSuitability());
    }

    public Seed.VoluntaryPlantEventResult postSeedVoluntaryPlantEvent (ItemEntity entityItem, Species species, BlockPos pos, boolean willPlant){
        SeedVoluntaryPlantEvent event = new SeedVoluntaryPlantEvent(entityItem, species, pos, willPlant);
        boolean canceled = MinecraftForge.EVENT_BUS.post(event);
        return new Seed.VoluntaryPlantEventResult(canceled, event.getWillPlant());
    }

    @Override
    public PoissonDiscProvider postPoissonDiscProviderCreateEvent(LevelAccessor level, PoissonDiscProvider poissonDiscProvider) {
        final PoissonDiscProviderCreateEvent poissonDiscProviderCreateEvent = new PoissonDiscProviderCreateEvent(level, poissonDiscProvider);
        MinecraftForge.EVENT_BUS.post(poissonDiscProviderCreateEvent);
        return poissonDiscProviderCreateEvent.getPoissonDiscProvider();
    }

}