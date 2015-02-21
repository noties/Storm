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

import android.database.Cursor;
import android.support.annotation.Nullable;

import java.util.Map;

import ru.noties.storm.util.ImmutableInt;
import ru.noties.storm.util.MapUtils;
import ru.noties.storm.vp.CursorGetterFieldSetter;
import ru.noties.storm.vp.CursorValueProvider;
import ru.noties.storm.vp.FieldValueSetter;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 08.02.2015.
 */
public abstract class AbsCursorParser<T> implements CursorParser<T> {

    private final Class<T> mClazz;
    private final InstanceCreator<T> mInstanceCreator;
    private final Map<FieldType, CursorGetterFieldSetter<?>> mGettersSetters;

    private Map<String, ImmutableInt> mIndices;

    public AbsCursorParser(Class<T> tableClazz) {
        this.mClazz = tableClazz;
        this.mInstanceCreator = Storm.getInstanceCreator(mClazz);
        this.mGettersSetters  = MapUtils.create();
    }

    protected Map<String, ImmutableInt> getIndices(Cursor cursor) {

        final Map<String, ImmutableInt> map = MapUtils.create();
        final String[] names = cursor.getColumnNames();

        String name;

        for (int i = 0, size = names.length; i < size; i++) {
            name = names[i];
            map.put(name, ImmutableInt.newInstance(i));
        }

        return map;
    }

    protected InstanceCreator<T> getInstanceCreator() {
        return mInstanceCreator;
    }

    protected abstract CachedTable getCachedTable(Class<T> clazz);

    public Class<T> getTableClass() {
        return mClazz;
    }

    @Override
    public T parse(Cursor cursor) {
        return parse(cursor, null);
    }

    @Override
    public T parse(Cursor cursor, @Nullable NameProvider nameProvider) {

        if (mIndices == null) {
            mIndices = getIndices(cursor);
        }

        final T object = mInstanceCreator.create(mClazz);

        CursorGetterFieldSetter<?> getterFieldSetter;

        for (FieldHolder holder: getCachedTable(mClazz).getFields()) {

            final int columnIndex = getColumnIndex(holder.getName());
            if (columnIndex < 0) {
                continue;
            }

            getterFieldSetter = getGetterAndSetter(holder.getType());
            getterFieldSetter.doYourThing(cursor, columnIndex, holder.getFieldDelegate(), object);
        }

        return object;
    }

    protected int getColumnIndex(String name) {
        final ImmutableInt immutableInt = mIndices.get(name);
        if (immutableInt == null) {
            return -1;
        }
        return immutableInt.getValue();
    }

    protected CursorGetterFieldSetter<?> getGetterAndSetter(FieldType type) {

        final CursorGetterFieldSetter<?> getterFieldSetter = mGettersSetters.get(type);
        if (getterFieldSetter != null) {
            return getterFieldSetter;
        }

        // else create new
        // we know that everything should be alright as long as we used the same FieldType

        //noinspection unchecked
        final CursorGetterFieldSetter newGetterAndSetter
                = createNewGetterSetter(type);

        mGettersSetters.put(type, newGetterAndSetter);

        return newGetterAndSetter;
    }

    protected CursorGetterFieldSetter<?> createNewGetterSetter(FieldType type) {
        final CursorValueProvider<?> valueProvider  = Storm.getCursorValueProviderFactory().get(type);
        final FieldValueSetter<?> setter            = Storm.getFieldValueSetterFactory().get(type);
        //noinspection unchecked
        return new CursorGetterFieldSetter(valueProvider, setter);
    }
}
