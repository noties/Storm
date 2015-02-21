/*
 * Copyright 2015 Dimitry Ivanov (mail@dimitryivanov.ru)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.noties.storm.pool;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Interface for ObjectPool which could be used directly or {@link ru.noties.storm.StormIterator#setObjectPool(ObjectPool)}
 * @see ru.noties.storm.pool.AbsObjectPool
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 21.02.2015.
 */
public interface ObjectPool<T> {

    /**
     * Retrieves item from pool. Returns null if there was no mapping
     * @param position of an item
     * @return previously cached item or null if there was no mapping
     */
    @Nullable T get(int position);

    /**
     * @return the pool size
     */
    int getPoolSize();

    /**
     * Stores <code>value</code> in cache with <code>position</code> as a key
     * @param position to be mapped with <code>value</code>
     * @param value to be stored
     */
    void store(int position, @NonNull T value);

    /**
     * Clears previous mappings.
     * May be used when data has changed or any other time
     */
    void clear();
}
