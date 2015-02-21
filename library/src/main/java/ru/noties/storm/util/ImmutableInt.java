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

package ru.noties.storm.util;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 29.01.2015.
 */
public class ImmutableInt {

    public static ImmutableInt newInstance(int value) {
        return new ImmutableInt(value);
    }

    public static ImmutableInt newInstance(boolean isReusable) {
        return new ImmutableInt(isReusable);
    }

    private int mValue;
    private final boolean mIsReusable;

    public ImmutableInt(int value) {
        this(false);
        this.mValue = value;
    }

    ImmutableInt(boolean isReusable) {
        this.mIsReusable = isReusable;
    }

    public int getValue() {
        return mValue;
    }

    public void setValue(int value) {
        if (!mIsReusable) {
            throw new IllegalStateException("This instance of the ImmutableInt is not marked as reusable");
        }
        this.mValue = value;
    }
}
