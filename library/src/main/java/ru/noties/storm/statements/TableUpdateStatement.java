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

package ru.noties.storm.statements;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import ru.noties.storm.anno.NewColumn;
import ru.noties.storm.anno.NewTable;
import ru.noties.storm.FieldHolder;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 29.01.2015.
 */
public class TableUpdateStatement extends TableCreateStatement {

    public static final String ALTER_TABLE_ADD_COLUMN = "ALTER TABLE %1$s ADD COLUMN %2$s";

    private static final Class<NewColumn> NEW_COLUMN_CLASS  = NewColumn.class;
    private static final Class<NewTable>  NEW_TABLE_CLASS   = NewTable.class;

    private final int mOldVersion;
    private final int mNewVersion;

    public TableUpdateStatement(int oldVersion, int newVersion) {
        this.mOldVersion = oldVersion;
        this.mNewVersion = newVersion;
    }

    public List<String> getStatements(Class<?> clazz, String tableName, List<FieldHolder> holders) {

        final List<String> statements = new ArrayList<>();

        // if class if annotated with NewTable - add full create statement
        final boolean isNewTable = isNewTable(clazz);

        if (isNewTable) {
            statements.addAll(super.getStatements(tableName, holders));
            return statements;
        }

        // check for each field
        for (FieldHolder holder: holders) {
            if (isNewColumn(holder)) {
                statements.addAll(getAlterStatements(tableName, holder));
            }
        }

        return statements;
    }

    private List<String> getAlterStatements(String tableName, FieldHolder holder) {

        final List<String> statements = new ArrayList<>();

        final String field = super.createField(holder);
        final List<String> indices = createIndicesStatements(tableName);

        final String alterStatement = String.format(ALTER_TABLE_ADD_COLUMN, tableName, field);

        statements.add(alterStatement);
        if (indices != null
                && indices.size() > 0) {
            statements.addAll(indices);
        }

        return statements;
    }

    private boolean isNewTable(Class<?> clazz) {

        if (!clazz.isAnnotationPresent(NEW_TABLE_CLASS)) {
            return false;
        }

        final NewTable newTable = clazz.getAnnotation(NEW_TABLE_CLASS);
        final int newInVersion = newTable.value();

        return checkVersion(newInVersion);
    }

    private boolean checkVersion(int whenAdded) {
        return whenAdded > mOldVersion && whenAdded <= mNewVersion;
    }

    private boolean isNewColumn(FieldHolder holder) {
        final Field field = holder.getField();
        if (!field.isAnnotationPresent(NEW_COLUMN_CLASS)) {
            return false;
        }

        final NewColumn newColumn = field.getAnnotation(NEW_COLUMN_CLASS);
        return checkVersion(newColumn.value());
    }
}
