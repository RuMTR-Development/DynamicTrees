package com.dtteam.dynamictrees.command.subcommand;

import com.dtteam.dynamictrees.command.CommandConstants;
import com.dtteam.dynamictrees.command.CommandHelper;
import com.dtteam.dynamictrees.tree.TreeHelper;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import java.util.Objects;

//? if < 1.19.2 {
/*import net.minecraft.network.chat.TranslatableComponent;
*///? }

public final class KillTreeCommand extends SubCommand {

    @Override
    protected String getName() {
        return CommandConstants.KILL_TREE;
    }

    @Override
    protected int getPermissionLevel() {
        return 2;
    }

    @Override
    public ArgumentBuilder<CommandSourceStack, ?> registerArgument() {
        return blockPosArgument().executes(context -> executesSuccess(() -> this.killTree(context.getSource(), rootPosArgument(context))));
    }

    private void killTree(final CommandSourceStack source, final BlockPos rootPos) {
        final Level level = source.getLevel();

        Objects.requireNonNull(TreeHelper.getRooty(level.getBlockState(rootPos))).destroyTree(level, rootPos);
        //~ if < 1.19.2 'Component.translatable' -> 'new TranslatableComponent'
        sendSuccessAndLog(source, Component.translatable("commands.dynamictrees.success.kill_tree",
                CommandHelper.posComponent(rootPos, ChatFormatting.AQUA)));
    }

}
