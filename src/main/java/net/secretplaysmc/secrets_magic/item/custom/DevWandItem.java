package net.secretplaysmc.secrets_magic.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.secretplaysmc.secrets_magic.spells.ModSpells;
import net.secretplaysmc.secrets_magic.spells.Spell;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DevWandItem extends WandItem{
    public DevWandItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        AtomicInteger selectedSpell = new AtomicInteger(getSelectedSpell(itemStack));

        if (player.isCreative() && player.isCrouching() && !player.isUsingItem() && !world.isClientSide()) {
            List<Spell> globalSpells = new ArrayList<>(ModSpells.getAllSpells());
            int spellCount = globalSpells.size();

            selectedSpell.set((selectedSpell.get() + 1) % spellCount);
            setSelectedSpell(itemStack, selectedSpell.get());

            Spell spell = globalSpells.get(selectedSpell.get());
            player.sendSystemMessage(Component.literal("Selected Spell: " + spell.getSpellName()));

            return InteractionResultHolder.success(itemStack);
        }

        player.startUsingItem(hand);
        return InteractionResultHolder.consume(itemStack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level world, LivingEntity entity, int timeLeft) {
        if (!world.isClientSide() && entity instanceof ServerPlayer player) {
            int useDuration = this.getUseDuration(stack);
            int chargeTime = useDuration - timeLeft;

            if (chargeTime < 20) {
                return;
            }

            if (player.isCreative()) {
                int selectedSpellIndex = getSelectedSpell(stack);

                List<Spell> globalSpells = new ArrayList<>(ModSpells.getAllSpells());
                if (selectedSpellIndex < globalSpells.size()) {
                    Spell spell = globalSpells.get(selectedSpellIndex);

                    if (spell != null) {
                        spell.cast(world, player, stack);
                    } else {
                        player.sendSystemMessage(Component.literal("Spell not found!"));
                    }
                }
            }
        }
    }
}
