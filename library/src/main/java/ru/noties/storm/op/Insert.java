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

package ru.noties.storm.op;

import java.util.List;

import ru.noties.storm.exc.StormException;

/**
 * The main interface for insert SQLite operations
 *
 * Automatically executes in transaction when dealing with {@link java.util.List} of items
 * @see ru.noties.storm.op.Transactional
 *
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 06.02.2015.
 */
public interface Insert extends Transactional {

    /**
     * @see #insert(Object, boolean)
     */
    public long insert(Object value) throws StormException;

    /**
     * @see #insert(Object, boolean, boolean)
     */
    public long insert(Object value, boolean shouldNotify) throws StormException;

    /**
     * Inserts NEW row in a db
     *
     * @param value Object to save
     * @param shouldNotify whether {@link android.content.ContentResolver} should be
     *                     notified about data change in the underlying table
     *                     defaults to TRUE
     * @param setPrimaryKey whether or not {@link ru.noties.storm.ContentValuesCreator} should
     *                      set primary key to the Object's {@link android.content.ContentValues}.
     *                      defaults to FALSE
     * @return an id of the inserted row
     * @throws StormException if there was an error during execution
     */
    public long insert(Object value, boolean shouldNotify, boolean setPrimaryKey) throws StormException;

    /**
     * @see #insert(java.util.List, boolean)
     */
    public long[] insert(List<?> values) throws StormException;

    /**
     * @see #insert(java.util.List, boolean, boolean)
     */
    public long[] insert(List<?> values, boolean shouldNotify) throws StormException;

    /**
     * Inserts NEW rows in a db in one SQLite transaction
     *
     * @param values to be inserted
     * @param shouldNotify whether {@link android.content.ContentResolver} should be
     *                     notified about data change in the underlying table
     *                     defaults to TRUE
     * @param setPrimaryKey whether or not {@link ru.noties.storm.ContentValuesCreator} should
     *                      set primary key to the Object's {@link android.content.ContentValues}.
     *                      defaults to FALSE
     * @return array of inserted ids in the same order as Objects in the List
     * @throws StormException
     */
    public long[] insert(List<?> values, boolean shouldNotify, boolean setPrimaryKey) throws StormException;

}
