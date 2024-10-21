package net.secretplaysmc.secrets_magic.skillTree;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.secretplaysmc.secrets_magic.network.ModNetworking;
import net.secretplaysmc.secrets_magic.network.UnlockNodePacket;

public class SkillTreeScreen extends Screen {
    public SkillTreeScreen() {
        super(Component.literal("Skill Tree"));
    }

    protected void init() {

    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGuiGraphics);
        drawSkillTree(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    public void drawSkillTree(GuiGraphics guiGraphics) {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        if (Minecraft.getInstance().player.getCapability(PlayerSkillsProvider.PLAYER_SKILLS).map(
                skills -> skills.isNodeUnlocked("starter")).orElse(false)) {
            renderUnlockedNode(guiGraphics, "Starter Node", centerX, centerY, ModSkillTree.starterNode);
        } else {
            renderLockedNode(guiGraphics, "Starter Node", centerX, centerY, ModSkillTree.starterNode);
        }

        if (Minecraft.getInstance().player.getCapability(PlayerSkillsProvider.PLAYER_SKILLS).map(
                skills -> skills.isNodeUnlocked("arrow")).orElse(false)) {
            renderUnlockedNode(guiGraphics, "Arrow Node", centerX, centerY + 50, ModSkillTree.arrowNode);
        } else {
            renderLockedNode(guiGraphics, "ArrowNode", centerX, centerY + 50, ModSkillTree.arrowNode);
        }
    }

    private void renderUnlockedNode(GuiGraphics guiGraphics, String name, int x, int y, SkillTreeNode node) {
        guiGraphics.drawString(this.font, name, x, y, 0x00FF00);
    }

    private void renderLockedNode(GuiGraphics guiGraphics, String name, int x, int y, SkillTreeNode node) {
        guiGraphics.drawString(this.font, name, x, y, 0xFF0000);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {

        if (isMouseOverNode(pMouseX, pMouseY, this.width / 2, this.height / 2)) {
            if (Minecraft.getInstance().player.getCapability(PlayerSkillsProvider.PLAYER_SKILLS).map(
                    skills -> skills.isNodeUnlocked("starter")).orElse(false)) {
                Minecraft.getInstance().player.sendSystemMessage(Component.literal("Node already unlocked!"));
            } else {
                unlockNode(ModSkillTree.starterNode);
            }
            return true;
        }

        if (isMouseOverNode(pMouseX, pMouseY, this.width / 2, this.height / 2 + 50)) {
            if (Minecraft.getInstance().player.getCapability(PlayerSkillsProvider.PLAYER_SKILLS).map(
                    skills -> skills.isNodeUnlocked("arrow")).orElse(false)) {
                Minecraft.getInstance().player.sendSystemMessage(Component.literal("Node already unlocked!"));
            } else {
                unlockNode(ModSkillTree.arrowNode);
            }
            return true;
        }

        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    public boolean isMouseOverNode(double pMouseX, double pMouseY, int nodeX, int nodeY) {
        int nodeRadius = 10;
        return pMouseX >= nodeX - nodeRadius && pMouseX <= nodeX + nodeRadius &&
                pMouseY >= nodeY - nodeRadius && pMouseY <= nodeY + nodeRadius;
    }

    private void unlockNode(SkillTreeNode node) {
        if (node.isUnlocked(Minecraft.getInstance().player)) {
            Minecraft.getInstance().player.sendSystemMessage(Component.literal("Node already unlocked!"));
        } else {
            ModNetworking.INSTANCE.sendToServer(new UnlockNodePacket(node.getName()));
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().setScreen(null);
    }
}
