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
package org.spongepowered.common.mixin.optimization.mapoptimization;

import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.common.interfaces.world.IMixinMapData;
import org.spongepowered.common.interfaces.world.IMixinWorld;
import org.spongepowered.common.mixin.core.entity.MixinEntityHanging;

@Mixin(EntityItemFrame.class)
public abstract class MixinEntityItemFrame_MapOptimization extends MixinEntityHanging {

    @Shadow public abstract ItemStack getDisplayedItem();

    @Inject(method = "setDisplayedItemWithUpdate", at = @At(value = "HEAD"))
    private void onSetItem(ItemStack stack, boolean p_174864_2_, CallbackInfo ci) {
        if (((IMixinWorld) this.world).isFake()) {
            return;
        }

        if (stack.getItem() instanceof ItemMap) {
            ((IMixinMapData) ((ItemMap) stack.getItem()).getMapData(stack, this.world)).updateItemFrameDecoration((EntityItemFrame) (Object) this);
        } else if (this.getDisplayedItem().getItem() instanceof ItemMap && stack.isEmpty()) {
            ((IMixinMapData) ((ItemMap) this.getDisplayedItem().getItem()).getMapData(stack, this.world)).removeItemFrame((EntityItemFrame) (Object) this);
        }
    }



}
