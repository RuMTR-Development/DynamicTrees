package com.dtteam.dynamictrees.compat.continuity;

import com.dtteam.dynamictrees.model.baked.BasicBranchBlockBakedModel;
import net.minecraft.client.resources.model.BakedModel;
import org.jetbrains.annotations.Nullable;

//? if >= 1.19.4 {
import me.pepperbell.continuity.client.model.CtmBakedModel;
//? } else {
/*import me.pepperbell.continuity.client.model.CTMBakedModel;
*///? }

public class ContinuityWrappedModelHandler extends WrappedModelHandler {

    @Override
    public @Nullable BasicBranchBlockBakedModel unwrapBranchModel(BakedModel model) {
        //~ if < 1.19.4 'CtmBakedModel' -> 'CTMBakedModel'
        if (model instanceof CtmBakedModel ctmModel){
            return super.unwrapBranchModel(ctmModel.getWrappedModel());
        }
        return super.unwrapBranchModel(model);
    }
}
