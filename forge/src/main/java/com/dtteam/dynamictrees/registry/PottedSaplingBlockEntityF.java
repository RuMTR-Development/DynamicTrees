package com.dtteam.dynamictrees.registry;

import com.dtteam.dynamictrees.block.sapling.PottedSaplingBlockEntity;
import com.dtteam.dynamictrees.tree.species.Species;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;

public class PottedSaplingBlockEntityF extends PottedSaplingBlockEntity {

    public PottedSaplingBlockEntityF(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    public static final ModelProperty<BlockState> POT_MIMIC = new ModelProperty<>();
    public static final ModelProperty<Species> SPECIES = new ModelProperty<>();

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        BlockState oldPotState = potState;
        this.handleUpdateTag(pkt.getTag());

        if (!oldPotState.equals(potState) && level != null) {
            level.getModelDataManager().requestRefresh(this);
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    @NotNull
    @Override
    public ModelData getModelData() {
        return ModelData.builder().with(POT_MIMIC, potState).with(SPECIES, species).build();
    }

}
