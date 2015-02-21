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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ru.noties.storm.CachedTable;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) 01.02.15.
 */
public class TableUpdater implements Iterator<String>, Iterable<String> {

    private final int mOldVersion;
    private final int mNewVersion;
    private final List<String> mStatements;

    private int mIndex = 0;

    public TableUpdater(List<CachedTable> tables, int oldVersion, int newVersion) {
        this.mOldVersion    = oldVersion;
        this.mNewVersion    = newVersion;
        this.mStatements    = getStatements(tables);
    }

    private List<String> getStatements(List<CachedTable> list) {

        final List<String> statements = new ArrayList<>();

        TableUpdateStatement statement;

        for (CachedTable table: list) {
            statement = new TableUpdateStatement(mOldVersion, mNewVersion);
            statements.addAll(statement.getStatements(table.getTableClass(), table.getTableName(), table.getFields()));
        }

        return statements;
    }

    @Override
    public boolean hasNext() {
        return mIndex < mStatements.size();
    }

    @Override
    public String next() {
        return mStatements.get(mIndex++);
    }

    @Override
    public void remove() {
        // do nothing
    }

    @Override
    public Iterator<String> iterator() {
        return this;
    }
}
