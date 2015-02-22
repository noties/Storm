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

package ru.noties.storm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 22.02.2015.
 */
public abstract class BaseStormAdapter<T> extends BaseAdapter {

    private final LayoutInflater mInflater;

    public BaseStormAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public abstract T getItem(int position);

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final View view;
        if (convertView == null) {
            view = newView(getInflater(), position, parent);
        } else {
            view = convertView;
        }

        bindView(position, view);

        return view;
    }

    protected abstract View newView(LayoutInflater inflater, int position, ViewGroup group);
    protected abstract void bindView(int position, View view);

    protected LayoutInflater getInflater() {
        return mInflater;
    }
}
