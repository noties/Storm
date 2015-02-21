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
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 21.02.2015.
 */
public class RecyclerObjectPool<T> extends RecyclerView.AdapterDataObserver implements ObjectPool<T> {

    private final AbsObjectPool<T> objectPool;

    public RecyclerObjectPool(T[] backingArray) {
        objectPool = new AbsObjectPool<>(backingArray);
    }

    @Override
    public void onChanged() {
        clear();
    }

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount) {
        removeItems(positionStart, itemCount);
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
        moveItems(positionStart, positionStart + itemCount, itemCount);
    }

    @Override
    public void onItemRangeRemoved(int positionStart, int itemCount) {
        removeItems(positionStart, itemCount);
    }

    @Override
    public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
        moveItems(fromPosition, toPosition, itemCount);
    }

    protected void removeItems(int position, int count) {
        final SparseIntArray positions = getPositions();
        int index;
        for (int i = position; i < position + count; i++) {
            index = positions.indexOfKey(i);
            if (index >= 0) {
                positions.removeAt(index);
            }
        }
    }

    protected void moveItems(int fromPosition, int toPosition, int count) {
        final SparseIntArray positions = getPositions();
        int index;
        int value;
        for (int i = fromPosition, delta = 0; i < fromPosition + count; i++, delta++) {
            index = positions.indexOfKey(i);
            if (index >= 0) {
                value = positions.valueAt(index);
                positions.removeAt(index);
                positions.put(toPosition + delta, value);
            }
        }
    }

    protected SparseIntArray getPositions() {
        return objectPool.getPositions();
    }

    @Nullable
    @Override
    public T get(int position) {
        return objectPool.get(position);
    }

    @Override
    public int getPoolSize() {
        return objectPool.getPoolSize();
    }

    @Override
    public void store(int position, @NonNull T value) {
        objectPool.store(position, value);
    }

    @Override
    public void clear() {
        objectPool.clear();
    }
}
