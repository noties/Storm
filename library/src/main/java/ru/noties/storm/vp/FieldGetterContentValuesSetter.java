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

import java.lang.reflect.Field;

import ru.noties.storm.field.IField;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 31.01.2015.
 */
public class FieldGetterContentValuesSetter<T> implements FieldValueGetter<T>, ContentValuesSetter<T> {

    private final FieldValueGetter<T>       fieldValueGetter;
    private final ContentValuesSetter<T>    contentValuesSetter;

    public FieldGetterContentValuesSetter(
            FieldValueGetter<T> fieldValueGetter,
            ContentValuesSetter<T> contentValuesSetter
    ) {
        this.fieldValueGetter = fieldValueGetter;
        this.contentValuesSetter = contentValuesSetter;
    }

    @Override
    public void set(ContentValues cv, String name, T what) {
        contentValuesSetter.set(cv, name, what);
    }

    @Override
    public T get(IField field, Object who) throws IllegalAccessException {
        return fieldValueGetter.get(field, who);
    }

    public void doYaThing(ContentValues cv, String name, IField field, Object who) {
        T value;
        try {
            value = get(field, who);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            value = null;
        }

        if (value != null) {
            set(cv, name, value);
        }
    }
}
