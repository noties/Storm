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

import android.content.ContentValues;
import android.database.sqlite.SQLiteException;

import java.util.List;

import ru.noties.storm.CachedTable;
import ru.noties.storm.ContentValuesCreator;
import ru.noties.storm.DatabaseManager;
import ru.noties.storm.FieldHolder;
import ru.noties.storm.exc.NoPrimaryKeyFoundException;
import ru.noties.storm.exc.StormException;
import ru.noties.storm.query.Selection;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 06.02.2015.
 */
public class UpdateImpl extends AbsSQLiteOp implements Update {

    public UpdateImpl(DatabaseManager manager) {
        super(manager);
    }

    @Override
    public int update(Object value) throws StormException {
        return update(value, true);
    }

    @Override
    public int update(Object value, boolean shouldNotify) throws StormException {
        return update(value, shouldNotify, false);
    }

    @Override
    public int update(Object value, boolean shouldNotify, boolean setPrimaryKey) throws StormException {

        // construct selection and pass further
        final CachedTable table = super.getCachedTable(value);

        Selection selection;

        try {
            selection = getSelection(table, value);
        } catch (NoPrimaryKeyFoundException | IllegalAccessException e) {
            throw new StormException(e);
        }

        return update(value, selection, shouldNotify, setPrimaryKey);
    }

    @Override
    public int update(Object value, Selection selection) throws StormException {
        return update(value, selection, true);
    }

    @Override
    public int update(Object value, Selection selection, boolean shouldNotify) throws StormException {
        return update(value, selection, shouldNotify, false);
    }

    @Override
    public int update(Object value, Selection selection, boolean shouldNotify, boolean setPrimaryKey) throws StormException {

        final CachedTable table = super.getCachedTable(value);

        final ContentValuesCreator creator = new ContentValuesCreator();
        final ContentValues cv = creator.toContentValues(table, value, setPrimaryKey, null);

        int result;

        try {
            result = manager.update(table.getTableName(), selection, cv);
        } catch (SQLiteException e) {
            throw new StormException(e);
        }

        if (shouldNotify) {
            manager.notifyChange(table.getNotificationUri());
        }

        return result;
    }

    @Override
    public int update(List<?> values) throws StormException {
        return update(values, true);
    }

    @Override
    public int update(List<?> values, boolean shouldNotify) throws StormException {
        return update(values, shouldNotify, false);
    }

    @Override
    public int update(List<?> values, boolean shouldNotify, boolean setPrimaryKey) throws StormException {

        final CachedTable table = super.getCachedTable(values);

        FieldHolder primaryKey;

        try {
            primaryKey = getPrimaryKey(table);
        } catch (NoPrimaryKeyFoundException e) {
            throw new StormException(e);
        }

        final ContentValuesCreator creator = new ContentValuesCreator();
        ContentValues cv;
        Selection selection;

        int result = 0;

        beginTransaction();

        try {
            for (Object who: values) {
                try {
                    selection = getSelection(primaryKey, who);
                } catch (IllegalAccessException e) {
                    throw new StormException(e);
                }

                cv = creator.toContentValues(table, who, setPrimaryKey, null);
                result += manager.update(table.getTableName(), selection, cv);
            }
            setTransactionSuccessful();
        } catch (SQLiteException e) {
            throw new StormException(e);
        } finally {
            endTransaction();
        }

        if (shouldNotify) {
            manager.notifyChange(table.getNotificationUri());
        }

        return result;
    }
}
