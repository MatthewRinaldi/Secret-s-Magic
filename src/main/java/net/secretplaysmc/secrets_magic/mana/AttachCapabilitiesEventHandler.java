package net.secretplaysmc.secrets_magic.mana;

import net.minecraft.client.CloudStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.secretplaysmc.secrets_magic.SecretsMagic;
import net.secretplaysmc.secrets_magic.skillTree.PlayerSkillsProvider;
import net.secretplaysmc.secrets_magic.spells.PlayerSpells;
import net.secretplaysmc.secrets_magic.spells.PlayerSpellsProvider;
import net.secretplaysmc.secrets_magic.util.worldgen.dimension.ModDimensions;

@Mod.EventBusSubscriber(modid = SecretsMagic.MOD_ID)
public class AttachCapabilitiesEventHandler {

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(new ResourceLocation(SecretsMagic.MOD_ID, "mana"), new ManaCapabilityProvider());
            event.addCapability(new ResourceLocation(SecretsMagic.MOD_ID, "spells"), new PlayerSpellsProvider());
            event.addCapability(new ResourceLocation(SecretsMagic.MOD_ID, "player_skills"), new PlayerSkillsProvider());
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            player.getCapability(ManaCapabilityProvider.MANA_CAPABILITY).ifPresent(mana -> {
                mana.syncManaWithClient(player);
            });
            player.getCapability(PlayerSpellsProvider.PLAYER_SPELLS).ifPresent(spells -> {
                spells.syncSpellsWithClient(player);
                if (!spells.knowsSpell("Fireball")) {
                    spells.learnSpell("Fireball");
                    spells.syncSpellsWithClient(player);
                }
                if (!spells.knowsSpell("Recursive Fireball")) {
                    spells.learnSpell("Recursive Fireball");
                    spells.syncSpellsWithClient(player);
                }
                if (!spells.knowsSpell("Arrow Storm")) {
                    spells.learnSpell("Arrow Storm");
                    spells.syncSpellsWithClient(player);
                }
                if (!spells.knowsSpell("Arrow Scattershot")) {
                    spells.learnSpell("Arrow Scattershot");
                    spells.syncSpellsWithClient(player);
                }
            });
            player.getCapability(PlayerSkillsProvider.PLAYER_SKILLS).ifPresent(skills -> {
                skills.syncSkillsWithClient(player);
            });
            if (player.level().dimension() == ModDimensions.SHOREDIM_LEVEL_KEY) {
                Minecraft.getInstance().options.cloudStatus().set(CloudStatus.OFF);
            }
        }
    }
    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            event.getOriginal().reviveCaps();
            event.getOriginal().getCapability(ManaCapabilityProvider.MANA_CAPABILITY).ifPresent(oldMana -> {
                event.getEntity().getCapability(ManaCapabilityProvider.MANA_CAPABILITY).ifPresent(newMana -> {
                    newMana.setMana(oldMana.getMana());
                    if (event.getEntity() instanceof ServerPlayer serverPlayer)
                        newMana.syncManaWithClient(serverPlayer);
                    else
                        System.out.println("Not a ServerPlayer, can't sync");
                });
            });
            event.getOriginal().getCapability(PlayerSpellsProvider.PLAYER_SPELLS).ifPresent(oldSpells -> {
                event.getEntity().getCapability(PlayerSpellsProvider.PLAYER_SPELLS).ifPresent(newSpells -> {
                    newSpells.getLearnedSpells().addAll(oldSpells.getLearnedSpells());
                    if (event.getEntity() instanceof ServerPlayer serverPlayer)
                        newSpells.syncSpellsWithClient(serverPlayer);
                    else
                        System.out.println("Not a ServerPlayer, can't sync");
                });
            });
            event.getOriginal().getCapability(PlayerSkillsProvider.PLAYER_SKILLS).ifPresent(oldSkills -> {
                event.getEntity().getCapability(PlayerSkillsProvider.PLAYER_SKILLS).ifPresent(newSkills -> {
                    newSkills.getUnlockedNodes().addAll(oldSkills.getUnlockedNodes());
                });
            });
            event.getOriginal().invalidateCaps();
        }
    }

}
