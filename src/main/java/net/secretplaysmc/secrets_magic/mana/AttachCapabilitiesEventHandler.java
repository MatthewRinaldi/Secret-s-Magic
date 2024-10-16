package net.secretplaysmc.secrets_magic.mana;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.secretplaysmc.secrets_magic.SecretsMagic;
import net.secretplaysmc.secrets_magic.spells.PlayerSpellsProvider;

@Mod.EventBusSubscriber(modid = SecretsMagic.MOD_ID)
public class AttachCapabilitiesEventHandler {

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            event.addCapability(new ResourceLocation(SecretsMagic.MOD_ID, "mana"), new ManaCapabilityProvider());
            event.addCapability(new ResourceLocation(SecretsMagic.MOD_ID, "spells"), new PlayerSpellsProvider());
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
                if (!spells.knowsSpell("arrow")) {
                    spells.learnSpell("arrow");
                    spells.syncSpellsWithClient(player);
                }
            });
        }
    }

    public static void onPlayerRespawn(PlayerEvent.Clone event) {
        if (event.isWasDeath() && event.getEntity() instanceof ServerPlayer player) {
            event.getOriginal().getCapability(ManaCapabilityProvider.MANA_CAPABILITY).ifPresent(oldMana -> {
                player.getCapability(ManaCapabilityProvider.MANA_CAPABILITY).ifPresent(newMana -> {
                    newMana.setMana(oldMana.getMana());
                });
            });

            event.getOriginal().getCapability(PlayerSpellsProvider.PLAYER_SPELLS).ifPresent(oldSpells -> {
                player.getCapability(PlayerSpellsProvider.PLAYER_SPELLS).ifPresent(newSpells -> {
                    newSpells.getLearnedSpells().addAll(oldSpells.getLearnedSpells());
                });
            });
        }
    }

}
