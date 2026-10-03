package com.dtteam.dynamictrees.command.subcommand;

import com.dtteam.dynamictrees.command.CommandConstants;
import com.dtteam.dynamictrees.tree.ChunkTreeHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

//? if < 1.19.2 {
/*import net.minecraft.network.chat.TranslatableComponent;
*///? }

/**
 * @author Harley O'Connor
 */
public final class ClearOrphanedCommand extends ChunkBasedCommand {

    @Override
    protected String getName() {
        return CommandConstants.CLEAR_ORPHANED;
    }

    @Override
    protected int getPermissionLevel() {
        return 0;
    }

    @Override
    protected void processChunk(CommandSourceStack source, Level level, ChunkPos chunkPos, int radius) {
        //~ if < 1.19.2 'Component.translatable' -> 'new TranslatableComponent'
        sendSuccessAndLog(source, Component.translatable("commands.dynamictrees.success.clear_orphaned",
                aqua(ChunkTreeHelper.removeOrphanedBranchNodes(level, chunkPos, radius))));
    }

}
