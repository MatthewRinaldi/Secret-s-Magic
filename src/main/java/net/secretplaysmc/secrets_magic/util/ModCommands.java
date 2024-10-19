package net.secretplaysmc.secrets_magic.util;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.secretplaysmc.secrets_magic.ModCapabilities;
import net.secretplaysmc.secrets_magic.spells.ModSpells;
import net.secretplaysmc.secrets_magic.spells.Spell;

import java.util.concurrent.CompletableFuture;

public class ModCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("secretsmagic")
                .then(Commands.literal("spells")
                        .then(Commands.literal("remove")
                                .then(Commands.literal("player")
                                        .then(Commands.argument("spellName", StringArgumentType.string())
                                                .suggests(ModCommands::suggestPlayerSpells)
                                                .executes(context -> removeSpellFromPlayer(
                                                        context.getSource(),
                                                        StringArgumentType.getString(context, "spellName")))))
                                .then(Commands.literal("global")
                                        .requires(source -> source.hasPermission(2))
                                        .then(Commands.argument("spellName", StringArgumentType.string())
                                                .suggests(ModCommands::suggestGlobalSpells)
                                                .executes(context -> removeSpellGlobally(
                                                        context.getSource(),
                                                        StringArgumentType.getString(context, "spellName"))))))));
    }

    private static CompletableFuture<Suggestions> suggestGlobalSpells(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        for (String spell : ModSpells.getAllSpells().stream().map(Spell::getSpellName).toList()) {
            builder.suggest(spell);
        }

        return builder.buildFuture();
    }

    private static CompletableFuture<Suggestions> suggestPlayerSpells(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();

        player.getCapability(ModCapabilities.PLAYER_SPELLS).ifPresent(spells -> {
            for (String spell : spells.getLearnedSpells()) {
                builder.suggest(spell);
            }
        });

        return builder.buildFuture();
    }


    private static int removeSpellFromPlayer(CommandSourceStack source, String spellName) throws CommandSyntaxException {
        // Get the player who executed the command
        ServerPlayer player = source.getPlayerOrException();

        // Remove the spell from the player's learned spells
        player.getCapability(ModCapabilities.PLAYER_SPELLS).ifPresent(spells -> {
            if (spells.knowsSpell(spellName)) {
                spells.getLearnedSpells().remove(spellName);  // Remove the spell from the list
                spells.syncSpellsWithClient(player);  // Sync with the client
                source.sendSuccess(() -> Component.literal("Removed spell: " + spellName), true);
            } else {
                source.sendFailure(Component.literal("Spell not found: " + spellName));
            }
        });

        return Command.SINGLE_SUCCESS;
    }

    private static int removeSpellGlobally(CommandSourceStack source, String spellName) throws CommandSyntaxException {
        if (ModSpells.getSpell(spellName) != null) {
            ModSpells.removeCustomSpell(spellName);

            ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers().forEach(player -> {
                player.getCapability(ModCapabilities.PLAYER_SPELLS).ifPresent(spells -> {
                    if (spells.knowsSpell(spellName)) {
                        spells.getLearnedSpells().remove(spellName);
                        spells.syncSpellsWithClient(player);
                    }
                });
            });

            source.sendSuccess(() -> Component.literal("Removed spell globally: " + spellName), true);
        } else {
            source.sendFailure(Component.literal("Spell not found globally: " + spellName));
        }


        return Command.SINGLE_SUCCESS;
    }
}
