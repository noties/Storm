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

package ru.noties.storm.loader;

import android.content.Context;
import android.net.Uri;

import ru.noties.storm.DatabaseManager;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 21.02.2015.
 */
public abstract class AbsStormLoader<T> extends AbsLoader<T> {

    private final DatabaseManager mManager;

    public AbsStormLoader(Context context, DatabaseManager manager) {
        super(context);
        this.mManager = manager;
    }

    protected DatabaseManager getManager() {
        return mManager;
    }

    /**
     * If you are using tables from database you could call
     * {@link ru.noties.storm.DatabaseManager#getNotificationUri(Class)}.
     *
     * Else you should construct your own - and don't forget
     * to send a notification when an insert/update/delete operation was executed
     *
     * @see ru.noties.storm.StormUriCreator
     *
     * @return {@link android.net.Uri}
     */
    @Override
    protected abstract Uri getNotificationUri();
}
