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

package ru.noties.storm.sample;

import android.app.Application;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.google.gson.Gson;

import java.util.Date;

import ru.noties.debug.Debug;
import ru.noties.storm.InstanceCreator;
import ru.noties.storm.Storm;
import ru.noties.storm.sample.model.SampleObject;
import ru.noties.storm.sample.tableExample.TableModel;
import ru.noties.storm.sd.ByteArraySerializer;
import ru.noties.storm.sd.EnumSerializer;
import ru.noties.storm.sd.LongSerializer;
import ru.noties.storm.sd.StringSerializer;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 17.02.2015.
 */
public class MainApplication extends Application {

    public void onCreate() {
        super.onCreate();

        Debug.init(true);

        Storm.getInstance().init(this, true);
        final Storm storm = Storm.getInstance();
        storm.registerInstanceCreator(SampleObject.class, new InstanceCreator<SampleObject>() {
            @Override
            public SampleObject create(Class<SampleObject> clazz) {
                return new SampleObject();
            }
        });

        storm.registerTypeSerializer(Date.class, new LongSerializer<Date>() {
            @Override
            public Date deserialize(long value) {
                return new Date(value);
            }

            @Override
            public long serialize(Date value) {
                return value.getTime();
            }
        });

        registerGson(storm, SampleObject.Wrapper.class);

        storm.registerTypeSerializer(TableModel.SomeEnum.class, new EnumSerializer<>(TableModel.SomeEnum.values()));
//        storm.registerTypeSerializer(ApiModel.class, new GenericBytesSerializer<>(ApiModel.class));
    }

    private <T> void registerGson(Storm storm, Class<T> clazz) {
        storm.registerTypeSerializer(clazz, new GenericGsonSerializer<>(clazz));
    }

    private static class  GenericGsonSerializer<T> extends StringSerializer<T> {

        private static final Gson gson;
        static {
            gson = new Gson();
        }
        private final Class<T> clazz;

        GenericGsonSerializer(Class<T> clazz) {
            this.clazz = clazz;
        }

        @Override
        public T deserialize(String in) {
            return gson.fromJson(in, clazz);
        }

        @Override
        public String serialize(T in) {
            return gson.toJson(in);
        }
    }

    private static class GenericBytesSerializer<T> extends ByteArraySerializer<T> {

        private static final Kryo kryo;
        static {
            kryo = new Kryo();
        }
        private final Class<T> clazz;

        private GenericBytesSerializer(Class<T> clazz) {
            this.clazz = clazz;
        }

        @Override
        public T deserialize(byte[] value) {
            kryo.register(clazz);
            final Input input = new Input(value);
            try {
                return kryo.readObject(input, clazz);
            } finally {
                input.close();
            }
        }

        @Override
        public byte[] serialize(Object value) {
            kryo.register(clazz);
            final Output output = new Output(4096);
            kryo.writeObject(output, value);
            try {
                return output.toBytes();
            } finally {
                output.close();
            }
        }
    }
}
