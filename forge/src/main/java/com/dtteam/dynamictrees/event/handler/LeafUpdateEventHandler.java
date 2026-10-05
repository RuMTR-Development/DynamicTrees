package com.dtteam.dynamictrees.event.handler;

import com.dtteam.dynamictrees.tree.TreeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

//? if >= 1.19.2 {
import net.minecraftforge.event.level.BlockEvent;
//? } else {
/*import net.minecraftforge.event.world.BlockEvent;
*///? }

//This has been put in place to counteract the effects of the FastLeafDecay mod
public class LeafUpdateEventHandler {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void UpdateNeighbour(BlockEvent.NeighborNotifyEvent event) {
        //~ if < 1.19.2 '.getLevel' -> '.getWorld'
        LevelAccessor level = event.getLevel();
        for (Direction facing : event.getNotifiedSides()) {
            BlockPos blockPos = event.getPos().relative(facing);
            if (TreeHelper.isLeaves(level.getBlockState(blockPos))) {
                event.setCanceled(true);
            }
        }
    }

}