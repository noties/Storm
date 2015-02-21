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
public class TableCreator implements Iterator<String>, Iterable<String> {

    private final List<String> mList;

    private int mIndex = 0;

    public TableCreator(List<CachedTable> cachedTables) {
        this.mList = getStatements(cachedTables);
    }

    private List<String> getStatements(List<CachedTable> cachedTables) {

        final List<String> statements = new ArrayList<>();

        TableCreateStatement statement;

        for (CachedTable table: cachedTables) {
            statement = new TableCreateStatement();
            statements.addAll(
                    statement.getStatements(table.getTableName(), table.getFields())
            );
        }

        return statements;
    }

    @Override
    public boolean hasNext() {
        return mIndex < mList.size();
    }

    @Override
    public String next() {
        return mList.get(mIndex++);
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
