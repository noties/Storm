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

import android.database.Cursor;
import android.support.annotation.Nullable;

import java.util.List;

import ru.noties.storm.Query;
import ru.noties.storm.StormIterator;
import ru.noties.storm.query.Selection;

/**
 * The main interface for select SQLite operations
 *
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 09.02.2015.
 */
public interface Select {

    /**
     * @see #rawQuery(String, String[], String, String[], String, String, String, String)
     * @see ru.noties.storm.Query
     */
    Cursor rawQuery(Query query);

    /**
     *
     * @param clazz to determine what table is queried
     * @param selection the selection for a query
     * @return {@link android.database.Cursor}
     * @see ru.noties.storm.query.Selection
     */
    Cursor rawQuery(Class<?> clazz, Selection selection);

    /**
     * @see {@link android.database.sqlite.SQLiteDatabase#rawQuery(String, String[])}
     * @param sql SQLite statement to execute
     * @param args arguments for SQLite statement
     * @return {@link android.database.Cursor}
     */
    Cursor rawQuery(String sql, String[] args);

    /**
     * @see {@link android.database.sqlite.SQLiteDatabase#query(String, String[], String, String[], String, String, String, String)}
     * @return {@link android.database.Cursor}
     */
    Cursor rawQuery(
            String tableName,
            String[] columns,
            String selection,
            String[] selectionArgs,
            String groupBy,
            String having,
            String orderBy,
            String limit
    );

    /**
     * This method is useful when query is performed with Joins or Unions (in other words
     * when combining results from different tables or with custom columns processing). In order
     * to save time and nerves (from parsing {@link android.database.Cursor} this method might be used.
     * Create an object with properly annotated fields as if it were a real db table. It should
     * not be passed to {@link ru.noties.storm.DatabaseManager} during initialization as it will try
     * to create a real table. And then pass this {@link java.lang.Class} to this method.
     * {@link ru.noties.storm.DynamicCursorParser} will parse object's fields and map
     * current cursor columns with new instance of specified object.
     * @param clazz with mapping fields for {@link android.database.Cursor} columns
     * @param sql statement to execute
     * @param args to add to statement
     * @param <T> Type of desired object
     * @return parsed object of Type <code>T</code> or <code>null</code> if cursor came empty
     */
    @Nullable
    <T> T customQuery(
            Class<T> clazz,
            String sql,
            String[] args
    );

    /**
     * @see #customQuery(Class, String, String[])
     * @return {@link java.util.List} of objects of Type <code>T</code>
     *      or <code>null</code> if cursor came empty
     */
    @Nullable
    <T> List<T> customQueryList(
            Class<T> clazz,
            String sql,
            String[] args
    );

    /**
     * @see #customQuery(Class, String, String[])
     * @return {@link ru.noties.storm.StormIterator} or <code>null</code> if cursor came empty
     */
    @Nullable
    <T> StormIterator<T> customQueryIterator(
            Class<T> clazz,
            String sql,
            String[] args
    );

    /**
     * Method will try to get a hold on a table from db
     *      and call {@link #query(Class, ru.noties.storm.Query)}
     * @see #query(Class, ru.noties.storm.Query)
     * @throws java.lang.IllegalStateException if {@link ru.noties.storm.Query}
     *      wasn't supplied with a Class or tableName(String),
     *      or if specified Class or tableName was not found in
     *      an underlying {@link ru.noties.storm.DatabaseManager}
     */
    @Nullable <T> T query(Query query);

    /**
     * Method will try to get a hold on a table from db
     *      and call {@link #queryList(Class, ru.noties.storm.Query)})}
     * @see #queryList(Class, ru.noties.storm.Query)
     * @throws java.lang.IllegalStateException if {@link ru.noties.storm.Query}
     *      wasn't supplied with a Class or a tableName(String),
     *      or if desired table was not found in
     *      an underlying {@link ru.noties.storm.DatabaseManager}
     */
    @Nullable <T> List<T> queryList(Query query);

    /**
     * Method will try to get a hold on a table from db
     *      and call {@link #queryIterator(Class, ru.noties.storm.Query)})}
     * @see #queryIterator(Class, ru.noties.storm.Query)
     * @throws java.lang.IllegalStateException if {@link ru.noties.storm.Query}
     *      wasn't supplied with a Class or a tableName(String),
     *      or if desired table was not found in
     *      an underlying {@link ru.noties.storm.DatabaseManager}
     */
    @Nullable <T> StormIterator<T> queryIterator(Query query);

    /**
     * @see #query(Class, ru.noties.storm.Query)
     */
    @Nullable <T> T query(Class<T> clazz, Selection selection);

    /**
     * Method will query {@link android.database.sqlite.SQLiteDatabase} for a {@link android.database.Cursor}
     *      and parse it with a {@link ru.noties.storm.TableCursorParser}
     * @param clazz of the table
     * @param query {@link ru.noties.storm.Query} to be executed
     * @param <T> Type of desired output item
     * @return parsed item of Type T if any
     */
    @Nullable <T> T query(Class<T> clazz, Query query);

    /**
     * Method will query items from specified table and parse them
     * @param clazz of the table
     * @param query {@link ru.noties.storm.Query} to be executed
     * @param <T> Type of desired items
     * @return {@link java.util.List} of items of Type T if any
     */
    @Nullable <T> List<T> queryList(Class<T> clazz, Query query);

    /**
     * Method will query items from specified table, construct a {@link ru.noties.storm.CursorParser}
     * and pass it and a {@link android.database.Cursor} to the {@link ru.noties.storm.StormIterator}
     * @param clazz of the table
     * @param query {@link ru.noties.storm.Query} to be executed
     * @param <T> Type of desired items
     * @return {@link ru.noties.storm.StormIterator}
     */
    @Nullable <T> StormIterator<T> queryIterator(Class<T> clazz, Query query);

    /**
     * Method queried for all items in the specified table
     * @param tableClass {@link java.lang.Class} of the table
     * @param <T> Type of desired output item
     * @return {@link java.util.List} of items of Type T if any
     */
    @Nullable <T> List<T> queryAll(Class<T> tableClass);

    /**
     * Method will query all items from specified table, construct a {@link ru.noties.storm.CursorParser}
     * and pass it and a {@link android.database.Cursor} to the {@link ru.noties.storm.StormIterator}
     * @param tableClass {@link java.lang.Class} of the table
     * @param <T> Type of desired output item
     * @return {@link ru.noties.storm.StormIterator}
     */
    @Nullable <T> StormIterator<T> queryAllIterator(Class<T> tableClass);
}
