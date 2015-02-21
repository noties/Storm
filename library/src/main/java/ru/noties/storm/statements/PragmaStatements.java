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

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ru.noties.storm.Pragma;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 29.01.2015.
 */
public class PragmaStatements implements Iterator<String>, Iterable<String> {

    private static final String PRAGMA = "PRAGMA %1$s = \'%2$s\'";

    private final List<String> mStatements;
    private int mIndex = 0;

    public PragmaStatements(@Nullable Pragma pragma) {
        this.mStatements = getStatements(pragma);
    }

    private List<String> getStatements(@Nullable Pragma pragma) {

        if (pragma == null) {
            return null;
        }

        final List<String> strings = new ArrayList<>();

        String sql;

        sql = getSynchronous(pragma.getSynchronous());
        if (sql != null) {
            strings.add(sql);
        }

        sql = getJournalMode(pragma.getJournalMode());
        if (sql != null) {
            strings.add(sql);
        }

        sql = getForeignKeys(pragma.getForeignKeys());
        if (sql != null) {
            strings.add(sql);
        }

        sql = getTempStore(pragma.getTempStore());
        if (sql != null) {
            strings.add(sql);
        }

        final List<String> customPragmas = pragma.getCustomPragmas();
        if (customPragmas != null) {
            strings.addAll(customPragmas);
        }

        if (strings.size() == 0) {
            return null;
        }

        return strings;
    }

    private String getSynchronous(Pragma.Synchronous synchronous) {
        if (synchronous == null
                || synchronous == Pragma.Synchronous.SKIP) {
            return null;
        }

        return getPragmaStatement(synchronous.getName(), String.valueOf(synchronous.getValue()));
    }

    private String getJournalMode(Pragma.JournalMode journalMode) {
        if (journalMode == null
                || journalMode == Pragma.JournalMode.SKIP) {
            return null;
        }

        return getPragmaStatement(journalMode.getName(), journalMode.getValue());
    }

    private String getForeignKeys(Pragma.ForeignKeys foreignKeys) {
        if (foreignKeys == null
                || foreignKeys == Pragma.ForeignKeys.SKIP) {
            return null;
        }

        return getPragmaStatement(foreignKeys.getName(), String.valueOf(foreignKeys.getValue()));
    }

    private String getTempStore(Pragma.TempStore tempStore) {
        if (tempStore == null
                || tempStore == Pragma.TempStore.SKIP) {
            return null;
        }

        return getPragmaStatement(tempStore.getName(), String.valueOf(tempStore.getValue()));
    }

    private String getPragmaStatement(String key, String value) {
        return String.format(PRAGMA, key, value);
    }

    @Override
    public Iterator<String> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return mStatements != null && mIndex < mStatements.size();
    }

    @Override
    public String next() {
        return mStatements.get(mIndex++);
    }

    @Override
    public void remove() {
        // do nothing
    }
}
