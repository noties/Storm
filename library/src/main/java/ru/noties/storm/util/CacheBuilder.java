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

package ru.noties.storm.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.Map;

import ru.noties.storm.CachedTable;
import ru.noties.storm.FieldsParser;
import ru.noties.storm.StormUriCreator;
import ru.noties.storm.FieldHolder;
import ru.noties.storm.TableNameParser;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 25.01.2015.
 */
public class CacheBuilder {

    private final TableNameParser   tableNameParser;
    private final StormUriCreator   uriCreator;
    private final FieldsParser      fieldsParser;

    private final String dbName;

    private final boolean mSkipTableNameParsing;

    public CacheBuilder() {
        this(null, null);
    }

    public CacheBuilder(@Nullable String applicationId, @Nullable String dbName) {
        tableNameParser = new TableNameParser();
        uriCreator      = applicationId != null ? new StormUriCreator(applicationId) : null;
        fieldsParser    = new FieldsParser();
        this.dbName     = dbName == null ? null : dbName;
        mSkipTableNameParsing = applicationId == null || dbName == null;
    }

    public Map<Class<?>, CachedTable> buildCache(@NonNull final Class<?>[] classes) {

        final Map<Class<?>, CachedTable> map = MapUtils.create();

        CachedTable table;

        for (Class<?> clazz: classes) {
            table = buildTableCache(clazz);
            map.put(clazz, table);
        }

        return map;
    }

    public @NonNull CachedTable buildTableCache(@NonNull Class<?> clazz) {

        final String tableName = mSkipTableNameParsing ? null : tableNameParser.getTableName(clazz);

        final String uri = !mSkipTableNameParsing
                ? uriCreator.createUriString(dbName, tableName)
                : null;

        final List<FieldHolder> fieldHolders = fieldsParser.getFields(clazz);

        if (fieldHolders == null) {
            throw new IllegalArgumentException("Clazz: " + clazz + " has no columns");
        }

        return new CachedTable(clazz, tableName, uri, fieldHolders);
    }
}
