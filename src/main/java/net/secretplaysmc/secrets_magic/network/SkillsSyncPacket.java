package net.secretplaysmc.secrets_magic.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.secretplaysmc.secrets_magic.skillTree.PlayerSkillsProvider;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class SkillsSyncPacket {
    private final Set<String> unlockedNodes;

    // Constructor for sending from the server (encoding)
    public SkillsSyncPacket(Set<String> unlockedNodes) {
        this.unlockedNodes = unlockedNodes;
    }

    // Constructor for decoding packet (reading data)
    public SkillsSyncPacket(FriendlyByteBuf buf) {
        this.unlockedNodes = buf.readCollection(LinkedHashSet::new, FriendlyByteBuf::readUtf);  // Read list of strings
    }

    // Encode packet (writing data)
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeCollection(unlockedNodes, FriendlyByteBuf::writeUtf);  // Write list of nodes
    }

    // Handle packet on the client side
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                player.getCapability(PlayerSkillsProvider.PLAYER_SKILLS).ifPresent(skills -> {
                    skills.getUnlockedNodes().clear();
                    skills.getUnlockedNodes().addAll(this.unlockedNodes);  // Sync with the client
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
