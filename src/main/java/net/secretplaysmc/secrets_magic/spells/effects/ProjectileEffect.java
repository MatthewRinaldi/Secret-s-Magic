package net.secretplaysmc.secrets_magic.spells.effects;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.secretplaysmc.secrets_magic.spells.modifiers.SpellModifier;

import java.util.List;

public class ProjectileEffect implements SpellEffect {

    private Arrow arrow;
    @Override
    public void apply(Level world, ServerPlayer player, ItemStack wandItem, List<SpellModifier> modifiers) {
        arrow = new Arrow(world, player);
        arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3.0F, 1.0F);

        arrow.pickup = AbstractArrow.Pickup.DISALLOWED;

        for (SpellModifier modifier : modifiers) {
            modifier.modify(this, world, player, wandItem);
        }
        world.addFreshEntity(arrow);
    }

    public Arrow getArrow() {
        return arrow;
    }

}
