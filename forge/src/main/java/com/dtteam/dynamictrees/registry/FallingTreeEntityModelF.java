package com.dtteam.dynamictrees.registry;

import com.dtteam.dynamictrees.api.network.BranchDestructionData;
import com.dtteam.dynamictrees.block.branch.BranchBlock;
import com.dtteam.dynamictrees.block.soil.SoilBlock;
import com.dtteam.dynamictrees.entity.FallingTreeEntity;
import com.dtteam.dynamictrees.model.FallingTreeEntityModel;
import com.dtteam.dynamictrees.model.QuadManipulator;
import com.dtteam.dynamictrees.model.modeldata.ModelConnections;
import com.dtteam.dynamictrees.tree.TreeHelper;
import com.dtteam.dynamictrees.tree.species.Species;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

//? if >= 1.19.4 {
import net.minecraft.core.registries.BuiltInRegistries;
//? } else {
/*import net.minecraftforge.registries.ForgeRegistries;
*///? }

//? if >= 1.19.2 {
import net.minecraftforge.client.model.data.ModelData;
//? } else {
/*import net.minecraftforge.client.model.data.EmptyModelData;
*///? }

public class FallingTreeEntityModelF extends FallingTreeEntityModel {

    public FallingTreeEntityModelF(FallingTreeEntity entity) {
        super(entity);
    }

    @Override
    public List<TreeQuadData> generateTreeQuads(FallingTreeEntity entity) {
        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
        BranchDestructionData destructionData = entity.getDestroyData();
        Direction cutDir = destructionData.cutDir;

        ArrayList<TreeQuadData> treeQuads = new ArrayList<>();

        int[] connectionArray = new int[6];

        if (destructionData.getNumBranches() > 0) {
            BlockState exState = destructionData.getBranchBlockState(0);
            BlockPos cutPos = destructionData.cutPos;
            if (exState != null) {
                Species species = destructionData.species;

                //Draw the rooty block if it is set to fall too
                boolean rootyBlockAdded = false;
                if (destructionData.soilState != null){
                    //~ if < 1.19.4 'BuiltInRegistries.BLOCK.get' -> 'ForgeRegistries.BLOCKS.getValue'
                    SoilBlock soilBlock = TreeHelper.getRooty(BuiltInRegistries.BLOCK.get(destructionData.soilState.getLeft()));
                    if (soilBlock != null) {
                        BlockState soilState = soilBlock.GetStateFromIndex(destructionData.soilState.getRight());
                        BakedModel rootyModel = dispatcher.getBlockModel(soilState);
                        BlockPos cutOffset = destructionData.getRelativeCutPos();
                        //~ if < 1.19.2 'ModelData.EMPTY' -> 'EmptyModelData.INSTANCE'
                        treeQuads.addAll(toTreeQuadData(QuadManipulator.getQuads(rootyModel, soilState, new Vec3(cutOffset.getX(), cutOffset.getY()-1, cutOffset.getZ()), entity.level.getRandom(), ModelData.EMPTY),
                                destructionData.species.getFamily().getRootColor(soilState, soilBlock.getColorFromBark()),
                                soilState));
                        rootyBlockAdded = true;
                    }

                }

                BakedModel branchModel = dispatcher.getBlockModel(exState);
                //Draw the ring texture cap on the cut block if the bottom connection is above 0
                destructionData.getConnections(0, connectionArray);
                boolean bottomRingsAdded = false;
                if (!rootyBlockAdded && connectionArray[cutDir.get3DDataValue()] > 0) {
                    BlockPos offsetPos = destructionData.getRelativeCutPos().relative(cutDir);
                    float offset = (8 - Math.min(((BranchBlock) exState.getBlock()).getRadius(exState), BranchBlock.MAX_RADIUS)) / 16f;
                    treeQuads.addAll(toTreeQuadData(QuadManipulator.getQuads(branchModel, exState, new Vec3(offsetPos.getX(), offsetPos.getY(), offsetPos.getZ()).scale(offset), new Direction[]{null}, entity.level.getRandom(),
                                    new ModelConnections(cutDir).setFamily(TreeHelper.getBranch(exState)).toModelData()),
                            exState));
                    bottomRingsAdded = true;
                }

                //Draw the rest of the tree/branch
                for (int index = 0; index < destructionData.getNumBranches(); index++) {
                    Block previousBranch = exState.getBlock();
                    exState = destructionData.getBranchBlockState(index);
                    if (!previousBranch.equals(exState.getBlock())) //Update the branch model only if the block is different
                    {
                        branchModel = dispatcher.getBlockModel(exState);
                    }
                    BlockPos relPos = destructionData.getBranchRelPos(index);
                    destructionData.getConnections(index, connectionArray);
                    ModelConnections modelConnections = new ModelConnections(connectionArray).setFamily(TreeHelper.getBranch(exState));
                    if (index == 0 && bottomRingsAdded) {
                        modelConnections.setForceRing(cutDir);
                    }
                    treeQuads.addAll(toTreeQuadData(QuadManipulator.getQuads(branchModel, exState, new Vec3(relPos.getX(), relPos.getY(), relPos.getZ()), entity.level.getRandom(), modelConnections.toModelData()),
                            exState));
                }

                //Draw the leaves
                for (Pair<BlockPos, BlockState> leafLoc : destructionData.getAllLeavesWithPos()) {
                    BlockState leafState = leafLoc.getValue();
                    //~ if < 1.19.2 'ModelData.EMPTY' -> 'EmptyModelData.INSTANCE'
                    List<BakedQuad> bakedQuads = QuadManipulator.getQuads(dispatcher.getBlockModel(leafState), leafState, new Vec3(leafLoc.getKey().getX(), leafLoc.getKey().getY(), leafLoc.getKey().getZ()), entity.level.getRandom(), ModelData.EMPTY);

                    treeQuads.addAll(toTreeQuadData(bakedQuads, species.leafColorMultiplier(entity.level,
                            cutPos.offset(leafLoc.getKey())), leafState));
                }
            }
        }

        return treeQuads;
    }
}
