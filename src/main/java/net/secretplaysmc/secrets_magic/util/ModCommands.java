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
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.secretplaysmc.secrets_magic.ModCapabilities;
import net.secretplaysmc.secrets_magic.skillTree.ModSkillTree;
import net.secretplaysmc.secrets_magic.skillTree.PlayerSkillsProvider;
import net.secretplaysmc.secrets_magic.skillTree.SkillTreeNode;
import net.secretplaysmc.secrets_magic.spells.ModSpells;
import net.secretplaysmc.secrets_magic.spells.PlayerSpells;
import net.secretplaysmc.secrets_magic.spells.Spell;
import net.secretplaysmc.secrets_magic.spells.effects.SpellEffect;
import net.secretplaysmc.secrets_magic.spells.effects.SpellEffectRegistry;
import net.secretplaysmc.secrets_magic.spells.modifiers.SpellModifier;
import net.secretplaysmc.secrets_magic.spells.modifiers.SpellModifierRegistry;
import net.secretplaysmc.secrets_magic.spells.triggers.SpellTrigger;
import net.secretplaysmc.secrets_magic.spells.triggers.SpellTriggerRegistry;

import java.util.ArrayList;
import java.util.List;
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
                                                        StringArgumentType.getString(context, "spellName"))))))
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.literal("create")
                                .then(Commands.literal("player")
                                        .then(Commands.argument("spellName", StringArgumentType.string())
                                                .then(Commands.argument("spellType", StringArgumentType.string())
                                                        .suggests(ModCommands::suggestSpellTypes)
                                                        .then(Commands.argument("spellModifier", StringArgumentType.string())
                                                                .suggests(ModCommands::suggestSpellModifiers)
                                                                .then(Commands.argument("spellTrigger", StringArgumentType.string())
                                                                        .suggests(ModCommands::suggestSpellTriggers)
                                                                        .executes(ctx -> {
                                                                            String spellName = StringArgumentType.getString(ctx, "spellName");
                                                                            String spellTypeName = StringArgumentType.getString(ctx, "spellType");
                                                                            String spellModifiersStr = StringArgumentType.getString(ctx, "spellModifier");
                                                                            String spellTriggerName = StringArgumentType.getString(ctx, "spellTrigger");

                                                                            CompoundTag effectTag = new CompoundTag();
                                                                            effectTag.putString("effectType", spellTypeName);
                                                                            CompoundTag triggerTag = new CompoundTag();
                                                                            triggerTag.putString("triggerType", spellTriggerName);

                                                                            String[] modifierNames = spellModifiersStr.split(" ");
                                                                            List<SpellModifier> modifiers = new ArrayList<>();
                                                                            for (String modifierName : modifierNames) {
                                                                                CompoundTag modifierTag = new CompoundTag();
                                                                                modifierTag.putString("modifierType", modifierName);
                                                                                SpellModifier spellModifier = SpellModifier.fromNBT(modifierTag);
                                                                                if (spellModifier != null) {
                                                                                    modifiers.add(spellModifier);
                                                                                }
                                                                            }

                                                                            SpellEffect spellEffect = SpellEffect.fromNBT(effectTag);
                                                                            SpellTrigger spellTrigger = SpellTrigger.fromNBT(triggerTag);

                                                                            if (spellEffect == null || modifiers.isEmpty() || spellTrigger == null) {
                                                                                ctx.getSource().sendFailure(Component.literal("Invalid spell components"));
                                                                                return 0;
                                                                            }

                                                                            Spell customSpell = new Spell(spellName, 50, 20, spellEffect, modifiers, spellTrigger);

                                                                            ServerPlayer player = ctx.getSource().getPlayerOrException();
                                                                            PlayerSpells.addSpellToPlayer(player, customSpell.getSpellName());
                                                                            ModSpells.addCustomSpell(customSpell.getSpellName(), customSpell, player.serverLevel());

                                                                            ctx.getSource().sendSuccess(() -> Component.literal("Created spell: " + spellName), true);;

                                                                            return 1;
                                                                        }))))))
                                .then(Commands.literal("global")
                                        .then(Commands.argument("spellName", StringArgumentType.string())
                                                .then(Commands.argument("spellType", StringArgumentType.string())
                                                        .suggests(ModCommands::suggestSpellTypes)
                                                        .then(Commands.argument("spellModifier", StringArgumentType.string())
                                                                .suggests(ModCommands::suggestSpellModifiers)
                                                                .then(Commands.argument("spellTrigger", StringArgumentType.string())
                                                                        .suggests(ModCommands::suggestSpellTriggers)
                                                                        .executes(ctx -> {

                                                                            String spellName = StringArgumentType.getString(ctx, "spellName");
                                                                            String spellTypeName = StringArgumentType.getString(ctx, "spellType");
                                                                            String spellModifierName = StringArgumentType.getString(ctx, "spellModifier");
                                                                            String spellTriggerName = StringArgumentType.getString(ctx, "spellTrigger");

                                                                            CompoundTag effectTag = new CompoundTag();
                                                                            effectTag.putString("effectType", spellTypeName);

                                                                            CompoundTag modifierTag = new CompoundTag();
                                                                            modifierTag.putString("modifierType", spellModifierName);

                                                                            CompoundTag triggerTag = new CompoundTag();
                                                                            triggerTag.putString("triggerType", spellTriggerName);

                                                                            SpellEffect spellEffect = SpellEffect.fromNBT(effectTag);
                                                                            SpellModifier spellModifier = SpellModifier.fromNBT(modifierTag);
                                                                            SpellTrigger spellTrigger = SpellTrigger.fromNBT(triggerTag);

                                                                            if (spellEffect == null || spellModifier == null || spellTrigger == null) {
                                                                                ctx.getSource().sendFailure(Component.literal("Invalid spell components"));
                                                                                return 0;
                                                                            }

                                                                            Spell customSpell = new Spell(spellName, 50, 20, spellEffect, List.of(spellModifier), spellTrigger);

                                                                            ServerLevel serverLevel = ctx.getSource().getLevel();
                                                                            ModSpells.addCustomSpell(customSpell.getSpellName(), customSpell, serverLevel);

                                                                            ctx.getSource().sendSuccess(() -> Component.literal("Created spell globally: " + spellName), true);;

                                                                            return 1;
                                                                        })))))))
                        .then(Commands.literal("give")
                                .requires(source -> source.hasPermission(2))
                                .then(Commands.argument("targetPlayer", EntityArgument.player())
                                        .then(Commands.argument("spellName", StringArgumentType.string())
                                                .suggests(ModCommands::suggestGlobalSpells)
                                                .executes(ctx -> {
                                                    ServerPlayer targetPlayer = EntityArgument.getPlayer(ctx, "targetPlayer");
                                                    String spellName = StringArgumentType.getString(ctx, "spellName");

                                                    // Check if the spell exists in the global registry
                                                    Spell globalSpell = ModSpells.getSpell(spellName);
                                                    if (globalSpell == null) {
                                                        ctx.getSource().sendFailure(Component.literal("Spell not found in global registry"));
                                                        return 0;
                                                    }

                                                    // Add the spell to the target player's spell list
                                                    PlayerSpells.addSpellToPlayer(targetPlayer, spellName);

                                                    // Notify the OP user and the target player
                                                    ctx.getSource().sendSuccess(() -> Component.literal("Gave spell '" + spellName + "' to " + targetPlayer.getName().getString()), true);
                                                    targetPlayer.sendSystemMessage(Component.literal("You have received the spell: " + spellName));

                                                    return 1;
                                                })))))
                .then(Commands.literal("skills")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.literal("clear")
                                .executes(ctx -> {
                                    ServerPlayer player = ctx.getSource().getPlayerOrException();
                                    player.getCapability(PlayerSkillsProvider.PLAYER_SKILLS).ifPresent(skills -> {
                                        skills.clearNodes();
                                        player.sendSystemMessage(Component.literal("Skill tree cleared!"));
                                        skills.syncSkillsWithClient(player);
                                    });
                                    return 1;
                                }))
                        .then(Commands.literal("unlock")
                                .then(Commands.argument("node", StringArgumentType.string())
                                        .suggests(ModCommands::suggestSkills)
                                        .executes(ctx -> {
                                            ServerPlayer player = ctx.getSource().getPlayerOrException();
                                            String node = StringArgumentType.getString(ctx, "node");

                                            SkillTreeNode SkillNode = ModSkillTree.getNodeByName(node);
                                            if(SkillNode == null) {
                                                player.sendSystemMessage(Component.literal(node + " not found!"));
                                                return 0;
                                            }

                                            player.getCapability(PlayerSkillsProvider.PLAYER_SKILLS).ifPresent(skills -> {
                                                skills.unlockNode(node);
                                                player.sendSystemMessage(Component.literal(node + " unlocked!"));
                                                skills.syncSkillsWithClient(player);
                                            });
                                            return 1;
                                        })))
                        .then(Commands.literal("unlockall")
                                .executes(ctx -> {
                                    ServerPlayer player = ctx.getSource().getPlayerOrException();
                                    for (String node : ModSkillTree.getAllNodes().stream().map(SkillTreeNode::getName).toList()) {
                                        player.getCapability(PlayerSkillsProvider.PLAYER_SKILLS).ifPresent(skills -> {
                                            skills.unlockNode(node);
                                            skills.syncSkillsWithClient(player);
                                        });
                                    }
                                    player.sendSystemMessage(Component.literal("Unlocked all skill tree nodes!"));
                                    return 1;
                                }))));
    }

    private static CompletableFuture<Suggestions> suggestSkills(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        for (String node : ModSkillTree.getAllNodes().stream().map(SkillTreeNode::getName).toList()) {
            builder.suggest(node);
        }

        return builder.buildFuture();
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

    public static CompletableFuture<Suggestions> suggestSpellTypes(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        SpellEffectRegistry.getAllEffectNames().forEach(builder::suggest);
        return builder.buildFuture();
    }

    public static CompletableFuture<Suggestions> suggestSpellModifiers(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        SpellModifierRegistry.getAllModifierNames().forEach(builder::suggest);
        return builder.buildFuture();
    }

    public static CompletableFuture<Suggestions> suggestSpellTriggers(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        SpellTriggerRegistry.getAllTriggerNames().forEach(builder::suggest);
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
