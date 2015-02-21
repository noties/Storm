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

package ru.noties.storm;

import android.net.Uri;

import java.util.List;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 25.01.2015.
 */
public class CachedTable {

    private final Class<?> tableClass;
    private final String tableName;
    private final String notificationUri;
    private final List<FieldHolder> fields;

    private Uri uri;

    public CachedTable(
            Class<?> clazz,
            String tableName,
            String notificationUri,
            List<FieldHolder> fields
    ) {
        this.tableClass = clazz;
        this.tableName       = tableName;
        this.notificationUri = notificationUri;
        this.fields          = fields;
    }

    public String getTableName() {
        return tableName;
    }

    public Uri getNotificationUri() {
        if (uri == null) {
            uri = Uri.parse(notificationUri);
        }
        return uri;
    }

    public List<FieldHolder> getFields() {
        return fields;
    }

    public Class<?> getTableClass() {
        return tableClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CachedTable that = (CachedTable) o;

        if (tableName != null ? !tableName.equals(that.tableName) : that.tableName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return tableName != null ? tableName.hashCode() : 0;
    }
}
