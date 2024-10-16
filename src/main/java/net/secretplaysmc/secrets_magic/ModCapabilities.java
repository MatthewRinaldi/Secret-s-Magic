package net.secretplaysmc.secrets_magic;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.secretplaysmc.secrets_magic.mana.PlayerMana;
import net.secretplaysmc.secrets_magic.spells.PlayerSpells;

public class ModCapabilities {
    public static Capability<PlayerMana> PLAYER_MANA = CapabilityManager.get(new CapabilityToken<PlayerMana>(){});
    public static Capability<PlayerSpells> PLAYER_SPELLS = CapabilityManager.get(new CapabilityToken<PlayerSpells>(){});
}
