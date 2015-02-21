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
import android.util.SparseIntArray;

/**
 * Does not intended to be used directly, although it surely could be.
 * See {@link ru.noties.storm.pool.ListViewObjectPool} and {@link ru.noties.storm.pool.RecyclerObjectPool}
 * as you will get observers for ListView adapter or RecyclerView.Adapter for free.
 *
 * Can be used directly but was intended to be used with {@link ru.noties.storm.StormIterator}
 * as a caching storage. The idea was simple - cache parsed in real-time from cursor items and store
 * them. So every call to getItemType(), getItemId(), etc would not parse an item again.
 *
 * The main logic hides behind {@link android.util.SparseIntArray}
 * which holds real position and position in backing array.
 *
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 21.02.2015.
 */
public class AbsObjectPool<T> implements ObjectPool<T> {

    private final SparseIntArray positions;
    private final T[] array;
    private final int max;

    private int currentPosition;

    /**
     * @param backingArray non null array of objects which will be used as a caching storage.
     *                     Should not be modified from anywhere else. The best practice is to call:
     *                     <code>new AbsObjectPool(new String[50])</code>. The length of the array
     *                     will be used as a maximum for this ObjectPool
     */
    public AbsObjectPool(@NonNull T[] backingArray) {
        this.array = backingArray;
        this.max   = array.length;
        this.positions = new SparseIntArray(max);
    }

    @Nullable
    @Override
    public T get(int position) {
        final int index = positions.indexOfKey(position);
        if (index >= 0) {
            return array[positions.valueAt(index)];
        }
        return null;
    }

    @Override
    public int getPoolSize() {
        return max;
    }

    @Override
    public void store(int position, @NonNull T value) {
        currentPosition = getNewArrayPosition();
        checkMappings(currentPosition);
        positions.put(position, currentPosition);
        array[currentPosition] = value;
    }

    @Override
    public void clear() {
        positions.clear();
    }

    protected int getNewArrayPosition() {
        if (++currentPosition >= max) {
            return currentPosition % max;
        }
        return currentPosition;
    }

    // if we have previously mapped some 'position' to the index in the array
    // we should clear it
    protected void checkMappings(int arrayPosition) {
        final int index = positions.indexOfValue(arrayPosition);
        if (index >= 0) {
            positions.removeAt(index);
        }
    }

    SparseIntArray getPositions() {
        return positions;
    }
}
