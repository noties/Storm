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

import ru.noties.storm.FieldType;
import ru.noties.storm.Storm;
import ru.noties.storm.field.IField;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 19.02.2015.
 */
public class SelectionFieldValueGetter {

    private SelectionFieldValueGetter() {}

    public static String get(FieldType type, IField field, Object who) throws IllegalAccessException {

        final FieldValueGetter<?> getter = Storm.getFieldValueGetterFactory().get(type);
        final Object what = getter.get(field, who);

        switch (type) {

            case LONG:
                return String.valueOf(((long[]) what)[0]);

            case STRING:
                return (String) what;

            case INT:
                return String.valueOf(((int[]) what)[0]);


            // it's very strange, but be it

            case SHORT:
                return String.valueOf(((short[]) what)[0]);

            case BOOLEAN:
                return String.valueOf(((boolean[]) what)[0] ? 1 : 0);

            case FLOAT:
                return String.valueOf(((float[]) what)[0]);

            case DOUBLE:
                return String.valueOf(((double[]) what)[0]);

//            case BYTE_ARRAY:
//                return String.valueOf(what);

        }

        return null;
    }
}
