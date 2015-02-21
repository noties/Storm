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
import ru.noties.storm.query.Selection;

/**
 * The main interface for delete SQLite operations
 *
 * Automatically executes in transaction when dealing with {@link java.util.List} of items
 * @see ru.noties.storm.op.Transactional
 *
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 06.02.2015.
 */
public interface Delete extends Transactional {

    /**
     * @see #delete(Object, boolean)
     */
    int delete(Object value) throws StormException;

    /**
     * This method will try to extract Object's primary key field, get it's value,
     * generate a {@link ru.noties.storm.query.Selection} and execute
     *
     * @param value Object to delete
     * @param shouldNotify whether {@link android.content.ContentResolver} should be
     *                     notified about data change in the underlying table
     *                     defaults to TRUE
     * @return number of rows deleted
     * @throws StormException if primary key was not found or could not be accessed
     */
    int delete(Object value, boolean shouldNotify) throws StormException;

    /**
     * @see #delete(java.util.List, boolean)
     */
    int delete(List<?> values) throws StormException;

    /**
     * This method will try to extract objects' primary keys and generate a delete statement.
     * (where _primary_key_ = ? or _primary_key_ = ?)
     * Executes in one SQLite transaction
     *
     * @param values to be deleted
     * @param shouldNotify whether {@link android.content.ContentResolver} should be
     *                     notified about data change in the underlying table
     *                     defaults to TRUE
     * @return number of rows deleted
     * @throws StormException if primary key was not found or could not be accessed
     */
    int delete(List<?> values, boolean shouldNotify) throws StormException;

    /**
     * @see #delete(Class, ru.noties.storm.query.Selection, boolean)
     */
    int delete(Class<?> clazz, Selection selection) throws StormException;

    /**
     * Deletes rows by specified {@link ru.noties.storm.query.Selection} in the specified db table
     *
     * @param clazz Table class to operate
     * @param selection {@link ru.noties.storm.query.Selection}
     * @param shouldNotify whether {@link android.content.ContentResolver} should be
     *                     notified about data change in the underlying table
     *                     defaults to TRUE
     * @return number of rows deleted
     * @throws StormException if there was an error during deleting
     */
    int delete(Class<?> clazz, Selection selection, boolean shouldNotify) throws StormException;

    /**
     * @see #deleteAll(Class, boolean)
     */
    int deleteAll(Class<?> clazz) throws StormException;

    /**
     * Deletes all rows from the specified table
     *
     * @param clazz table class to operate
     * @param shouldNotify whether {@link android.content.ContentResolver} should be
     *                     notified about data change in the underlying table
     *                     defaults to TRUE
     * @return number of rows deleted
     * @throws StormException if there was an error during execution
     */
    int deleteAll(Class<?> clazz, boolean shouldNotify) throws StormException;
}
