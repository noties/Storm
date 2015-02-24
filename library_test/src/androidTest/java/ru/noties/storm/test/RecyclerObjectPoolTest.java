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

package ru.noties.storm.test;

import android.test.AndroidTestCase;

import ru.noties.storm.pool.RecyclerObjectPool;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 24.02.2015.
 */
public class RecyclerObjectPoolTest extends AndroidTestCase {

    private static final int POOL_SIZE = 50;
    private static final int PREPOPULATE = 10;

    public void testPoolSize() {
        assertEquals(POOL_SIZE, getTestRecyclerPool().getPoolSize());
    }

    public void testGeneral() {
        final TestRecyclerPool<String> pool = getPrepopulatedPool();
        assertEquals("0", pool.get(0));
        assertNull(pool.get(11));
    }

    public void testInsert() {
        final TestRecyclerPool<String> pool = getPrepopulatedPool();
        pool.onItemRangeInserted(5, 5);
        assertEquals("9", pool.get(14));
        assertNull(pool.get(5));
    }

    private TestRecyclerPool<String> getPrepopulatedPool() {
        final TestRecyclerPool<String> pool = getTestRecyclerPool();
        for (int i = 0; i < PREPOPULATE; i++) {
            pool.store(i, String.valueOf(i));
        }
        return pool;
    }

    private TestRecyclerPool<String> getTestRecyclerPool() {
        return new TestRecyclerPool<>(new String[POOL_SIZE]);
    }

    private static class TestRecyclerPool<T> extends RecyclerObjectPool<T> {

        public TestRecyclerPool(T[] backingArray) {
            super(backingArray);
        }
    }
}
