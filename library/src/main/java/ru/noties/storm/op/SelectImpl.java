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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import ru.noties.storm.CachedTable;
import ru.noties.storm.CursorParser;
import ru.noties.storm.DatabaseManager;
import ru.noties.storm.DynamicCursorParser;
import ru.noties.storm.Query;
import ru.noties.storm.StormIterator;
import ru.noties.storm.TableCursorParser;
import ru.noties.storm.query.Selection;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 09.02.2015.
 */
public class SelectImpl implements Select {

    private final DatabaseManager manager;

    public SelectImpl(DatabaseManager manager) {
        this.manager = manager;
    }

    @Override
    public Cursor rawQuery(Query query) {
        return manager.query(query);
    }

    @Override
    public Cursor rawQuery(Class<?> clazz, Selection selection) {
        return manager.query(new Query(clazz).selection(selection));
    }

    @Override
    public Cursor rawQuery(String sql, String[] args) {
        return manager.query(sql, args);
    }

    @Override
    public Cursor rawQuery(
            String tableName,
            String[] columns,
            String selection,
            String[] selectionArgs,
            String groupBy,
            String having,
            String orderBy,
            String limit
    ) {
        return manager.query(tableName, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    @Nullable
    @Override
    public <T> T customQuery(
            Class<T> clazz,
            String sql,
            String[] args
    ) {
        final Cursor cursor = rawQuery(sql, args);

        if (cursor != null) {

            if (!cursor.moveToFirst()) {
                cursor.close();
                return null;
            }

            final DynamicCursorParser<T> cursorParser = new DynamicCursorParser<>(clazz);
            final T value = cursorParser.parse(cursor);

            cursor.close();
            return value;
        }

        return null;
    }

    @Nullable
    @Override
    public <T> List<T> customQueryList(Class<T> clazz, String sql, String[] args) {

        final Cursor cursor = rawQuery(sql, args);

        if (cursor != null) {

            if (!cursor.moveToFirst()) {
                cursor.close();
                return null;
            }

            final DynamicCursorParser<T> dynamicCursorParser = new DynamicCursorParser<>(clazz);
            final List<T> list = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                list.add(dynamicCursorParser.parse(cursor));
                cursor.moveToNext();
            }

            cursor.close();
            return list;
        }
        return null;
    }

    @Nullable
    @Override
    public <T> StormIterator<T> customQueryIterator(Class<T> clazz, String sql, String[] args) {

        final Cursor cursor = rawQuery(sql, args);

        if (cursor != null) {

            if (!cursor.moveToFirst()) {
                cursor.close();
                return null;
            }

            return new StormIteratorImpl<>(cursor, new DynamicCursorParser<>(clazz));
        }
        return null;
    }

    @Override
    public <T> T query(Query query) {
        final Class<T> clazz = getQueryClazz(query);
        return query(clazz, query);
    }

    @Override
    public <T> List<T> queryList(Query query) {
        final Class<T> clazz = getQueryClazz(query);
        return queryList(clazz, query);
    }

    @Nullable
    @Override
    public <T> StormIterator<T> queryIterator(Query query) {
        final Class<T> clazz = getQueryClazz(query);
        return queryIterator(clazz, query);
    }

    @Nullable
    @Override
    public <T> T query(Class<T> clazz, Selection selection) {
        final Query query = new Query(clazz)
                .selection(selection);
        return query(clazz, query);
    }

    @Override
    public <T> T query(Class<T> clazz, Query query) {

        final Cursor cursor = getCursor(query.limit(1));
        if (cursor == null) {
            return null;
        }

        final CursorParser<T> parser2 = new TableCursorParser<>(clazz, manager);
        final T value = parser2.parse(cursor);

        cursor.close();

        return value;
    }

    @Override
    public <T> List<T> queryList(Class<T> clazz, Query query) {

        final Cursor cursor = getCursor(query);
        if (cursor == null) {
            return null;
        }

        return getMany(clazz, cursor);
    }

    @Nullable
    @Override
    public <T> StormIterator<T> queryIterator(Class<T> clazz, Query query) {

        final Cursor cursor = getCursor(query);
        if (cursor == null) {
            return null;
        }

        return new StormIteratorImpl<>(cursor, new TableCursorParser<>(clazz, manager));
    }

    @Override
    public <T> List<T> queryAll(Class<T> tableClass) {

        final Cursor cursor = getCursor(new Query(tableClass));
        if (cursor == null) {
            return null;
        }

        return getMany(tableClass, cursor);
    }

    @Nullable
    @Override
    public <T> StormIterator<T> queryAllIterator(Class<T> tableClass) {

        final Cursor cursor = getCursor(new Query(tableClass));
        if (cursor == null) {
            return null;
        }

        return new StormIteratorImpl<>(cursor, new TableCursorParser<>(tableClass, manager));
    }

    protected <T> List<T> getMany(Class<T> clazz, Cursor cursor) {

        final List<T> list = new ArrayList<>();
        final CursorParser<T> parser2 = new TableCursorParser<>(clazz, manager);

        // by now we already at the first position
        // because getCursor method checks for Cursor.moveToFirst()
        while (!cursor.isAfterLast()) {
            list.add(parser2.parse(cursor));
            cursor.moveToNext();
        }

        cursor.close();

        return list;
    }

    protected @Nullable Cursor getCursor(Query query) {

        final Cursor cursor = manager.query(query);
        if (cursor == null) {
            return null;
        }

        if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }

        return cursor;
    }

    protected @NonNull <T> Class<T> getQueryClazz(Query query) {

        if (query.getTableClass() != null) {
            //noinspection unchecked
            return (Class<T>) query.getTableClass();
        }

        final String tableName = query.getTableName();
        if (TextUtils.isEmpty(tableName)) {
            throw new IllegalStateException("Query must be supplied with a Class<?> or a tableName");
        }

        final List<CachedTable> cachedTables = manager.getCachedTables();
        for (CachedTable table: cachedTables) {
            if (tableName.equals(table.getTableName())) {
                //noinspection unchecked
                return (Class<T>) table.getTableClass();
            }
        }

        throw new IllegalStateException("The specified tableName: " + tableName
                + " is not found in the underlying DatabaseManager");
    }
}
