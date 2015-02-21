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

package ru.noties.storm;

import android.content.ContentValues;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Map;

import ru.noties.storm.util.MapUtils;
import ru.noties.storm.vp.ContentValuesSetter;
import ru.noties.storm.vp.ContentValuesSetterFactory;
import ru.noties.storm.vp.FieldGetterContentValuesSetter;
import ru.noties.storm.vp.FieldValueGetter;
import ru.noties.storm.vp.FieldValueGetterFactory;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 31.01.2015.
 */
public class ContentValuesCreator {

    private final Map<FieldType, FieldGetterContentValuesSetter<?>> mMap;

    public ContentValuesCreator() {
        mMap = MapUtils.create();
    }

    public ContentValues toContentValues(
            CachedTable table,
            @NonNull Object who,
            boolean setPrimaryKey,
            @Nullable NameProvider provider
    ) {

        final ContentValues cv = new ContentValues();

        FieldGetterContentValuesSetter<?> getterAndSetter;
        String name;

        for (FieldHolder holder: table.getFields()) {

            if (!setPrimaryKey && holder.isPrimaryKey()) {
                continue;
            }

            getterAndSetter = get(holder.getType());
            name = provider == null ? holder.getName() : provider.provide(holder.getName());
            getterAndSetter.doYaThing(cv, name, holder.getFieldDelegate(), who);
        }

        return cv;
    }

    private FieldGetterContentValuesSetter<?> get(FieldType type) {
        final FieldGetterContentValuesSetter<?> value = mMap.get(type);
        if (value != null) {
            return value;
        }

        final FieldValueGetter<?> getter    = Storm.getFieldValueGetterFactory().get(type);
        final ContentValuesSetter<?> setter = Storm.getContentValuesProviderFactory().get(type);

        //noinspection unchecked
        final FieldGetterContentValuesSetter newValue = new FieldGetterContentValuesSetter(
                getter,
                setter
        );

        mMap.put(type, newValue);

        return newValue;
    }
}
