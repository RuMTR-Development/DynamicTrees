package com.dtteam.dynamictrees.model.baked;

import com.dtteam.dynamictrees.model.QuadManipulator;
import com.dtteam.dynamictrees.registry.PottedSaplingBlockEntityF;
import com.dtteam.dynamictrees.tree.species.Species;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;import java.util.concurrent.ConcurrentHashMap;

//? if >= 1.19.2 {
import net.minecraft.util.RandomSource;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.model.IDynamicBakedModel;
import net.minecraftforge.client.model.data.ModelData;
//? } else {
/*import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
*///? }

public class BakedModelBlockPottedSapling implements IDynamicBakedModel {

    protected BakedModel basePotModel;
    protected Map<Species, List<BakedQuad>> cachedSaplingQuads = new ConcurrentHashMap<>();

    public BakedModelBlockPottedSapling(BakedModel basePotModel) {
        this.basePotModel = basePotModel;
    }

    @NotNull
    @Override
    public List<BakedQuad> getQuads(
            @Nullable BlockState state,
            @Nullable Direction side,

            //~ if < 1.19.2 'RandomSource' -> 'Random'
            @NotNull RandomSource rand,

            //~ if < 1.19.2 'ModelData' -> 'IModelData'
            @NotNull ModelData extraData

            //? if >= 1.19.2
            , @Nullable RenderType renderType
    ) {
        List<BakedQuad> quads = new ArrayList<>();

        //~ if < 1.19.2 '.has' -> '.hasProperty'
        if (state == null || !extraData.has(PottedSaplingBlockEntityF.SPECIES) || !extraData.has(PottedSaplingBlockEntityF.POT_MIMIC)) {
            return quads;
        }

        //~ if < 1.19.2 '.get' -> '.getData'
        final BlockState potState = extraData.get(PottedSaplingBlockEntityF.POT_MIMIC);

        if (potState == null) {
            return quads;
        }

        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
        BakedModel potModel = dispatcher.getBlockModel(potState);
        quads.addAll(potModel.getQuads(
                potState,
                side,
                rand,
                extraData

                //? if >= 1.19.2
                , renderType
        ));

        if (side == null){
            //~ if < 1.19.2 '.get' -> '.getData'
            final Species species = extraData.get(PottedSaplingBlockEntityF.SPECIES);
            if (species == null || !species.isValid() || species.getSapling().isEmpty()) {
                return quads;
            }
            final BlockState saplingState = species.getSapling().get().defaultBlockState();
            BakedModel saplingModel = dispatcher.getBlockModel(saplingState);
            quads.addAll(cachedSaplingQuads.computeIfAbsent(species, s -> QuadManipulator.getQuads(saplingModel, saplingState, new Vec3(0, 0.25, 0), new Direction[]{null}, rand, extraData)));
        }

        return quads;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return this.basePotModel.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public boolean isCustomRenderer() {
        return true;
    }

    @Override
    public boolean usesBlockLight() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return this.basePotModel.getParticleIcon();
    }

    @Override
    //~ if < 1.19.2 'ModelData' -> 'IModelData'
    public TextureAtlasSprite getParticleIcon(ModelData data) {
        return this.basePotModel.getParticleIcon(data);
    }

    @Override
    public ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }

    //? if >= 1.19.2 {
    @Override
    public ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data) {
        return ChunkRenderTypeSet.of(RenderType.cutoutMipped());
    }
    //? }
}