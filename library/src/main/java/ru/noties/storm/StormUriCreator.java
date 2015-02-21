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
import android.support.annotation.NonNull;

/**
 * Generates an {@link android.net.Uri} for specified table
 *
 * comexample://storm/db_name/table_name
 *
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) 01.02.15.
 */
public class StormUriCreator {

    private static final String URI_PATTERN = "%1$s://storm/%2$s/%3$s";
    private static final String CLEAN_PATTERN = "([^a-zA-Z0-9])";

    private final String mBase;

    public StormUriCreator(@NonNull String applicationId) {
        this.mBase = getCleanString(applicationId);
    }

    public String getCleanString(@NonNull String base) {
        return base.replaceAll(CLEAN_PATTERN, "");
    }

    public Uri createUri(@NonNull String dbName, @NonNull String tableName) {
        return Uri.parse(createUriString(dbName, tableName));
    }

    public String createUriString(@NonNull String dbName, @NonNull String tableName) {
        return String.format(URI_PATTERN, mBase, dbName, tableName);
    }
}
