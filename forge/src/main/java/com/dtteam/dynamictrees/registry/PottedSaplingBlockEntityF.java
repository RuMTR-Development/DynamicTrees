package com.dtteam.dynamictrees.registry;

import com.dtteam.dynamictrees.block.sapling.PottedSaplingBlockEntity;
import com.dtteam.dynamictrees.tree.species.Species;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.ModelDataManager;import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;

//? if >= 1.19.2 {
/*import net.minecraftforge.client.model.data.ModelData;
*///} else {
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
//? }

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
            //? if >= 1.19.2 {
            /*level.getModelDataManager().requestRefresh(this);
            *///? } else {
            ModelDataManager.requestModelDataRefresh(this);
            //? }

            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    @NotNull
    @Override
    //~ if < 1.19.2 'ModelData' -> 'IModelData'
    public IModelData getModelData() {
        //~ if < 1.19.2 'ModelData.builder' -> 'new ModelDataMap.Builder'
        //~ if < 1.19.2 '.with' -> '.withInitial'
        return new ModelDataMap.Builder().withInitial(POT_MIMIC, potState).withInitial(SPECIES, species).build();
    }

}
