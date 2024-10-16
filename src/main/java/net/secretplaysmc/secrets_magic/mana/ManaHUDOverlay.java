package net.secretplaysmc.secrets_magic.mana;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.secretplaysmc.secrets_magic.SecretsMagic;
import net.secretplaysmc.secrets_magic.item.custom.WandItem;

public class ManaHUDOverlay {
    @SubscribeEvent
    public static void onRenderGuiOverlay(RenderGuiOverlayEvent.Pre event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null && minecraft.level != null) {
            if (minecraft.player.getMainHandItem().getItem() instanceof WandItem
                    || minecraft.player.getOffhandItem().getItem() instanceof WandItem) {
                minecraft.player.getCapability(ManaCapabilityProvider.MANA_CAPABILITY).ifPresent(mana -> {
                    int screenWidth = minecraft.getWindow().getGuiScaledWidth();
                    int screenHeight = minecraft.getWindow().getGuiScaledHeight();

                    // Create a GuiGraphic to draw text
                    GuiGraphics guiGraphics = event.getGuiGraphics();
                    String manaDisplay = "Mana: " + mana.getMana() + "/" + mana.getMaxMana();
                    guiGraphics.drawString(minecraft.font, manaDisplay, screenWidth / 2 + 20, screenHeight - 50, 0x00FFFF, false);
                });
            }
        }
    }
}
