package net.secretplaysmc.secrets_magic.spells.effects;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.secretplaysmc.secrets_magic.spells.effects.customObjects.ExplosiveSmallFireball;
import net.secretplaysmc.secrets_magic.spells.modifiers.SpellModifier;

import java.util.List;

public class FireballEffect implements SpellEffect{

    private float velocity = 2.0F;
    private float power = 2.0F;

    @Override
    public void apply(Level world, ServerPlayer player, ItemStack wandItem, List<SpellModifier> modifiers) {

        ExplosiveSmallFireball fireball = new ExplosiveSmallFireball(world, player, 0, 0, 0, power);
        fireball.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, velocity, 1.0F);
        fireball.setPos(player.getX(), player.getY() + player.getEyeHeight(), player.getZ());

        for (SpellModifier modifier : modifiers) {
            modifier.modify(this, world, player, wandItem);
        }

        world.addFreshEntity(fireball);
    }

    public void amplifyVelocity(float enhanceAmplification) {
        this.velocity += enhanceAmplification;
    }

    public void damageBoost(float damageBoost) {
        this.power += damageBoost;
    }
}
