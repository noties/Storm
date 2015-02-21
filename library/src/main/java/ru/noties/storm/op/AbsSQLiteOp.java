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

import android.support.annotation.NonNull;

import java.util.List;

import ru.noties.storm.CachedTable;
import ru.noties.storm.DatabaseManager;
import ru.noties.storm.FieldHolder;
import ru.noties.storm.exc.NoPrimaryKeyFoundException;
import ru.noties.storm.query.Selection;
import ru.noties.storm.vp.SelectionFieldValueGetter;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 08.02.2015.
 */
public abstract class AbsSQLiteOp implements Transactional {

    protected final DatabaseManager manager;

    public AbsSQLiteOp(DatabaseManager manager) {
        this.manager = manager;
    }

    protected final Selection getSelection(CachedTable table, Object who) throws NoPrimaryKeyFoundException, IllegalAccessException {
        final FieldHolder holder = getPrimaryKey(table);
        return getSelection(holder, who);
    }

    protected @NonNull final CachedTable getCachedTable(Object who) {
        return getCachedTable(who.getClass());
    }

    protected @NonNull final CachedTable getCachedTable(List<?> values) {
        return getCachedTable(values.get(0).getClass());
    }

    protected @NonNull final CachedTable getCachedTable(Class<?> clazz) {
        return manager.getCachedTable(clazz);
    }

    protected final FieldHolder getPrimaryKey(CachedTable table) throws NoPrimaryKeyFoundException {

        final List<FieldHolder> fields = table.getFields();

        FieldHolder primaryKey = null;

        for (FieldHolder field: fields) {
            if (field.isPrimaryKey()) {
                primaryKey = field;
            }
        }

        if (primaryKey == null) {
            throw new NoPrimaryKeyFoundException();
        }

        return primaryKey;
    }

    protected final Selection getSelection(FieldHolder field, Object who) throws IllegalAccessException {

        final Object what = SelectionFieldValueGetter.get(field.getType(), field.getFieldDelegate(), who);

        return Selection.eq(field.getName(), what);
    }

    @Override
    public void beginTransaction() {
        manager.beginTransaction();
    }

    @Override
    public void setTransactionSuccessful() {
        manager.setTransactionSuccessful();
    }

    @Override
    public void endTransaction() {
        manager.endTransaction();
    }
}
