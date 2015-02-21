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

package ru.noties.storm.sd;

import ru.noties.storm.FieldType;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 17.02.2015.
 */
public abstract class DoubleSerializer<T> extends AbsSerializer<T> {

    @Override
    public final FieldType getSerializedFieldType() {
        return FieldType.DOUBLE;
    }

    public abstract T       deserialize (double value);
    public abstract double  serialize   (T value);
}
