package net.secretplaysmc.secrets_magic.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.secretplaysmc.secrets_magic.SecretsMagic;

public class ModNetworking {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(SecretsMagic.MOD_ID, "network"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void registerPackets() {
        int id = 0;
        INSTANCE.registerMessage(id++, ManaSyncPacket.class, ManaSyncPacket::toBytes, ManaSyncPacket::new, ManaSyncPacket::handle);
        INSTANCE.registerMessage(id++, SpellsSyncPacket.class, SpellsSyncPacket::toBytes, SpellsSyncPacket::new, SpellsSyncPacket::handle);
        INSTANCE.registerMessage(id++, SpellCreationPacket.class, SpellCreationPacket::toBytes, SpellCreationPacket::new, SpellCreationPacket::handle);
    }
}
