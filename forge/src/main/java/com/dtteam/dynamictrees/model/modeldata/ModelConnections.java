package com.dtteam.dynamictrees.model.modeldata;

import com.dtteam.dynamictrees.api.network.Connections;
import com.dtteam.dynamictrees.block.branch.BranchBlock;
import com.dtteam.dynamictrees.tree.family.Family;
import net.minecraft.core.Direction;
import net.minecraftforge.client.model.CompositeModel;
import net.minecraftforge.client.model.data.ModelDataMap;import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.Nullable;

//? if >= 1.19.2 {
/*import net.minecraftforge.client.model.data.ModelData;
*///? } else {
import net.minecraftforge.client.model.data.IModelData;
//? }

/**
 * Extension of {@link Connections} for storing and transferring model data to baked models.
 */
public class ModelConnections extends Connections {

    public static final ModelProperty<ModelConnections> CONNECTIONS_PROPERTY = new ModelProperty<>();

    private Direction ringOnly = null;
    private Family family = Family.NULL_FAMILY;

    public ModelConnections() {}

    public ModelConnections(Connections connections) {
        this.setAllRadii(connections.getAllRadii());
    }

    public ModelConnections(int[] radii) {
        super(radii);
    }

    public ModelConnections(Direction ringDir) {
        ringOnly = ringDir;
    }

    public ModelConnections setAllRadii(int[] radii) {
        return (ModelConnections) super.setAllRadii(radii);
    }

    public ModelConnections setFamily(Family family) {
        this.family = family;
        return this;
    }

    public ModelConnections setFamily(@Nullable BranchBlock branch) {
        if (branch != null) {
            this.family = branch.getFamily();
        }
        return this;
    }

    public Family getFamily() {
        return family;
    }

    public Direction getRingOnly() {
        return ringOnly;
    }

    public void setForceRing(Direction ringSide) {
        ringOnly = ringSide;
    }

    //? if >= 1.19.2 {
    /*public ModelData toModelData() {
        return ModelData.builder().with(CONNECTIONS_PROPERTY, this).build();
    }

    public ModelData toModelData(ModelData baseData) {
        return baseData.derive().with(CONNECTIONS_PROPERTY, this).build();
    }
    *///? } else {
    private static class ModelDataWrapper extends ModelDataMap {
        //
        // Copyright (c) Forge Development LLC and contributors
        // SPDX-License-Identifier: LGPL-2.1-only
        //

        private final IModelData parent;

        public static IModelData wrap(IModelData parent) {
            return new ModelDataWrapper(parent);
        }

        private ModelDataWrapper(IModelData parent) {
            this.parent = parent;
        }

        public boolean hasProperty(ModelProperty<?> prop) {
            return super.hasProperty(prop) || this.parent.hasProperty(prop);
        }

        @Nullable
        public <T> T getData(ModelProperty<T> prop) {
            return super.hasProperty(prop) ? super.getData(prop) : this.parent.getData(prop);
        }

        @Nullable
        public <T> T setData(ModelProperty<T> prop, T data) {
            return super.setData(prop, data);
        }
    }

    public IModelData toModelData() {
        return new ModelDataMap.Builder().withInitial(CONNECTIONS_PROPERTY, this).build();
    }

    public IModelData toModelData(IModelData baseData) {
        IModelData wrapper = ModelDataWrapper.wrap(baseData);
        wrapper.setData(CONNECTIONS_PROPERTY, this);
        return wrapper;
    }
    //? }
}