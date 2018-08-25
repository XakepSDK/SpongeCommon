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
package org.spongepowered.common.mixin.core.data.types;

import net.minecraft.block.BlockLog;
import org.spongepowered.api.CatalogKey;
import org.spongepowered.api.data.type.LogAxis;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(BlockLog.EnumAxis.class)
@Implements(@Interface(iface = LogAxis.class, prefix = "log$"))
public abstract class MixinEnumLogAxis implements LogAxis {

    @Shadow public abstract String shadow$getName();
    @Nullable private CatalogKey key;

    public CatalogKey log$getKey() {
        if (this.key == null) {
            this.key = CatalogKey.resolve(shadow$getName());
        }
        return this.key;
    }

    @Intrinsic
    public String log$getName() {
        return shadow$getName();
    }

    @Override
    public LogAxis cycleNext() {
        return (LogAxis) (Object) BlockLog.EnumAxis.values()[(((BlockLog.EnumAxis) (Object) this).ordinal() + 1) % BlockLog.EnumAxis.values().length];
    }
}
