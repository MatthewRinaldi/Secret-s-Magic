package net.secretplaysmc.secrets_magic.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.secretplaysmc.secrets_magic.spells.CustomSpellCreation;
import net.secretplaysmc.secrets_magic.spells.ModSpells;
import net.secretplaysmc.secrets_magic.spells.PlayerSpells;
import net.secretplaysmc.secrets_magic.spells.Spell;

import java.util.function.Supplier;

public class SpellCreationPacket {
    private final String spellType;
    private final String spellModifier;
    private final String spellTrigger;

    public SpellCreationPacket(String type, String modifier, String trigger) {
        this.spellType = type;
        this.spellModifier = modifier;
        this.spellTrigger = trigger;
    }

    public SpellCreationPacket(FriendlyByteBuf buf) {
        this.spellType = buf.readUtf();
        this.spellModifier = buf.readUtf();
        this.spellTrigger = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(spellType);
        buf.writeUtf(spellModifier);
        buf.writeUtf(spellTrigger);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                Spell customSpell = CustomSpellCreation.createCustomSpell(spellType, spellModifier, spellTrigger);
                PlayerSpells.addSpellToPlayer(player, customSpell.getSpellName());
                ServerLevel serverLevel = player.serverLevel();
                ModSpells.addCustomSpell(customSpell.getSpellName(), customSpell, serverLevel);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
