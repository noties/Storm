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

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) 01.02.15.
 */
public class FieldTypeParser {

    public FieldType parse(Class<?> clazz) {

        if (isString(clazz)) {
            return FieldType.STRING;
        }

        if (Long.TYPE.equals(clazz)) {
            return FieldType.LONG;
        }

        if (Integer.TYPE.equals(clazz)) {
            return FieldType.INT;
        }

        if (Boolean.TYPE.equals(clazz)) {
            return FieldType.BOOLEAN;
        }

        if (Float.TYPE.equals(clazz)) {
            return FieldType.FLOAT;
        }

        if (Double.TYPE.equals(clazz)) {
            return FieldType.DOUBLE;
        }

        if (Short.TYPE.equals(clazz)) {
            return FieldType.SHORT;
        }

        if (isByteArray(clazz)) {
            return FieldType.BYTE_ARRAY;
        }

        return FieldType.UNKNOWN;
    }

    public boolean isString(Class<?> type) {
        return String.class.equals(type);
    }

    public boolean isByteArray(Class<?> type) {
        return byte[].class.equals(type);
    }
}
