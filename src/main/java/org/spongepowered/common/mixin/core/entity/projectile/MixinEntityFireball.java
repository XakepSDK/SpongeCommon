/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.common.mixin.core.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import org.spongepowered.api.entity.projectile.explosive.fireball.Fireball;
import org.spongepowered.api.entity.projectile.source.ProjectileSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.common.entity.projectile.ProjectileSourceSerializer;
import org.spongepowered.common.event.SpongeCommonEventFactory;
import org.spongepowered.common.mixin.core.entity.MixinEntity;

@Mixin(EntityFireball.class)
public abstract class MixinEntityFireball extends MixinEntity implements Fireball {

    @Shadow public EntityLivingBase shootingEntity;
    @Shadow protected abstract void onImpact(MovingObjectPosition movingObjectPosition);

    private ProjectileSource projectileSource = null;

    @Override
    public ProjectileSource getShooter() {
        if (this.projectileSource == null || this.projectileSource != this.shootingEntity) {
            if (this.shootingEntity != null && this.shootingEntity instanceof ProjectileSource) {
                this.projectileSource = (ProjectileSource) this.shootingEntity;
            } else {
                this.projectileSource = ProjectileSource.UNKNOWN;
            }
        }
        return this.projectileSource;
    }

    @Override
    public void setShooter(ProjectileSource shooter) {
        this.projectileSource = shooter;
        if (shooter instanceof EntityLivingBase) {
            this.shootingEntity = (EntityLivingBase) shooter;
        } else {
            this.shootingEntity = null;
        }
    }

    @Override
    public void detonate() {
        this.onImpact(new MovingObjectPosition(null));
    }

    @Override
    public void readFromNbt(NBTTagCompound compound) {
        super.readFromNbt(compound);
        ProjectileSourceSerializer.readSourceFromNbt(compound, this);
    }

    @Override
    public void writeToNbt(NBTTagCompound compound) {
        super.writeToNbt(compound);
        ProjectileSourceSerializer.writeSourceToNbt(compound, this.projectileSource, this.shootingEntity);
    }

    @Redirect(method = "onUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/EntityFireball;onImpact(Lnet/minecraft/util/MovingObjectPosition;)V"))
    public void onProjectileImpact(EntityFireball projectile, MovingObjectPosition movingObjectPosition) {
        if (this.worldObj.isRemote || movingObjectPosition.typeOfHit == MovingObjectType.MISS) {
            this.onImpact(movingObjectPosition);
            return;
        }

        if (!SpongeCommonEventFactory.handleCollideImpactEvent(projectile, this.projectileSource, movingObjectPosition)) {
            this.onImpact(movingObjectPosition);
        }
    }
}
