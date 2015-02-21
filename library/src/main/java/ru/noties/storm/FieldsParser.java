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
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.noties.storm.field.IField;
import ru.noties.storm.field.SerializedFieldDelegate;
import ru.noties.storm.field.SimpleFieldDelegate;
import ru.noties.storm.sd.AbsSerializer;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 25.01.2015.
 */
public class FieldsParser {

    private final ColumnNameParser columnNameParser;
    private final FieldTypeParser  fieldTypeParser;

    public FieldsParser() {
        columnNameParser = new ColumnNameParser();
        fieldTypeParser  = new FieldTypeParser();
    }

    public @Nullable List<FieldHolder> getFields(@NonNull Class<?> clazz) {

        final Field[] fields = clazz.getDeclaredFields();

        if (fields == null
                || fields.length == 0) {
            return null;
        }

        final Map<Class<?>, AbsSerializer<Object>> serializerMap = Storm.getSerializers();
        final boolean hasSerializers = serializerMap.size() > 0;

        final List<FieldHolder> list = new ArrayList<>();

        String columnName;
        FieldType fieldType;
        Class<?> fieldClass;
        AbsSerializer<Object> serializer = null;
        boolean isPrimary;

        IField delegate;

        for (Field field: fields) {

            columnName = columnNameParser.get(field);
            if (columnName == null) {
                continue;
            }

            // after we ensured that field has a @Column annotation,
            // we can set it accessible
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }

            fieldClass = field.getType();

            // if a field is primitive or String or byte[] then it cannot (or better say must not) have a serializer
            if (hasSerializers
                    && !(fieldClass.isPrimitive()
                            || fieldTypeParser.isString(fieldClass)
                            || fieldTypeParser.isByteArray(fieldClass))
            ) {
                serializer = serializerMap.get(fieldClass);
            }

            isPrimary = FieldPrimaryKey.isPrimaryKey(field);

            if (serializer == null) {

                fieldType = fieldTypeParser.parse(fieldClass);

                if (fieldType == FieldType.UNKNOWN) {
                    // log about it
                    if (Storm.isDebug()) {
                        Log.e(null, "fieldType is not a native SQLite type &" +
                                "has no registered type serializer: " + fieldClass);
                    }
                    continue;
                }

                delegate = new SimpleFieldDelegate(field);

            } else {
                fieldType = serializer.getSerializedFieldType();
                delegate = new SerializedFieldDelegate(field, serializer);
                serializer = null;
            }

            list.add(FieldHolder.newInstance(delegate, columnName, fieldType, isPrimary));
        }

        if (list.size() == 0) {
            return null;
        }

        return list;
    }
}
