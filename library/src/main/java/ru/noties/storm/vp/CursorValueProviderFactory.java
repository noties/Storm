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

package ru.noties.storm.vp;

import android.database.Cursor;

import ru.noties.storm.Storm;
import ru.noties.storm.FieldType;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 31.01.2015.
 */
public class CursorValueProviderFactory extends AbsValueFactory<CursorValueProvider<?>> {

    public CursorValueProvider<?> getRaw(FieldType type) {

        switch (type) {

            case STRING:
                return new StringProvider();

            case SHORT:
                return new ShortProvider();

            case INT:
                return new IntProvider();

            case LONG:
                return new LongProvider();

            case BOOLEAN:
                return new BooleanProvider();

            case FLOAT:
                return new FloatProvider();

            case DOUBLE:
                return new DoubleProvider();

            case BYTE_ARRAY:
                return new ByteArrayProvider();
        }

        if (Storm.isDebug()) {
            throw new AssertionError("Unknown type of FieldType: " + type);
        }

        return null;
    }

    private static class ShortProvider implements CursorValueProvider<short[]> {

        @Override
        public short[] get(Cursor cursor, int position) {
            return new short[] { cursor.getShort(position) };
        }
    }

    private static class IntProvider implements CursorValueProvider<int[]> {

        @Override
        public int[] get(Cursor cursor, int position) {
            return new int[] { cursor.getInt(position) };
        }
    }

    private static class LongProvider implements CursorValueProvider<long[]> {

        @Override
        public long[] get(Cursor cursor, int position) {
            return new long[] { cursor.getLong(position) };
        }
    }

    private static class StringProvider implements CursorValueProvider<String> {

        @Override
        public String get(Cursor cursor, int position) {
            return cursor.getString(position);
        }
    }

    private static class BooleanProvider implements CursorValueProvider<boolean[]> {

        @Override
        public boolean[] get(Cursor cursor, int position) {
            return new boolean[] { cursor.getInt(position) == 1 };
        }
    }

    private static class FloatProvider implements CursorValueProvider<float[]> {

        @Override
        public float[] get(Cursor cursor, int position) {
            return new float[] { cursor.getFloat(position) };
        }
    }

    private static class DoubleProvider implements CursorValueProvider<double[]> {

        @Override
        public double[] get(Cursor cursor, int position) {
            return new double[] { cursor.getDouble(position) };
        }
    }

    private static class ByteArrayProvider implements CursorValueProvider<byte[]> {

        @Override
        public byte[] get(Cursor cursor, int position) {
            return cursor.getBlob(position);
        }
    }

}
