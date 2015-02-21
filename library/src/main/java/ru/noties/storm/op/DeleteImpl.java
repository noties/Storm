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

import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;

import ru.noties.storm.CachedTable;
import ru.noties.storm.DatabaseManager;
import ru.noties.storm.FieldHolder;
import ru.noties.storm.Storm;
import ru.noties.storm.exc.NoPrimaryKeyFoundException;
import ru.noties.storm.exc.StormException;
import ru.noties.storm.query.Selection;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 06.02.2015.
 */
public class DeleteImpl extends AbsSQLiteOp implements Delete {

    private static final int MAX = 750;

    public DeleteImpl(DatabaseManager manager) {
        super(manager);
    }

    @Override
    public int delete(Object value) throws StormException {
        return delete(value, true);
    }

    @Override
    public int delete(Object value, boolean shouldNotify) throws StormException {

        final CachedTable table = super.getCachedTable(value);

        Selection selection;

        try {
            selection = getSelection(table, value);
        } catch (NoPrimaryKeyFoundException | IllegalAccessException e) {
            throw new StormException(e);
        }

        final int result = deleteInner(table.getTableName(), selection);

        if (shouldNotify
                && result > 0) {
            manager.notifyChange(table.getNotificationUri());
        }

        return result;
    }

    @Override
    public int delete(List<?> values) throws StormException {
        return delete(values, true);
    }

    /**
     * If values' size hit {@link #MAX}, then query will be split (size % MAX + [1])
     *
     * {@inheritDoc}
     */
    @Override
    public int delete(List<?> values, boolean shouldNotify) throws StormException {

        final CachedTable table = super.getCachedTable(values);

        FieldHolder primaryKey;

        try {
            primaryKey = getPrimaryKey(table);
        } catch (NoPrimaryKeyFoundException e) {
            throw new StormException(e);
        }

        final List<Selection> selections = new ArrayList<>();

        final int size   = values.size();
        int x = size / MAX;
        final int steps = x == 0
                ? 1
                : size % MAX != 0
                    ? x + 1
                    : x;

        if (steps > 1) {
            for (int i = 0, end = MAX, start = 0; i < steps; i++, start = end, end += Math.min(size - (MAX * i), MAX)) {
                selections.add(getSelection(primaryKey, values.subList(start, end)));
            }
        } else {
            selections.add(getSelection(primaryKey, values));
        }

        int result = 0;
        beginTransaction();
        try {
            for (Selection selection: selections) {
                result += deleteInner(table.getTableName(), selection);
            }
            setTransactionSuccessful();
        } finally {
            endTransaction();
        }

        if (shouldNotify
                && result > 0) {
            manager.notifyChange(table.getNotificationUri());
        }

        return result;
    }

    private Selection getSelection(FieldHolder primaryKey, List<?> values) throws StormException {

        Selection agrSelection = null;
        Selection selection;

        for (Object value: values) {

            try {
                selection = getSelection(primaryKey, value);
            } catch (IllegalAccessException e) {
                throw new StormException(e);
            }

            if (agrSelection == null) {
                agrSelection = selection;
            } else {
                agrSelection.or(selection);
            }
        }

        return agrSelection;
    }

    @Override
    public int delete(Class<?> clazz, Selection selection) throws StormException {
        return delete(clazz, selection, true);
    }

    @Override
    public int delete(Class<?> clazz, Selection selection, boolean shouldNotify) throws StormException {

        final CachedTable table = super.getCachedTable(clazz);

        final int result = deleteInner(table.getTableName(), selection);

        if (shouldNotify && result > 0) {
            manager.notifyChange(table.getNotificationUri());
        }

        return result;
    }

    @Override
    public int deleteAll(Class<?> clazz) throws StormException {
        return deleteAll(clazz, true);
    }

    @Override
    public int deleteAll(Class<?> clazz, boolean shouldNotify) throws StormException {

        final CachedTable table = super.getCachedTable(clazz);
        final int result = deleteInner(table.getTableName(), null);

        if (shouldNotify && result > 0) {
            manager.notifyChange(table.getNotificationUri());
        }

        return result;
    }

    protected final int deleteInner(String tableName, Selection selection) throws StormException {
        try {
            return manager.delete(tableName, selection);
        } catch (SQLiteException e) {
            throw new StormException(e);
        }
    }
}
