package net.secretplaysmc.secrets_magic.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import net.secretplaysmc.secrets_magic.mana.ManaCapabilityProvider;

import java.util.function.Supplier;

public class ManaSyncPacket {
    private final int mana;

    public ManaSyncPacket(int mana) {
        this.mana = mana;
    }

    // Decode packet (reading data)
    public ManaSyncPacket(FriendlyByteBuf buf) {
        this.mana = buf.readInt();
    }

    // Encode packet (writing data)
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(mana);
    }

    // Handle packet on the client side
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Make sure this is client-side!
            if (ctx.get().getDirection().getReceptionSide().isClient()) {
                // Update the client's player mana here (client-side)
                Player player = Minecraft.getInstance().player;
                if (player != null) {
                    player.getCapability(ManaCapabilityProvider.MANA_CAPABILITY).ifPresent(mana -> {
                        mana.setMana(this.mana);  // Update client-side mana
                    });
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
