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

import java.lang.reflect.Field;

import ru.noties.storm.field.IField;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 31.01.2015.
 */
public class CursorGetterFieldSetter<T> implements CursorValueProvider<T>, FieldValueSetter<T> {

    private final CursorValueProvider<T>    getter;
    private final FieldValueSetter<T>       setter;

    public CursorGetterFieldSetter(
            CursorValueProvider<T> getter,
            FieldValueSetter<T> setter
    ) {
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public T get(Cursor cursor, int position) {
        return getter.get(cursor, position);
    }

    @Override
    public void set(IField field, Object who, T value) throws IllegalAccessException {
        setter.set(field, who, value);
    }

    public void doYourThing(Cursor cursor, int position, IField field, Object who) {
        final T value = get(cursor, position);
        try {
            set(field, who, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
