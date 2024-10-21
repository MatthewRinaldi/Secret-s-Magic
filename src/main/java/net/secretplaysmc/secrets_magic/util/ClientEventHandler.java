package net.secretplaysmc.secrets_magic.util;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.secretplaysmc.secrets_magic.SecretsMagic;
import net.secretplaysmc.secrets_magic.skillTree.SkillTreeScreen;

@Mod.EventBusSubscriber(modid = SecretsMagic.MOD_ID, value = Dist.CLIENT)
public class ClientEventHandler {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getInstance().player == null) return;

        if (ModKeybindings.openSkillTreeKey.isDown()) {
            if (Minecraft.getInstance().screen == null) {
                Minecraft.getInstance().setScreen(new SkillTreeScreen());
            }
        }
    }
}
