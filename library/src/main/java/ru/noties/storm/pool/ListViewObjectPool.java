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

import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 21.02.2015.
 */
public class ListViewObjectPool<T> extends DataSetObserver implements ObjectPool<T> {

    private final AbsObjectPool<T> objectPool;

    public ListViewObjectPool(T[] backingArray) {
        this.objectPool = new AbsObjectPool<>(backingArray);
    }

    @Override
    public void onChanged() {
        clear();
    }

    @Override
    public void onInvalidated() {
        clear();
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
