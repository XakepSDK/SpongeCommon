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
package org.spongepowered.common.data.value.immutable;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.Value;
import org.spongepowered.common.data.ImmutableDataCachingUtil;
import org.spongepowered.common.data.value.AbstractValue;
import org.spongepowered.common.data.value.mutable.SpongeMutableValue;

import java.util.function.Function;

@SuppressWarnings("unchecked")
public class ImmutableSpongeValue<E> extends AbstractValue<E> implements Value.Immutable<E> {

    /**
     * Gets a cached {@link Immutable} of the default value and the actual value.
     *
     * @param key The key for the value
     * @param defaultValue The default value
     * @param actualValue The actual value
     * @param <T> The type of value
     * @return The cached immutable value
     */
    public static <T> Immutable<T> cachedOf(Key<? extends Value<T>> key, T defaultValue, T actualValue) {
        return ImmutableDataCachingUtil.getValue(ImmutableSpongeValue.class, key, defaultValue, actualValue);
    }

    public ImmutableSpongeValue(Key<? extends Value<E>> key, E defaultValue) {
        super(key, defaultValue, defaultValue);
    }

    public ImmutableSpongeValue(Key<? extends Value<E>> key, E defaultValue, E actualValue) {
        super(key, defaultValue, actualValue);
    }

    @Override
    public Immutable<E> with(E value) {
        return new ImmutableSpongeValue<>(this.getKey(), getDefault(), value);
    }

    @Override
    public Immutable<E> transform(Function<E, E> function) {
        final E value = checkNotNull(function).apply(get());
        return new ImmutableSpongeValue<>(this.getKey(), getDefault(), value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Mutable<E> asMutable() {
        return new SpongeMutableValue<>(getKey(), getDefault(), get());
    }

    @Override
    public Immutable<E> asImmutable() {
        return this;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        ImmutableSpongeValue that = (ImmutableSpongeValue) o;

        return Objects.equal(this.getKey(), that.getKey()) &&
               Objects.equal(this.getDefault(), that.getDefault()) &&
               Objects.equal(this.actualValue, that.actualValue);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getKey(), getDefault(), actualValue);
    }
}
