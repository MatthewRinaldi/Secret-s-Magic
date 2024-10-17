package net.secretplaysmc.secrets_magic.item.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.secretplaysmc.secrets_magic.ModCapabilities;
import net.secretplaysmc.secrets_magic.item.ModItems;
import net.secretplaysmc.secrets_magic.mana.ManaCapabilityProvider;
import net.secretplaysmc.secrets_magic.spells.ModSpells;
import net.secretplaysmc.secrets_magic.spells.Spell;

import java.awt.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class WandItem extends Item {
    public WandItem(Properties pProperties) {
        super(pProperties);
    }

    // Duration of "charging" the shot
    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;  // Large value to allow holding down the use button
    }

    // Animation when the wand is in use (charging bow animation)
    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    public void setSelectedSpell(ItemStack stack, int selectedSpell) {
        stack.getOrCreateTag().putInt("selectedSpell", selectedSpell);
    }

    public int getSelectedSpell(ItemStack stack) {
        return stack.getOrCreateTag().getInt("selectedSpell");
    }

    // Called when right-clicked to "start using" the wand (charge shot)
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        AtomicInteger selectedSpell = new AtomicInteger(getSelectedSpell(itemStack));

        if (player.isCrouching() && !player.isUsingItem() && !world.isClientSide()) {
            player.getCapability(ModCapabilities.PLAYER_SPELLS).ifPresent(spells -> {
                int spellCount = spells.getLearnedSpells().size();
                selectedSpell.set((selectedSpell.get() + 1) % spellCount);  // Rotate to the next spell
                setSelectedSpell(itemStack, selectedSpell.get());  // Save selected spell to the item
                player.sendSystemMessage(Component.literal("Selected Spell: " + spells.getLearnedSpells().get(selectedSpell.get())));
            });
            return InteractionResultHolder.success(itemStack);
        }

        player.startUsingItem(hand);  // Start charging
        return InteractionResultHolder.consume(itemStack);
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        if (pLivingEntity instanceof  Player player && player.level().isClientSide) {
            for (int i = 0; i < 2; i++) {
                double offsetX = (player.level().random.nextDouble() - 0.5) * 2.0;
                double offsetY = player.level().random.nextDouble() * 2.0;
                double offsetZ = (player.level().random.nextDouble() - 0.5) * 2.0;

                player.level().addParticle(ParticleTypes.ENCHANT,
                        player.getX() + offsetX, player.getY() + offsetY, player.getZ() + offsetZ,
                        0.0, 0.0, 0.0);
            }
        }
    }

    // Called when the player stops using the wand (release shot)
    @Override
    public void releaseUsing(ItemStack stack, Level world, LivingEntity entity, int timeLeft) {
        if (!world.isClientSide() && entity instanceof ServerPlayer player) {
            int useDuration = this.getUseDuration(stack);  // Maximum possible duration (e.g. 72000)
            int chargeTime = useDuration - timeLeft;  // How long the player has charged the wand

            if (chargeTime < 20) {
                return;
            }

            // access mana
            player.getCapability(ManaCapabilityProvider.MANA_CAPABILITY).ifPresent(mana -> {
                int selectedSpellIndex = getSelectedSpell(stack);

                player.getCapability(ModCapabilities.PLAYER_SPELLS).ifPresent(spells -> {
                    List<String> learnedSpells = spells.getLearnedSpells();
                    if (selectedSpellIndex < learnedSpells.size()) {
                        String spellName = learnedSpells.get(selectedSpellIndex);
                        Spell spell = ModSpells.getSpell(spellName);

                        if (spell != null) {
                            if (player.isCreative()) {
                                spell.cast(world, player, stack);
                            } else {
                                if (mana.getMana() >= spell.getManaCost()) {
                                    mana.consumeMana(spell.getManaCost());
                                    spell.cast(world, player, stack);
                                    mana.syncManaWithClient(player);
                                } else {
                                    player.sendSystemMessage(Component.literal("Not Enough Mana!"));
                                }
                            }
                        } else {
                            player.sendSystemMessage(Component.literal("Spell not found!"));
                        }
                    }
                });
            });
        }
    }

    @Override
    public boolean isValidRepairItem(ItemStack pStack, ItemStack pRepairCandidate) {
        return pRepairCandidate.is(ModItems.SAPPHIRE.get());
    }
}
