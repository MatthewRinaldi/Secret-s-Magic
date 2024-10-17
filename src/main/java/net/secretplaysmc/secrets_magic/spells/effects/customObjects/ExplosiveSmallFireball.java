package net.secretplaysmc.secrets_magic.spells.effects.customObjects;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class ExplosiveSmallFireball extends SmallFireball {
    private final float power;
    public ExplosiveSmallFireball(Level pLevel, LivingEntity pShooter, double pOffsetX, double pOffsetY, double pOffsetZ, float power) {
        super(pLevel, pShooter, pOffsetX, pOffsetY, pOffsetZ);
        this.power = power;
    }

    @Override
    public void tick() {
        super.tick();


        if (!this.level().isClientSide && this.tickCount > 60) {
            this.discard();
        }
    }

    @Override
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);

        if (!this.level().isClientSide) {
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), this.power, Level.ExplosionInteraction.BLOCK);
            this.discard();
        }
    }
}
