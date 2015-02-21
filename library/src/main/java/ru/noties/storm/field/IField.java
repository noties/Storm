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
public interface IField {

    void set(Object who, short  value)  throws IllegalAccessException, IllegalArgumentException;
    void set(Object who, int    value)  throws IllegalAccessException, IllegalArgumentException;
    void set(Object who, long   value)  throws IllegalAccessException, IllegalArgumentException;
    void set(Object who, float  value)  throws IllegalAccessException, IllegalArgumentException;
    void set(Object who, double value)  throws IllegalAccessException, IllegalArgumentException;
    void set(Object who, boolean value) throws IllegalAccessException, IllegalArgumentException;
    void set(Object who, byte[] value)  throws IllegalAccessException, IllegalArgumentException;
    void set(Object who, String value)  throws IllegalAccessException, IllegalArgumentException;

    short   getShort    (Object who) throws IllegalAccessException, IllegalArgumentException;
    int     getInt      (Object who) throws IllegalAccessException, IllegalArgumentException;
    long    getLong     (Object who) throws IllegalAccessException, IllegalArgumentException;
    float   getFloat    (Object who) throws IllegalAccessException, IllegalArgumentException;
    double  getDouble   (Object who) throws IllegalAccessException, IllegalArgumentException;
    boolean getBoolean  (Object who) throws IllegalAccessException, IllegalArgumentException;
    byte[]  getByteArray(Object who) throws IllegalAccessException, IllegalArgumentException;
    String  getString   (Object who) throws IllegalAccessException, IllegalArgumentException;

    Field getField();
}
