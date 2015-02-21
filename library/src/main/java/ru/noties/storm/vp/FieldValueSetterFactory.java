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
public class FieldValueSetterFactory extends AbsValueFactory<FieldValueSetter<?>> {

    @Override
    protected FieldValueSetter<?> getRaw(FieldType type) {

        switch (type) {

            case STRING:
                return new StringSetter();

            case BYTE_ARRAY:
                return new ByteArraySetter();

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
        }

        if (Storm.isDebug()) {
            throw new AssertionError("Unknown type of FieldType: " + type);
        }

        return null;
    }

    private static class StringSetter implements FieldValueSetter<String> {

        @Override
        public void set(IField field, Object who, String value) throws IllegalAccessException {
            field.set(who, value);
        }
    }

    private static class ByteArraySetter implements FieldValueSetter<byte[]> {

        @Override
        public void set(IField field, Object who, byte[] value) throws IllegalAccessException {
            field.set(who, value);
        }
    }

    private static class ShortSetter implements FieldValueSetter<short[]> {

        @Override
        public void set(IField field, Object who, short[] value) throws IllegalAccessException {
            field.set(who, value[0]);
        }
    }

    private static class IntSetter implements FieldValueSetter<int[]> {

        @Override
        public void set(IField field, Object who, int[] value) throws IllegalAccessException {
            field.set(who, value[0]);
        }
    }

    private static class LongSetter implements FieldValueSetter<long[]> {

        @Override
        public void set(IField field, Object who, long[] value) throws IllegalAccessException {
            field.set(who, value[0]);
        }
    }

    private static class FloatSetter implements FieldValueSetter<float[]> {

        @Override
        public void set(IField field, Object who, float[] value) throws IllegalAccessException {
            field.set(who, value[0]);
        }
    }

    private static class DoubleSetter implements FieldValueSetter<double[]> {

        @Override
        public void set(IField field, Object who, double[] value) throws IllegalAccessException {
            field.set(who, value[0]);
        }
    }

    private static class BooleanSetter implements FieldValueSetter<boolean[]> {

        @Override
        public void set(IField field, Object who, boolean[] value) throws IllegalAccessException {
            field.set(who, value[0]);
        }
    }
}
