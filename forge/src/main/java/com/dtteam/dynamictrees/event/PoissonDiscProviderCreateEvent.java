package com.dtteam.dynamictrees.event;

import com.dtteam.dynamictrees.api.worldgen.PoissonDiscProvider;
import net.minecraft.world.level.LevelAccessor;

//? if >= 1.19.2 {
/*import net.minecraftforge.event.level.LevelEvent;
*///? } else {
import net.minecraftforge.event.world.WorldEvent;
//? }

//~ if < 1.19.2 'LevelEvent' -> 'WorldEvent'
public class PoissonDiscProviderCreateEvent extends WorldEvent {

    private PoissonDiscProvider poissonDiscProvider;

    public PoissonDiscProviderCreateEvent(LevelAccessor level, PoissonDiscProvider poissonDiscProvider) {
        super(level);
        this.poissonDiscProvider = poissonDiscProvider;
    }

    public void setPoissonDiscProvider(PoissonDiscProvider poissonDiscProvider) {
        this.poissonDiscProvider = poissonDiscProvider;
    }

    public PoissonDiscProvider getPoissonDiscProvider() {
        return poissonDiscProvider;
    }

}
