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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import ru.noties.storm.util.MapUtils;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 25.01.2015.
 */
public class ReflectionInstanceCreator<T> implements InstanceCreator<T> {

    private final Map<Class<?>, Constructor<?>> mCache;

    ReflectionInstanceCreator() {
        this.mCache = MapUtils.create();
    }

    @Override
    public T create(Class<T> clazz) {

        final Constructor<T> constructor = getConstructor(clazz);

        try {
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        throw new IllegalStateException("Cannot create instance for a class: " + clazz);
    }

    private Constructor<T> getConstructor(Class<T> clazz) {

        final Constructor<?> constructor = mCache.get(clazz);

        if (constructor != null) {
            //noinspection unchecked
            return (Constructor<T>) constructor;
        }

        Constructor<T> newConstructor;

        try {
            newConstructor = clazz.getConstructor();
            newConstructor.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            newConstructor = null;
        }

        if (newConstructor == null) {
            throw new IllegalArgumentException("No empty constructor for a class: " + clazz);
        }

        mCache.put(clazz, newConstructor);

        return newConstructor;
    }
}
