//? if >= 1.21 {
package com.dtteam.dynamictrees.model;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier;
import net.minecraft.client.resources.model.BakedModel;
import org.jetbrains.annotations.Nullable;

public class DTModelLoadingPlugin implements ModelLoadingPlugin {
    @Override
    public void onInitializeModelLoader(Context pluginContext) {
        DTModelModifier.onInitializeModelLoader();
        pluginContext.modifyModelAfterBake().register(ModelModifier.WRAP_PHASE, this::modifyModelAfterBake);
    }

    private @Nullable BakedModel modifyModelAfterBake(@Nullable BakedModel bakedModel, ModelModifier.AfterBake.Context context) {
        return DTModelModifier.modifyModelAfterBake(bakedModel, context.topLevelId(), context.textureGetter());
    }
}
//? }
