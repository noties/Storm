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

import android.support.annotation.NonNull;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 25.01.2015.
 */
public class Pair<F, S> {

    public static <F, S> Pair<F, S> newInstance(F first, S second) {
        return new Pair<>(first, second);
    }

    private final F first;
    private final S second;

    public Pair(@NonNull F first, @NonNull S second) {
        this.first  = first;
        this.second = second;
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }

    public static class SimplePair<T> extends Pair<T, T> {

        public SimplePair(@NonNull T first, @NonNull T second) {
            super(first, second);
        }
    }
}
