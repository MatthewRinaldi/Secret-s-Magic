package net.secretplaysmc.secrets_magic.util.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.secretplaysmc.secrets_magic.SecretsMagic;
import net.secretplaysmc.secrets_magic.network.ModNetworking;
import net.secretplaysmc.secrets_magic.network.SpellCreationPacket;

import java.util.ArrayList;
import java.util.List;


public class CustomSpellScreen extends AbstractContainerScreen<CustomSpellContainer> {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(SecretsMagic.MOD_ID, "textures/gui/temp.png");

    private String selectedType = "None";
    private String selectedModifier = "None";
    private String selectedTrigger = "None";

    private boolean typeButtonClicked = false;
    private boolean modifierButtonClicked = false;
    private boolean triggerButtonClicked = false;

    private List<AbstractWidget> dynamicButtons = new ArrayList<>();

    public CustomSpellScreen(CustomSpellContainer pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();

        this.imageWidth = (int) (this.width * 0.7);
        this.imageHeight = (int) (this.height * 0.7);

        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

        int buttonX = this.leftPos + (int) (this.imageWidth * 0.7);
        int buttonWidth = (int) (this.imageWidth * 0.20);
        int saveButtonWidth = (int) (this.imageWidth * 0.30);

        this.addRenderableWidget(Button.builder(
                    Component.literal("Type"), button -> {
                        modifierButtonClicked = false;
                        triggerButtonClicked = false;
                        typeButtonClicked = true;
                    })
                .pos(buttonX, this.topPos + 40)
                .size(buttonWidth, 20)
                .build()
        );

        this.addRenderableWidget(Button.builder(
                    Component.literal("Modifier"), button -> {
                        typeButtonClicked = false;
                        triggerButtonClicked = false;
                        modifierButtonClicked = true;
                    })
                .pos(buttonX, this.topPos + 70)
                .size(buttonWidth, 20)
                .build()
        );

        this.addRenderableWidget(Button.builder(
                Component.literal("Trigger"),
                button -> {
                    typeButtonClicked = false;
                    modifierButtonClicked = false;
                    triggerButtonClicked = true;
                })
                .pos(buttonX, this.topPos + 100)
                .size(buttonWidth, 20)
                .build()
        );

        // Save Spell Button
        this.addRenderableWidget(Button.builder(
                    Component.literal("Save Spell!"), button -> {
                        saveSpell();
                    })
                .pos(this.leftPos + (this.imageWidth / 2) - (saveButtonWidth / 2), this.topPos + this.imageHeight - 30)
                .size(saveButtonWidth, 20)
                .build()
        );
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        clearDynamicButtons();

        if (modifierButtonClicked) {
            // Display Modifier options dynamically
            int optionsX = this.leftPos + (int) (this.imageWidth * 0.1);
            int optionsWidth = (int) (this.imageWidth * 0.2);

            dynamicButtons.add(
                    this.addRenderableWidget(Button.builder(Component.literal("Amplification"),
                                    button -> {
                                        selectedModifier = "Amplification";
                                    })
                            .pos(optionsX, this.topPos + 40)
                            .size(optionsWidth, 20)
                            .build()
                    )
            );

            dynamicButtons.add(
                    this.addRenderableWidget(Button.builder(Component.literal("DamageBonus"),
                            button -> {
                                selectedModifier = "DamageBonus";
                            })
                    .pos(optionsX, this.topPos + 70)
                    .size(optionsWidth, 20)
                    .build()
                )
            );

            // Duration Button
            dynamicButtons.add(
                    this.addRenderableWidget(Button.builder(Component.literal("Duration"),
                            button -> {
                                selectedModifier = "Duration";
                            })
                    .pos(optionsX, this.topPos + 100)
                    .size(optionsWidth, 20)
                    .build()
                )
            );

            dynamicButtons.add(
                    this.addRenderableWidget(Button.builder(Component.literal("AoE"),
                            button -> {
                                selectedModifier = "AoE";
                            })
                            .pos(optionsX + optionsWidth + 20, this.topPos + 40)
                            .size(optionsWidth, 20)
                            .build())
            );
        } else if (typeButtonClicked) {
            // Display Modifier options dynamically
            int optionsX = this.leftPos + (int) (this.imageWidth * 0.1);
            int optionsWidth = (int) (this.imageWidth * 0.2);

            dynamicButtons.add(
                    this.addRenderableWidget(Button.builder(Component.literal("Arrow"),
                            button -> {
                                selectedType = "Arrow";
                            })
                    .pos(optionsX, this.topPos + 40)
                    .size(optionsWidth, 20)
                    .build()
                )
            );

            dynamicButtons.add(
                    this.addRenderableWidget(Button.builder(Component.literal("Fireball"),
                            button -> {
                                selectedType = "Fireball";
                            })
                    .pos(optionsX, this.topPos + 70)
                    .size(optionsWidth, 20)
                    .build()
                )
            );

            dynamicButtons.add(
                    this.addRenderableWidget(Button.builder(Component.literal("Heal"),
                            button -> {
                                selectedType = "Heal";
                            })
                    .pos(optionsX, this.topPos + 100)
                    .size(optionsWidth, 20)
                    .build()
                )
            );
        } else if (triggerButtonClicked) {
            int optionsX = this.leftPos + (int) (this.imageWidth * 0.1);
            int optionsWidth = (int) (this.imageWidth * 0.2);

            dynamicButtons.add(
                    this.addRenderableWidget(Button.builder(Component.literal("Instant"),
                            button -> {
                                selectedTrigger = "Instant";
                            })
                    .pos(optionsX, this.topPos + 40)
                    .size(optionsWidth, 20)
                    .build()
                )
            );
        }
    }
    // Method to clear dynamic buttons
    private void clearDynamicButtons() {
        for (AbstractWidget button : dynamicButtons) {
            this.removeWidget(button);
        }
        dynamicButtons.clear();
    }


    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);
        pGuiGraphics.blit(GUI_TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        // Render the screen title and labels
        String spellDetails = selectedType + " + " + selectedModifier + " + " + selectedTrigger;
        int spellDetailsWidth = this.font.width(spellDetails);
        int spellDetailsX = (this.imageWidth / 2) - (spellDetailsWidth / 2);

        guiGraphics.drawString(this.font, spellDetails, spellDetailsX, 10, 0x00FFFF, false);
    }

    private void saveSpell() {
        // Logic to save the current spell configuration
        if (!selectedType.equals("None") && !selectedModifier.equals("None") && !selectedTrigger.equals("None")) {
            // Send the spell details to the server
            ModNetworking.INSTANCE.sendToServer(new SpellCreationPacket(selectedType, selectedModifier, selectedTrigger));
        } else {
            System.out.println("Please select all components (Type, Modifier, Trigger)!");
        }
    }
}
