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
package org.spongepowered.common.data.processor.data.tileentity;

import net.minecraft.tileentity.TileEntityBrewingStand;
import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.immutable.tileentity.ImmutableBrewingStandData;
import org.spongepowered.api.data.manipulator.mutable.tileentity.BrewingStandData;
import org.spongepowered.api.data.value.BoundedValue;
import org.spongepowered.api.data.value.Value;
import org.spongepowered.api.data.value.ValueContainer;
import org.spongepowered.common.data.manipulator.mutable.tileentity.SpongeBrewingStandData;
import org.spongepowered.common.data.processor.common.AbstractTileEntitySingleDataProcessor;
import org.spongepowered.common.data.value.SpongeValueFactory;

import java.util.Optional;

public class BrewingStandDataProcessor extends
        AbstractTileEntitySingleDataProcessor<TileEntityBrewingStand, Integer, BoundedValue<Integer>, BrewingStandData, ImmutableBrewingStandData> {

    public BrewingStandDataProcessor() {
        super(TileEntityBrewingStand.class, Keys.REMAINING_BREW_TIME);
    }

    @Override
    protected boolean set(TileEntityBrewingStand entity, Integer value) {
        if (!entity.canBrew()) {
            return false;
        }

        entity.setField(0, value);
        return true;
    }

    @Override
    protected Optional<Integer> getVal(TileEntityBrewingStand entity) {
        return Optional.of(entity.canBrew() ? entity.getField(0) : 0);
    }

    @Override
    protected BoundedValue.Mutable<Integer> constructValue(Integer actualValue) {
        return SpongeValueFactory.boundedBuilder(Keys.REMAINING_BREW_TIME)
                .minimum(0)
                .maximum(Integer.MAX_VALUE)
                .defaultValue(400)
                .actualValue(actualValue)
                .build();
    }

    @Override
    protected Value.Immutable<Integer> constructImmutableValue(Integer value) {
        return constructValue(value).asImmutable();
    }

    @Override
    protected BrewingStandData createManipulator() {
        return new SpongeBrewingStandData();
    }

    @Override
    public DataTransactionResult removeFrom(ValueContainer<?> container) {
        return DataTransactionResult.failNoData(); // cannot be removed
    }

}
