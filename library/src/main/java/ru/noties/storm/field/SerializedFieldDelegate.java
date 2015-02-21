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

package ru.noties.storm.field;

import android.support.annotation.NonNull;

import java.lang.reflect.Field;

import ru.noties.storm.sd.AbsSerializer;
import ru.noties.storm.sd.BooleanSerializer;
import ru.noties.storm.sd.ByteArraySerializer;
import ru.noties.storm.sd.DoubleSerializer;
import ru.noties.storm.sd.FloatSerializer;
import ru.noties.storm.sd.IntSerializer;
import ru.noties.storm.sd.LongSerializer;
import ru.noties.storm.sd.ShortSerializer;
import ru.noties.storm.sd.StringSerializer;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 17.02.2015.
 */
public class SerializedFieldDelegate implements IField {

    private final Field field;
    private final AbsSerializer<Object> serializer;

    public SerializedFieldDelegate(Field field, @NonNull AbsSerializer<Object> serializer) {
        this.field = field;
        this.serializer = serializer;
    }

    @Override
    public void set(Object who, short value) throws IllegalAccessException, IllegalArgumentException {
        field.set(who, ((ShortSerializer<?>) serializer).deserialize(value));
    }

    @Override
    public void set(Object who, int value) throws IllegalAccessException, IllegalArgumentException {
        field.set(who, ((IntSerializer<?>) serializer).deserialize(value));
    }

    @Override
    public void set(Object who, long value) throws IllegalAccessException, IllegalArgumentException {
        field.set(who, ((LongSerializer<Object>) serializer).deserialize(value));
    }

    @Override
    public void set(Object who, float value) throws IllegalAccessException, IllegalArgumentException {
        field.set(who, ((FloatSerializer<Object>) serializer).deserialize(value));
    }

    @Override
    public void set(Object who, double value) throws IllegalAccessException, IllegalArgumentException {
        field.set(who, ((DoubleSerializer<Object>) serializer).deserialize(value));
    }

    @Override
    public void set(Object who, boolean value) throws IllegalAccessException, IllegalArgumentException {
        field.set(who, ((BooleanSerializer<Object>) serializer).deserialize(value));
    }

    @Override
    public void set(Object who, byte[] value) throws IllegalAccessException, IllegalArgumentException {
        field.set(who, ((ByteArraySerializer) serializer).deserialize(value));
    }

    @Override
    public void set(Object who, String value) throws IllegalAccessException, IllegalArgumentException {
        field.set(who, ((StringSerializer<Object>) serializer).deserialize(value));
    }

    @Override
    public short getShort(Object who) throws IllegalAccessException, IllegalArgumentException {
        return ((ShortSerializer<Object>) serializer).serialize(field.get(who));
    }

    @Override
    public int getInt(Object who) throws IllegalAccessException, IllegalArgumentException {
        return ((IntSerializer<Object>) serializer).serialize(field.get(who));
    }

    @Override
    public long getLong(Object who) throws IllegalAccessException, IllegalArgumentException {
        return ((LongSerializer<Object>) serializer).serialize(field.get(who));
    }

    @Override
    public float getFloat(Object who) throws IllegalAccessException, IllegalArgumentException {
        return ((FloatSerializer<Object>) serializer).serialize(field.get(who));
    }

    @Override
    public double getDouble(Object who) throws IllegalAccessException, IllegalArgumentException {
        return ((DoubleSerializer<Object>)serializer).serialize(field.get(who));
    }

    @Override
    public boolean getBoolean(Object who) throws IllegalAccessException, IllegalArgumentException {
        return ((BooleanSerializer<Object>) serializer).serialize(field.get(who));
    }

    @Override
    public byte[] getByteArray(Object who) throws IllegalAccessException, IllegalArgumentException {
        return ((ByteArraySerializer<Object>) serializer).serialize(field.get(who));
    }

    @Override
    public String getString(Object who) throws IllegalAccessException, IllegalArgumentException {
        return ((StringSerializer<Object>) serializer).serialize(field.get(who));
    }

    @Override
    public Field getField() {
        return field;
    }
}
