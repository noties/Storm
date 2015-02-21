package ru.noties.performance_test.aa;

import com.activeandroid.serializer.TypeSerializer;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 13.02.2015.
 */
public class ShortSerializer extends TypeSerializer {

    @Override
    public Class<?> getDeserializedType() {
        return short.class;
    }

    @Override
    public Class<?> getSerializedType() {
        return short.class;
    }

    @Override
    public Short serialize(Object data) {
        return (short) data;
    }

    @Override
    public Short deserialize(Object data) {
        return (short) data;
    }
}
