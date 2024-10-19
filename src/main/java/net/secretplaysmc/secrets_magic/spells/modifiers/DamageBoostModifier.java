package net.secretplaysmc.secrets_magic.spells.modifiers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.secretplaysmc.secrets_magic.spells.effects.FireballEffect;
import net.secretplaysmc.secrets_magic.spells.effects.ProjectileEffect;
import net.secretplaysmc.secrets_magic.spells.effects.SpellEffect;

public class DamageBoostModifier implements SpellModifier{
    private final double damageBoost;

    public DamageBoostModifier(double damageBoost){
        this.damageBoost = damageBoost;
    }

    @Override
    public void modify(SpellEffect effect, Level world, ServerPlayer player, ItemStack wandItem) {
        if (effect instanceof ProjectileEffect projectileEffect) {
            Arrow arrow = projectileEffect.getArrow();
            arrow.setBaseDamage(arrow.getBaseDamage() + damageBoost);
        } else if (effect instanceof FireballEffect fireballEffect) {
            fireballEffect.damageBoost((float) damageBoost);
        }
    }

    @Override
    public CompoundTag toNBT() {
        CompoundTag tag = new CompoundTag();

        tag.putString("modifierType", "damageBoostModifier");

        tag.putDouble("boost", this.damageBoost);
        return tag;
    }

    public static DamageBoostModifier fromNBT(CompoundTag tag) {

        double boost = tag.getInt("boost");

        return new DamageBoostModifier(boost);
    }
}
