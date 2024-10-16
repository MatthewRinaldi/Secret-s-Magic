package net.secretplaysmc.secrets_magic.spells;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Spell {
    private final String name;
    private final int manaCost;
    private final SpellType type;
    private int damage;  // For projectiles
    private double speed;  // For projectiles or other effects
    private int duration;

    public Spell(String name, int manaCost, SpellType type) {
        this.name = name;
        this.manaCost = manaCost;
        this.type = type;
    }

    public SpellType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getManaCost() {
        return manaCost;
    }

    public void cast(Level world, ServerPlayer player, ItemStack wandItem) {
        switch (type) {
            case PROJECTILE:
                shootProjectile(world, player);
                break;
            case BUFF:
                applyBuff(player);
                break;
            case AREA_EFFECT:
                break;
        }
    }

    private void shootProjectile(Level world, ServerPlayer player) {
        Arrow arrow = new Arrow(world, player);
        arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, BowItem.getPowerForTime(72000) * 3.0F, 1.0F);
        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F);
        world.addFreshEntity(arrow);
        arrow.pickup = AbstractArrow.Pickup.DISALLOWED;
    }

    private void applyBuff(ServerPlayer player) {
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 3));
        player.addEffect(new MobEffectInstance(MobEffects.SATURATION, 100, 3));
    }

    private void triggerAreaEffect(Level world, ServerPlayer player) {
        // Your AoE logic here
    }
}
