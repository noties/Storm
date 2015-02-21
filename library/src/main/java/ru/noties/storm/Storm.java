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

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Map;

import ru.noties.storm.op.Delete;
import ru.noties.storm.op.DeleteImpl;
import ru.noties.storm.op.Insert;
import ru.noties.storm.op.InsertImpl;
import ru.noties.storm.op.Select;
import ru.noties.storm.op.SelectImpl;
import ru.noties.storm.op.Update;
import ru.noties.storm.op.UpdateImpl;
import ru.noties.storm.sd.AbsSerializer;
import ru.noties.storm.util.MapUtils;
import ru.noties.storm.vp.ContentValuesSetterFactory;
import ru.noties.storm.vp.CursorValueProviderFactory;
import ru.noties.storm.vp.FieldValueGetterFactory;
import ru.noties.storm.vp.FieldValueSetterFactory;


/**
 * The main class of Storm library.
 *
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) 01.02.15.
 */
public class Storm {

    private static volatile Storm sInstance = null;

    public static Storm getInstance() {
        Storm local = sInstance;
        if (local == null) {
            synchronized (Storm.class) {
                local = sInstance;
                if (local == null) {
                    local = sInstance = new Storm();
                }
            }
        }
        return local;
    }

    private final InstanceCreatorsMap mInstanceCreators;
    private final Map<Class<?>, AbsSerializer<Object>> mSerializers;

    private Storm() {
        mInstanceCreators = new InstanceCreatorsMap();
        mSerializers      = MapUtils.create();
    }

    private Context mApplicationContext;

    // factories
    private FieldValueGetterFactory     mFieldValueGetterFactory;
    private FieldValueSetterFactory     mFieldValueSetterFactory;
    private CursorValueProviderFactory  mCursorValueProviderFactory;
    private ContentValuesSetterFactory  mContentValuesSetterFactory;

    private boolean mIsDebug;

    private boolean mIsInitCalled;

    /**
     * The main entry point for the Storm library.
     * Should be called only once - Application's onCreate() is a good start
     * This method won't open any SQLite database connections. See {@link DatabaseManager#open()}
     *
     * @param applicationContext {@link android.content.Context} of an {@link android.app.Application}
     * @param isDebug flag to indicate that current build is debug,
     *                may be helpful when debugging as Storm
     *                will throw {@link java.lang.Exception} more aggressively
     * @throws java.lang.AssertionError if this method was already called
     */
    public void init(Context applicationContext, boolean isDebug) {

        if (mIsInitCalled) {
            throw new AssertionError("init() has already been called");
        }

        mApplicationContext         = applicationContext;

        mFieldValueGetterFactory    = new FieldValueGetterFactory();
        mFieldValueSetterFactory    = new FieldValueSetterFactory();
        mCursorValueProviderFactory = new CursorValueProviderFactory();
        mContentValuesSetterFactory = new ContentValuesSetterFactory();

        mIsDebug = isDebug;
        mIsInitCalled = true;
    }

    /**
     * Registers {@link ru.noties.storm.InstanceCreator} for the specified {@link java.lang.Class}.
     * It may be helpful and performance wise if, for example, an {@link java.lang.Object}
     * has no empty constructor or has very specific constructor,
     * or if construction of an Object via Reflection is not desired
     * (default {@link ru.noties.storm.InstanceCreator} for
     * all objects is {@link ru.noties.storm.ReflectionInstanceCreator})
     * @param clazz to be registered
     * @param ics instance of {@link ru.noties.storm.InstanceCreator}
     * @param <T> Type of object that {@link ru.noties.storm.InstanceCreator} will handle
     * @param <IC> {@link ru.noties.storm.InstanceCreator} of type T
     */
    public <T, IC extends InstanceCreator<T>> void registerInstanceCreator(@NonNull Class<T> clazz, @NonNull IC ics) {
        mInstanceCreators.register(clazz, ics);
    }

    /**
     * Unregisters previously registered {@link ru.noties.storm.InstanceCreator}
     * @see #registerInstanceCreator(Class, InstanceCreator)
     * @param clazz to be unregistered
     */
    public void unregisterInstanceCreator(@NonNull Class<?> clazz) {
        mInstanceCreators.unregister(clazz);
    }

