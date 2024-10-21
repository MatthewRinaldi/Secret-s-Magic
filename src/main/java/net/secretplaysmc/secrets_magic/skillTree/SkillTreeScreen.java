package net.secretplaysmc.secrets_magic.skillTree;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.secretplaysmc.secrets_magic.network.ModNetworking;
import net.secretplaysmc.secrets_magic.network.UnlockNodePacket;


public class SkillTreeScreen extends Screen {
    private int offsetX = 0;
    private int offsetY = 0;
    private boolean dragging = false;
    private int lastMouseX;
    private int lastMouseY;
    private float scale = 1.0F;

    public SkillTreeScreen() {
        super(Component.literal("Skill Tree"));
    }

    protected void init() {

    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGuiGraphics);
        drawSkillTree(pGuiGraphics, pMouseX, pMouseY);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    private SkillTreeNode getNodeUnderMouse(int pMouseX, int pMouseY) {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        for (SkillTreeNode node : ModSkillTree.NODE_MAP.values()) {
            int nodeX = centerX;
            int nodeY = centerY;

            if (isMouseOverNode(pMouseX, pMouseY, nodeX, nodeY)) {
                return node;
            }
        }

        return null;
    }

    public void drawSkillTree(GuiGraphics guiGraphics, int pMouseX, int pMouseY) {
        int centerX = this.width / 2 + offsetX;
        int centerY = this.height / 2 + offsetY;

        int color;
        int yOffset = 0;
        SkillTreeNode previousNode = null;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(scale, scale, 1.0F);

        for (SkillTreeNode node : ModSkillTree.NODE_MAP.values()) {
            boolean unlocked = Minecraft.getInstance().player.getCapability(PlayerSkillsProvider.PLAYER_SKILLS)
                    .map(playerSkills -> playerSkills.isNodeUnlocked(node.getName()))
                    .orElse(false);

            if (previousNode != null) {
                if (unlocked) {
                    color = 0xFFFFFFFF;
                } else {
                    color = 0x5A5A5A5A;
                }
                int previousYOffset = yOffset - 40;  // Adjust based on your yOffset increment
                drawSimpleLine(guiGraphics, centerX, centerY + previousYOffset,
                        centerX, centerY + yOffset, color);  // Draw line before nodes
            }
            previousNode = node;
            yOffset += 40;
        }

        yOffset = 0;

        for (SkillTreeNode node : ModSkillTree.NODE_MAP.values()) {
            boolean unlocked = Minecraft.getInstance().player.getCapability(PlayerSkillsProvider.PLAYER_SKILLS)
                    .map(playerSkills -> playerSkills.isNodeUnlocked(node.getName()))
                    .orElse(false);

            ResourceLocation nodeTexture = node.getTexture();
            ResourceLocation lockedNodeTexture = node.getTextureLocked();
            int spriteSize = node.getNodeLevel() == 1 ? 32 : 16;

            if (unlocked) {
                Minecraft.getInstance().getTextureManager().bindForSetup(nodeTexture);
                guiGraphics.blit(nodeTexture, centerX - spriteSize/2, centerY + yOffset - spriteSize/2, 0, 0,
                        spriteSize, spriteSize, spriteSize, spriteSize);
            } else {
                Minecraft.getInstance().getTextureManager().bindForSetup(lockedNodeTexture);
                guiGraphics.blit(lockedNodeTexture, centerX - spriteSize/2, centerY + yOffset - spriteSize/2, 0, 0,
                        spriteSize, spriteSize, spriteSize, spriteSize);
                if (isMouseOverNode(pMouseX, pMouseY, centerX, centerY + yOffset)) {
                    drawRequirementsTooltip(guiGraphics, node, centerX, centerY + yOffset + 10);
                }
            }
            yOffset += 40;
        }

        guiGraphics.pose().popPose();

    }

    public void drawSimpleLine(GuiGraphics guiGraphics, int startX, int startY, int endX, int endY, int color) {
        int thickness = 2;

        if (startX == endX) {
            guiGraphics.fill(startX - thickness / 2, startY, endX + thickness / 2, endY, color);
        }
    }

    public void drawRequirementsTooltip(GuiGraphics guiGraphics, SkillTreeNode node, int x, int y) {
        if (node.getRequirements().isEmpty()) {
            return;
        }

        int iconX = x;
        int iconY = y;

        for (NodeRequirement requirement : node.getRequirements()) {
            ItemStack requiredItem = requirement.getRequiredItem();
            guiGraphics.renderItem(requiredItem, iconX, iconY);
            guiGraphics.drawString(this.font, "x" + requiredItem.getCount(), iconX + 20, iconY + 4,
                    requirement.isSatisfied(Minecraft.getInstance().player) ? 0x00FF00 : 0xFFFFFF);
            iconX += 40;
        }
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        double mouseTreeX = (pMouseX - (this.width / 2 + offsetX)) / scale;
        double mouseTreeY = (pMouseY - (this.height / 2 + offsetY)) / scale;

        scale += pDelta * 0.1F;
        scale = Math.max(0.5F, Math.min(scale, 2.0F));

        offsetX = (int) (pMouseX - mouseTreeX * scale - this.width / 2);
        offsetY = (int) (pMouseY - mouseTreeY * scale - this.height / 2);
        return true;
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {

        if (isMouseOverNode(pMouseX, pMouseY, this.width / 2 + offsetX, this.height / 2 + offsetY)) {
            if (!Minecraft.getInstance().player.getCapability(PlayerSkillsProvider.PLAYER_SKILLS).map(
                    skills -> skills.isNodeUnlocked("starter")).orElse(false)) {
                unlockNode(ModSkillTree.starterNode);
            }
            return true;
        }

        if (isMouseOverNode(pMouseX, pMouseY, this.width / 2 + offsetX, this.height / 2 + 40 + offsetY)) {
            if (!Minecraft.getInstance().player.getCapability(PlayerSkillsProvider.PLAYER_SKILLS).map(
                    skills -> skills.isNodeUnlocked("arrow")).orElse(false)) {
                unlockNode(ModSkillTree.arrowNode);
            }
            return true;
        }

        if (pButton == 0) {
            this.dragging = true;
            this.lastMouseX = (int) pMouseX;
            this.lastMouseY = (int) pMouseY;
            return true;
        }

        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        if (pButton == 0) {
            this.dragging = false;
            return true;
        }

        return super.mouseReleased(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        if (this.dragging) {
            int deltaX = (int) pMouseX - this.lastMouseX;
            int deltaY = (int) pMouseY - this.lastMouseY;
            this.offsetX += deltaX;
            this.offsetY += deltaY;
            this.lastMouseX = (int) pMouseX;
            this.lastMouseY = (int) pMouseY;
        }
        return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    }

    public boolean isMouseOverNode(double pMouseX, double pMouseY, int nodeX, int nodeY) {
        int nodeRadius = 16;
        return pMouseX >= nodeX - nodeRadius && pMouseX <= nodeX + nodeRadius &&
                pMouseY >= nodeY - nodeRadius && pMouseY <= nodeY + nodeRadius;
    }

    private void unlockNode(SkillTreeNode node) {
        boolean unlocked = node.isUnlocked(Minecraft.getInstance().player);

        if (unlocked) {
            Minecraft.getInstance().player.sendSystemMessage(Component.literal("Node already unlocked!"));
        } else if (!node.canUnlock(Minecraft.getInstance().player)) {
            Minecraft.getInstance().player.sendSystemMessage(Component.literal("You cannot unlock this node!"));
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
