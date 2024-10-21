package net.secretplaysmc.secrets_magic.spells.modifiers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.secretplaysmc.secrets_magic.spells.effects.BuffEffect;
import net.secretplaysmc.secrets_magic.spells.effects.FireballEffect;
import net.secretplaysmc.secrets_magic.spells.effects.ProjectileEffect;
import net.secretplaysmc.secrets_magic.spells.effects.SpellEffect;
import net.secretplaysmc.secrets_magic.spells.effects.customObjects.ExplosiveSmallFireball;

public class AoEModifier implements SpellModifier{
    private final int radius;
    private final boolean invert;

    public AoEModifier(int radius, boolean invert) {
        this.radius = radius;
        this.invert = invert;
    }

    @Override
    public void modify(SpellEffect effect, Level world, ServerPlayer player, ItemStack wandItem) {
        if (effect instanceof ProjectileEffect projectileEffect) {
            spawnAdditionalProjectiles(projectileEffect, world, player, radius);
        } else if (effect instanceof FireballEffect fireballEffect) {

        } else if (effect instanceof BuffEffect buffEffect) {
            AreaEffectCloud cloud = new AreaEffectCloud(world, player.getX(), player.getY(), player.getZ());
            cloud.setOwner(player);
            cloud.setRadius(radius);
            cloud.setWaitTime(10);
            cloud.setDuration(buffEffect.getDuration());
            cloud.addEffect(new MobEffectInstance(buffEffect.getBuff(), buffEffect.getDuration(), buffEffect.getAmplifier()));
            world.addFreshEntity(cloud);
        }
    }

    public int sendRadius() {
        return this.radius;
    }

    private void spawnAdditionalProjectiles(ProjectileEffect effect, Level world, ServerPlayer player, int radius) {
        Arrow baseArrow = effect.getArrow();
        int arrows = (int) Math.pow(2, radius);

        Vec3 targetPoint = getLookTargetPosition(player, world);
        for (int i = 0; i < arrows; i++) {
            Arrow additionalArrow = new Arrow(world, player);
            copyArrowProperties(baseArrow, additionalArrow);
            additionalArrow.setNoGravity(true);

            float yawOffset = (world.random.nextFloat() - 0.5F) * radius * 5.0F;
            float pitchOffset = (world.random.nextFloat() - 0.5F) * radius * 2.0F;

            double offsetX = (world.random.nextDouble() - 0.5) * radius * 2;
            double offsetY = (world.random.nextDouble() - 0.5) * radius * 0.5;
            double offsetZ = (world.random.nextDouble() - 0.5) * radius * 2;

            if (invert) {
                additionalArrow.setPos(baseArrow.getX() + offsetX, baseArrow.getY() + offsetY, baseArrow.getZ() + offsetZ);
                Vec3 arrowPosition = additionalArrow.position();
                Vec3 directionToTarget = targetPoint.subtract(arrowPosition).normalize();
                additionalArrow.shoot(directionToTarget.x, directionToTarget.y, directionToTarget.z, 3.0F, 1.0F);
            } else {
                additionalArrow.setPos(baseArrow.getX(), baseArrow.getY(), baseArrow.getZ());
                additionalArrow.shootFromRotation(player, player.getXRot() + pitchOffset, player.getYRot() + yawOffset, 0.0F, 3.0F, 1.0F);
            }


            additionalArrow.pickup = AbstractArrow.Pickup.DISALLOWED;
            world.addFreshEntity(additionalArrow);
        }
    }

    private Vec3 getLookTargetPosition(ServerPlayer player, Level world) {
        double maxDistance = 100.0D;

        Vec3 startVec = player.getEyePosition(1.0F);
        Vec3 lookVec = player.getLookAngle().scale(maxDistance);
        Vec3 endVec = startVec.add(lookVec);

        EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(world, player, startVec, endVec,
                player.getBoundingBox().expandTowards(lookVec).inflate(1.0D),
                entity -> !entity.isSpectator() && entity.isPickable());

        if (entityHitResult != null && entityHitResult.getEntity() != null) {
            return entityHitResult.getEntity().position();
        }

        ClipContext context = new ClipContext(
                player.getEyePosition(1.0F),
                player.getEyePosition(1.0F).add(player.getLookAngle().scale(maxDistance)),
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.NONE,
                player
        );

        BlockHitResult blockHitResult = world.clip(context);

        if (blockHitResult.getType() != HitResult.Type.MISS) {
            return blockHitResult.getLocation();
        }

        return player.position().add(player.getLookAngle().scale(15));
    }

    private void copyArrowProperties(Arrow original, Arrow copy) {
        copy.setBaseDamage(original.getBaseDamage());
        copy.setKnockback(original.getKnockback());
        copy.setCritArrow(original.isCritArrow());
        copy.setPierceLevel(original.getPierceLevel());

        if (original.getOwner() instanceof LivingEntity player) {
            copy.setEnchantmentEffectsFromEntity(player, (float) original.getDeltaMovement().length());
        }
    }

    @Override
    public CompoundTag toNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("radius", radius);
        tag.putBoolean("invert", invert);
        tag.putString("modifierType", "aoe");
        return tag;
    }

    public static AoEModifier fromNBT(CompoundTag tag) {
        int radius = tag.getInt("radius");
        boolean invert = tag.getBoolean("invert");
        return new AoEModifier(radius, invert);
    }
}
