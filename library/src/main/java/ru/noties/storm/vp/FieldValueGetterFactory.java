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

import ru.noties.storm.Storm;
import ru.noties.storm.FieldType;
import ru.noties.storm.field.IField;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 31.01.2015.
 */
public class FieldValueGetterFactory extends AbsValueFactory<FieldValueGetter<?>> {

    public FieldValueGetter<?> getRaw(FieldType type) {

        switch (type) {

            case STRING:
                return new StringGetter();

            case BYTE_ARRAY:
                return new ByteArrayGetter();

            case SHORT:
                return new ShortGetter();

            case INT:
                return new IntGetter();

            case LONG:
                return new LongGetter();

            case FLOAT:
                return new FloatGetter();

            case DOUBLE:
                return new DoubleGetter();

            case BOOLEAN:
                return new BooleanGetter();

        }

        if (Storm.isDebug()) {
            throw new AssertionError("Unknown type of FieldType: " + type);
        }

        return null;
    }

    private static class ByteArrayGetter implements FieldValueGetter<byte[]> {

        @Override
        public byte[] get(IField field, Object who) throws IllegalAccessException {
            return field.getByteArray(who);
        }
    }

    private static class StringGetter implements FieldValueGetter<String> {

        @Override
        public String get(IField field, Object who) throws IllegalAccessException {
            return field.getString(who);
        }
    }

    private static class ShortGetter implements FieldValueGetter<short[]> {

        @Override
        public short[] get(IField field, Object who) throws IllegalAccessException {
            return new short[] { field.getShort(who) };
        }
    }

    private static class IntGetter implements FieldValueGetter<int[]> {

        @Override
        public int[] get(IField field, Object who) throws IllegalAccessException {
            return new int[] { field.getInt(who) };
        }
    }

    private static class LongGetter implements FieldValueGetter<long[]> {

        @Override
        public long[] get(IField field, Object who) throws IllegalAccessException {
            return new long[] { field.getLong(who) };
        }
    }

    private static class FloatGetter implements FieldValueGetter<float[]> {

        @Override
        public float[] get(IField field, Object who) throws IllegalAccessException {
            return new float[] { field.getFloat(who) };
        }
    }

    private static class DoubleGetter implements FieldValueGetter<double[]> {

        @Override
        public double[] get(IField field, Object who) throws IllegalAccessException {
            return new double[] { field.getDouble(who) };
        }
    }

    private static class BooleanGetter implements FieldValueGetter<boolean[]> {

        @Override
        public boolean[] get(IField field, Object who) throws IllegalAccessException {
            return new boolean[] { field.getBoolean(who) };
        }
    }
}
