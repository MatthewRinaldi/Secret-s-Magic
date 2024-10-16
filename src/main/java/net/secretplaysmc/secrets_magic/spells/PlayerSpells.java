package net.secretplaysmc.secrets_magic.spells;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;
import net.secretplaysmc.secrets_magic.ModCapabilities;
import net.secretplaysmc.secrets_magic.network.ModNetworking;
import net.secretplaysmc.secrets_magic.network.SpellsSyncPacket;

import java.util.ArrayList;
import java.util.List;

public class PlayerSpells {
    private final List<String> learnedSpells = new ArrayList<>();

    public List<String> getLearnedSpells() {
        return learnedSpells;
    }

    public void learnSpell(String spell) {
        if (!learnedSpells.contains(spell)) {
            learnedSpells.add(spell);
        }
    }

    public boolean knowsSpell(String spell) {
        return learnedSpells.contains(spell);
    }

    public void syncSpellsWithClient(ServerPlayer player) {
        ModNetworking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new SpellsSyncPacket(this.learnedSpells));
    }

    public static void addSpellToPlayer(ServerPlayer player, String spell) {
        player.getCapability(ModCapabilities.PLAYER_SPELLS).ifPresent(spells -> {
            if (!spells.knowsSpell(spell)) {
                spells.learnSpell(spell);  // Add the new spell
                spells.syncSpellsWithClient(player);  // Sync with client
            }
        });
    }

    public void clearSpells() {
        learnedSpells.clear();  // Clear the list (for respawn scenarios)
    }
}
