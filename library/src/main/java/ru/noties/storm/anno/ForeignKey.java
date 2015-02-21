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

package ru.noties.storm.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ru.noties.storm.ForeignKeyAction;

/**
 * @see <a href="https://www.sqlite.org/foreignkeys.html">https://www.sqlite.org/foreignkeys.html</a>
 *
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 29.01.2015.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ForeignKey {

    Class<?> parentTable();
    String parentColumnName();

    ForeignKeyAction onDelete() default ForeignKeyAction.NO_ACTION;
    ForeignKeyAction onUpdate() default ForeignKeyAction.NO_ACTION;

}
