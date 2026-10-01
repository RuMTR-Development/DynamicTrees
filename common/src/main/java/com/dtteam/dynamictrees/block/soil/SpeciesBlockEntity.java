package com.dtteam.dynamictrees.block.soil;

import com.dtteam.dynamictrees.registry.DTRegistries;
import com.dtteam.dynamictrees.tree.species.Species;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A TileEntity that holds a species value.
 *
 * @author ferreusveritas
 */
public class SpeciesBlockEntity extends BlockEntity {

    private Species species = Species.NULL_SPECIES;

    public SpeciesBlockEntity(BlockPos pos, BlockState state) {
        super(DTRegistries.SPECIES_BLOCK_ENTITY.get(), pos, state);
    }

    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
        this.setChanged();
    }

    //? if >= 1.21.1 {
    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        if (tag.contains("species")) {
            ResourceLocation speciesName = ResourceLocation.parse(tag.getString("species"));
            species = Species.findSpecies(speciesName);
        }
        super.loadAdditional(tag, registries);
    }
    //? } else {
    /*@Override
    public void load(CompoundTag tag) {
        if (tag.contains("species")) {
            //? if >= 1.21.1 {
            ResourceLocation speciesName = ResourceLocation.parse(tag.getString("species"));
            //? } else {
            /^ResourceLocation speciesName = new ResourceLocation(tag.getString("species"));
            ^///? }

            species = Species.findSpecies(speciesName);
        }

        super.load(tag);
    }
    *///? }

    //? if >= 1.21.1 {
    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putString("species", species.getRegistryName().toString());
    }
    //? } else {
    /*@Override
    protected void saveAdditional(CompoundTag tag) {
        tag.putString("species", species.getRegistryName().toString());
    }
    *///? }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    //? if >= 1.21.1 {
    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        this.saveAdditional(tag, registries);
        return tag;
    }
    //? } else {
    /*@Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        this.saveAdditional(tag);
        return tag;
    }
    *///? }

    //    @Override
//    public void onDataPacket(Connection connection, ClientboundBlockEntityDataPacket packet) {
//        load(packet.getTag());
//    }
//

}
