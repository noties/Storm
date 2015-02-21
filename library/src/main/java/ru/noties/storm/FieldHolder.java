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

import java.lang.reflect.Field;

import ru.noties.storm.field.IField;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) 01.02.15.
 */
public class FieldHolder {

    public static FieldHolder newInstance(IField field, String name, FieldType type, boolean isPrimaryKey) {
        return new FieldHolder(field, name, type, isPrimaryKey);
    }

    private final IField    field;
    private final String    name;
    private final FieldType type;
    private final boolean   isPrimaryKey;

    private FieldHolder(IField field, String name, FieldType type, boolean isPrimaryKey) {
        this.field  = field;
        this.name   = name;
        this.type   = type;

        this.isPrimaryKey = isPrimaryKey;
    }

    public Field getField() {
        return field.getField();
    }

    public IField getFieldDelegate() {
        return field;
    }

    public String getName() {
        return name;
    }

    public FieldType getType() {
        return type;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }
}
