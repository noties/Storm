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

import android.content.ContentValues;

import ru.noties.storm.Storm;
import ru.noties.storm.FieldType;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 31.01.2015.
 */
public class ContentValuesSetterFactory extends AbsValueFactory<ContentValuesSetter<?>> {

    @Override
    protected ContentValuesSetter<?> getRaw(FieldType type) {

        switch (type) {

            case STRING:
                return new StringSetter();

            case SHORT:
                return new ShortSetter();

            case INT:
                return new IntSetter();

            case LONG:
                return new LongSetter();

            case FLOAT:
                return new FloatSetter();

            case DOUBLE:
                return new DoubleSetter();

            case BOOLEAN:
                return new BooleanSetter();

            case BYTE_ARRAY:
                return new ByteArraySetter();
        }

        if (Storm.isDebug()) {
            throw new AssertionError("Unknown type of FieldType: " + type);
        }

        return null;
    }

    private static class StringSetter implements ContentValuesSetter<String> {

        @Override
        public void set(ContentValues cv, String name, String what) {
            cv.put(name, what);
        }
    }

    private static class ShortSetter implements ContentValuesSetter<short[]> {

        @Override
        public void set(ContentValues cv, String name, short[] what) {
            cv.put(name, what[0]);
        }
    }

    private static class IntSetter implements ContentValuesSetter<int[]> {

        @Override
        public void set(ContentValues cv, String name, int[] what) {
            cv.put(name, what[0]);
        }
    }

    private static class LongSetter implements ContentValuesSetter<long[]> {

        @Override
        public void set(ContentValues cv, String name, long[] what) {
            cv.put(name, what[0]);
        }
    }

    private static class FloatSetter implements ContentValuesSetter<float[]> {

        @Override
        public void set(ContentValues cv, String name, float[] what) {
            cv.put(name, what[0]);
        }
    }

    private static class DoubleSetter implements ContentValuesSetter<double[]> {

        @Override
        public void set(ContentValues cv, String name, double[] what) {
            cv.put(name, what[0]);
        }
    }

    private static class BooleanSetter implements ContentValuesSetter<boolean[]> {

        @Override
        public void set(ContentValues cv, String name, boolean[] what) {
            cv.put(name, what[0]);
        }
    }

    private static class ByteArraySetter implements ContentValuesSetter<byte[]> {

        @Override
        public void set(ContentValues cv, String name, byte[] what) {
            cv.put(name, what);
        }
    }
}
