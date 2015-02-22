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

import java.util.List;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 22.02.2015.
 */
public abstract class BaseStormListAdapter<T> extends BaseStormAdapter<T> implements StormListAdapter<T> {

    private List<T> mList;

    public BaseStormListAdapter(Context context) {
        super(context);
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public void setItems(List<T> list) {
        this.setItems(list, true);
    }

    @Override
    public void setItems(List<T> list, boolean shouldNotify) {
        mList = list;
        if (shouldNotify) {
            notifyDataSetChanged();
        }
    }

    @Override
    public List<T> getItems() {
        return mList;
    }
}
