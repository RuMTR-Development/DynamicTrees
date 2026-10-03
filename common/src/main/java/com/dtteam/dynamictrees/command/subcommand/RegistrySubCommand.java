package com.dtteam.dynamictrees.command.subcommand;

import com.dtteam.dynamictrees.api.registry.Registry;
import com.dtteam.dynamictrees.api.registry.RegistryEntry;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.util.Locale;

import static com.dtteam.dynamictrees.command.CommandConstants.RAW;

//? if < 1.19.2 {
/*import net.minecraft.network.chat.TextComponent;
*///? }

/**
 * @author Harley O'Connor
 */
public final class RegistrySubCommand<V extends RegistryEntry<V>> extends SubCommand {

    public final Registry<V> registry;

    public RegistrySubCommand(Registry<V> registry) {
        this.registry = registry;
    }

    @Override
    protected String getName() {
        return this.registry.getName().toLowerCase(Locale.ENGLISH);
    }

    @Override
    protected int getPermissionLevel() {
        return 0;
    }

    @Override
    public ArgumentBuilder<CommandSourceStack, ?> registerArgument() {
        return Commands.literal("list")
                .executes(context -> executesSuccess(() -> this.listEntries(context.getSource(), false)))
                .then(booleanArgument(RAW)
                        .executes(context -> executesSuccess(() -> this.listEntries(context.getSource(), BoolArgumentType.getBool(context, RAW))))
                );
    }

    private void listEntries(final CommandSourceStack source, final boolean raw) {
        if (raw) {
            this.registry.getAll().forEach(entry -> source.sendSuccess(
                    //? if >= 1.21 {
                    () -> Component.literal(entry.getRegistryName().toString()),
                    //? } else {
                    /*//~ if < 1.19.2 'Component.literal' -> 'new TextComponent'
                    Component.literal(entry.getRegistryName().toString()),
                    *///? }

                    false));
            return;
        }

        this.registry.getAll().forEach(entry -> source.sendSuccess(
                //? if >= 1.21 {
                () -> Component.literal("- ")
                //? } else {
                /*//~ if < 1.19.2 'Component.literal' -> 'new TextComponent'
                Component.literal("- ")
                *///? }

                .append(entry.getTextComponent()).withStyle(ChatFormatting.GREEN), false)
        );
    }

}
