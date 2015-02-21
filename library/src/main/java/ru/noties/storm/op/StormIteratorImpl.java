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

package ru.noties.storm.op;

import android.database.Cursor;
import android.support.annotation.Nullable;

import ru.noties.storm.CursorParser;
import ru.noties.storm.StormIterator;
import ru.noties.storm.pool.ObjectPool;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 21.02.2015.
 */
class StormIteratorImpl<T> implements StormIterator<T> {

    private final Cursor cursor;
    private final CursorParser<T> cursorParser;
    private final int count;

    private ObjectPool<T> objectPool;
    private boolean isClosed;

    public StormIteratorImpl(Cursor cursor, CursorParser<T> cursorParser) {
        this.cursor = cursor;
        this.cursorParser = cursorParser;
        this.count = cursor.getCount();
    }

    public boolean moveToPosition(int position) {
        return cursor.moveToPosition(position);
    }

    @Override
    public int getCount() {
        return isClosed ? 0 : count;
    }

    @Nullable
    @Override
    public T get(int position) {
        T value = objectPool != null ? getFromPool(position) : null;
        if (value == null) {
            value = getFromCursor(position);
            if (value != null && objectPool != null) {
                objectPool.store(position, value);
            }
        }
        return value;
    }

    @Override
    public void close() {
        isClosed = true;
        cursor.close();
    }

    private T getFromPool(int position) {
        return objectPool.get(position);
    }

    private T getFromCursor(int position) {
        if (!moveToPosition(position)) {
            return null;
        }
        return cursorParser.parse(cursor);
    }

    @Override
    public void setObjectPool(ObjectPool<T> objectPool) {
        this.objectPool = objectPool;
    }

    @Nullable
    @Override
    public ObjectPool<T> getObjectPool() {
        return objectPool;
    }
}
