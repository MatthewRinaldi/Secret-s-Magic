package net.secretplaysmc.secrets_magic.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.secretplaysmc.secrets_magic.spells.PlayerSpellsProvider;

import java.util.List;
import java.util.function.Supplier;

public class SpellsSyncPacket {
    private final List<String> learnedSpells;

    // Constructor for sending from the server (encoding)
    public SpellsSyncPacket(List<String> learnedSpells) {
        this.learnedSpells = learnedSpells;
    }

    // Constructor for decoding packet (reading data)
    public SpellsSyncPacket(FriendlyByteBuf buf) {
        this.learnedSpells = buf.readList(FriendlyByteBuf::readUtf);  // Read list of strings (spells)
    }

    // Encode packet (writing data)
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeCollection(learnedSpells, FriendlyByteBuf::writeUtf);  // Write list of spells to the buffer
    }

    // Handle packet on the client side
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Ensure this is client-side
            if (ctx.get().getDirection().getReceptionSide().isClient()) {
                // Update the client's learned spells here (client-side)
                Player player = Minecraft.getInstance().player;
                if (player != null) {
                    player.getCapability(PlayerSpellsProvider.PLAYER_SPELLS).ifPresent(spells -> {
                        spells.clearSpells();  // Clear existing spells on the client side
                        spells.getLearnedSpells().addAll(this.learnedSpells);  // Add new spells from the packet
                    });
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
