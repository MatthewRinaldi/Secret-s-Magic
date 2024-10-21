package net.secretplaysmc.secrets_magic.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.secretplaysmc.secrets_magic.skillTree.PlayerSkillsProvider;

import java.util.HashSet;
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
        int size = buf.readInt();
        this.unlockedNodes = new HashSet<>();
        for (int i = 0; i < size; i++) {
            this.unlockedNodes.add(buf.readUtf(32767));
        }
    }

    // Encode packet (writing data)
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.unlockedNodes.size());
        for (String node : unlockedNodes) {
            buf.writeUtf(node);
        }
    }

    // Handle packet on the client side
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                player.getCapability(PlayerSkillsProvider.PLAYER_SKILLS).ifPresent(skills -> {
                    skills.clearNodes();
                    this.unlockedNodes.forEach(skills::unlockNode);
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
