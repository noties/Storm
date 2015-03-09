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

package ru.noties.performance_test;

import android.content.Context;

import java.lang.reflect.Constructor;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 09.03.2015.
 */
public class AbsTestInvoker {

    private final Constructor<?> mConstructor;

    public AbsTestInvoker(String className) throws Exception {
        this.mConstructor = getConstructor(className);
    }

    private Constructor<?> getConstructor(String name) throws ClassNotFoundException, NoSuchMethodException {
        final Class<?> clazz = Class.forName(name);
        return clazz.getConstructor(
                Context.class,
                OpType[].class,
                int.class,
                Time.class,
                boolean.class
        );
    }

    public AbsTest<?> invoke(Context context, OpType[] opTypes, int rounds, Time time, boolean isLast) throws Exception {
        return (AbsTest<?>) mConstructor.newInstance(context, opTypes, rounds, time, isLast);
    }
}
