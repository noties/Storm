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

import java.lang.reflect.Field;


/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 16.02.2015.
 */
public class SimpleFieldDelegate implements IField {

    private final Field field;

    public SimpleFieldDelegate(Field field) {
        this.field = field;
    }

    @Override
    public void set(Object who, short value) throws IllegalAccessException, IllegalArgumentException {
        field.setShort(who, value);
    }

    @Override
    public void set(Object who, int value) throws IllegalAccessException, IllegalArgumentException {
        field.setInt(who, value);
    }

    @Override
    public void set(Object who, long value) throws IllegalAccessException, IllegalArgumentException {
        field.setLong(who, value);
    }

    @Override
    public void set(Object who, float value) throws IllegalAccessException, IllegalArgumentException {
        field.setFloat(who, value);
    }

    @Override
    public void set(Object who, double value) throws IllegalAccessException, IllegalArgumentException {
        field.setDouble(who, value);
    }

    @Override
    public void set(Object who, boolean value) throws IllegalAccessException, IllegalArgumentException {
        field.setBoolean(who, value);
    }

    @Override
    public void set(Object who, byte[] value) throws IllegalAccessException, IllegalArgumentException {
        field.set(who, value);
    }

    @Override
    public void set(Object who, String value) throws IllegalAccessException, IllegalArgumentException {
        field.set(who, value);
    }

    @Override
    public short getShort(Object who) throws IllegalAccessException, IllegalArgumentException {
        return field.getShort(who);
    }

    @Override
    public int getInt(Object who) throws IllegalAccessException, IllegalArgumentException {
        return field.getInt(who);
    }

    @Override
    public long getLong(Object who) throws IllegalAccessException, IllegalArgumentException {
        return field.getLong(who);
    }

    @Override
    public float getFloat(Object who) throws IllegalAccessException, IllegalArgumentException {
        return field.getFloat(who);
    }

    @Override
    public double getDouble(Object who) throws IllegalAccessException, IllegalArgumentException {
        return field.getDouble(who);
    }

    @Override
    public boolean getBoolean(Object who) throws IllegalAccessException, IllegalArgumentException {
        return field.getBoolean(who);
    }

    @Override
    public byte[] getByteArray(Object who) throws IllegalAccessException, IllegalArgumentException {
        return (byte[]) field.get(who);
    }

    @Override
    public String getString(Object who) throws IllegalAccessException, IllegalArgumentException {
        return (String) field.get(who);
    }

    @Override
    public Field getField() {
        return field;
    }
}