    /**
     * Registers Type serializer (aka not supported SQLite types) {@link ru.noties.storm.sd.AbsSerializer}
     * As a matter of fact {@link ru.noties.storm.sd.AbsSerializer} has only one method that
     * indicates what SQLite type({@link ru.noties.storm.FieldType}) this type will represent.
     * Methods <code>serialize</code> and <code>deserialize</code>
     * are not in the inheritance tree. This is done due to the autoboxing issue.
     *
     * If you wish to supply your own TypeSerializer you should definitely extend one of the following:
     * {@link ru.noties.storm.sd.ShortSerializer}
     * {@link ru.noties.storm.sd.IntSerializer}
     * {@link ru.noties.storm.sd.LongSerializer}
     * {@link ru.noties.storm.sd.FloatSerializer}
     * {@link ru.noties.storm.sd.DoubleSerializer}
     * {@link ru.noties.storm.sd.StringSerializer}
     * {@link ru.noties.storm.sd.ByteArraySerializer}
     * {@link ru.noties.storm.sd.BooleanSerializer}
     *
     * Additionally you could easily register Serialization
     * for an Enum {@link ru.noties.storm.sd.EnumSerializer}
     *
     * @param who Class to be registered
     * @param serializer {@link ru.noties.storm.sd.AbsSerializer}
     * @param <T> Type to be linked with
     */
    public <T> void registerTypeSerializer(Class<T> who, AbsSerializer<T> serializer) {
        //noinspection unchecked
        mSerializers.put(who, (AbsSerializer<Object>) serializer);
    }

    /**
     * Unregisters type serializer
     * @see #registerTypeSerializer(Class, ru.noties.storm.sd.AbsSerializer)
     * @param clazz to be unregistered
     */
    public void unregisterTypeSerializer(Class<?> clazz) {
        mSerializers.remove(clazz);
    }

    /**
     * Prepares new insert operation
     * @param manager {@link ru.noties.storm.DatabaseManager} to operate on
     * @return new instance of {@link ru.noties.storm.op.Insert}
     */
    public static Insert newInsert(DatabaseManager manager) {
        return new InsertImpl(manager);
    }

    /**
     * Prepares new delete operation
     * @param manager {@link ru.noties.storm.DatabaseManager} to operate on
     * @return new instance of {@link ru.noties.storm.op.Delete}
     */
    public static Delete newDelete(DatabaseManager manager) {
        return new DeleteImpl(manager);
    }

    /**
     * Prepares new update operation
     * @param manager {@link ru.noties.storm.DatabaseManager} to operate on
     * @return new instance of {@link ru.noties.storm.op.Update}
     */
    public static Update newUpdate(DatabaseManager manager) {
        return new UpdateImpl(manager);
    }

    /**
     * Prepares new select operation
     * @param manager {@link ru.noties.storm.DatabaseManager} to operate on
     * @return new instance of {@link ru.noties.storm.op.Select}
     */
    public static Select newSelect(DatabaseManager manager) {
        return new SelectImpl(manager);
    }


     // Although these methods are public they are not intended to be used directly

    public static boolean isDebug() {
        return Storm.getInstance().mIsDebug;
    }

    public static Context getApplicationContext() {
        return Storm.getInstance().mApplicationContext;
    }

    public static <T> InstanceCreator<T> getInstanceCreator(Class<T> clazz) {
        final Storm storm = Storm.getInstance();
        return storm.mInstanceCreators.get(clazz);
    }

    public @Nullable static AbsSerializer<Object> getTypeSerializer(Class<?> clazz) {
        return Storm.getInstance().mSerializers.get(clazz);
    }

    static Map<Class<?>, AbsSerializer<Object>> getSerializers() {
        return Storm.getInstance().mSerializers;
    }

    public static FieldValueGetterFactory getFieldValueGetterFactory() {
        return Storm.getInstance().mFieldValueGetterFactory;
    }

    public static FieldValueSetterFactory getFieldValueSetterFactory() {
        return Storm.getInstance().mFieldValueSetterFactory;
    }

    public static CursorValueProviderFactory getCursorValueProviderFactory() {
        return Storm.getInstance().mCursorValueProviderFactory;
    }

    public static ContentValuesSetterFactory getContentValuesProviderFactory() {
        return Storm.getInstance().mContentValuesSetterFactory;
    }
}
