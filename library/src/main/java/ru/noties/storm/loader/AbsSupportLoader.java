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

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 21.02.2015.
 */
public abstract class AbsSupportLoader<T> extends AsyncTaskLoader<T> {

    private final ForceLoadContentObserver mObserver;

    private T mValue;

    public AbsSupportLoader(Context context) {
        super(context);
        this.mObserver = new ForceLoadContentObserver();
    }

    @Override
    public void onStartLoading() {

        if(hasValue()) {
            deliverResult(mValue);
        }

        registerObserver();

        forceLoad();
    }

    @Override
    public void onStopLoading() {
        cancelLoad();
    }

    @Override
    public void onReset() {
        super.onReset();

        onStopLoading();

        unregisterObserver();
    }

    @Override
    public T loadInBackground() {
        try {
            mValue = loadValue();
            return mValue;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    protected void registerObserver() {
        getResolver().registerContentObserver(
                getNotificationUri(),
                false,
                mObserver
        );
    }

    protected void unregisterObserver() {
        getResolver().unregisterContentObserver(mObserver);
    }

    protected ContentResolver getResolver() {
        return getContext().getContentResolver();
    }

    protected boolean hasValue() {
        return mValue != null;
    }

    protected abstract T    loadValue();
    protected abstract Uri  getNotificationUri();
}
