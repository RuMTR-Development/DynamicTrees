package com.dtteam.dynamictrees.command.subcommand;

import com.dtteam.dynamictrees.command.CommandConstants;
import com.dtteam.dynamictrees.command.CommandHelper;
import com.dtteam.dynamictrees.tree.TreeHelper;
import com.dtteam.dynamictrees.worldgen.JoCode;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import java.util.Optional;

//? if < 1.19.2 {
/*import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
*///? }

public final class GetTreeCommand extends SubCommand {

    @Override
    protected String getName() {
        return CommandConstants.GET_TREE;
    }

    @Override
    protected int getPermissionLevel() {
        return 0;
    }

    private static final String CODE_RAW = "code_raw";

    @Override
    public ArgumentBuilder<CommandSourceStack, ?> registerArgument() {
        return blockPosArgument().executes(context -> this.getTree(context.getSource(), blockPosArgument(context), false))
                .then(booleanArgument(CODE_RAW).executes(context -> this.getTree(context.getSource(), blockPosArgument(context),
                        booleanArgument(context, CODE_RAW))));
    }

    private int getTree(final CommandSourceStack source, final BlockPos pos, final boolean codeRaw) {
        final Level level = source.getLevel();

        return TreeHelper.getBestGuessSpecies(level, pos).ifValidElse(species -> {
                    final Optional<JoCode> joCode = TreeHelper.getJoCode(level, pos);

                    if (codeRaw) {
                        //~ if < 1.19.2 'Component.literal' -> 'new TextComponent'
                        sendSuccess(source, Component.literal(joCode.map(JoCode::toString).orElse("?")));
                    } else {
                        //~ if < 1.19.2 'Component.translatable' -> 'new TranslatableComponent'
                        sendSuccess(source, Component.translatable("commands.dynamictrees.success.get_tree",
                                species.getTextComponent(), joCode.map(JoCode::getTextComponent)
                                //~ if < 1.19.2 'Component.literal' -> 'new TextComponent'
                                .orElse(Component.literal("?"))));
                    }
                //~ if < 1.19.2 'Component.translatable' -> 'new TranslatableComponent'
                }, () -> sendFailure(source, Component.translatable("commands.dynamictrees.error.get_tree",
                        CommandHelper.posComponent(pos).copy().withStyle(style -> style.withColor(ChatFormatting.DARK_RED))))
        ) ? 1 : 0;
    }

}
