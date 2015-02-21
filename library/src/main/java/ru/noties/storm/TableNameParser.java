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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.lang.annotation.Annotation;

import ru.noties.storm.anno.Table;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) 01.02.15.
 */
public class TableNameParser {

    public static @Nullable String parse(Class<?> clazz) {
        return new TableNameParser().getTableName(clazz);
    }

    private static final Class<Table> TABLE_CLASS = Table.class;

    public @NonNull String getTableName(Class<?> clazz) {

        Annotation annotation = clazz.getAnnotation(TABLE_CLASS);

        if (annotation == null) {
            throw new IllegalArgumentException("Class should annotated with @Table annotation");
        }

        Table table = (Table) annotation;

        final String tableName = table.value();

        if (TextUtils.isEmpty(tableName)) {
            throw new IllegalArgumentException("Table name could not be empty string, Class: " + clazz);
        }

        return tableName;
    }
}
