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

package ru.noties.storm.test;

import android.test.AndroidTestCase;

import java.util.Date;

import ru.noties.storm.InstanceCreator;
import ru.noties.storm.Storm;
import ru.noties.storm.StormMediator;
import ru.noties.storm.sd.LongSerializer;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 23.02.2015.
 */
public class StormTest extends AndroidTestCase {

    public void testStormInitialization() {

        final Storm storm = getStorm();

        assertEquals(false, Storm.isDebug());
        assertNull(Storm.getApplicationContext());

        storm.init(getContext(), true);
        assertNotNull(Storm.getApplicationContext());
        assertEquals(getContext(), Storm.getApplicationContext());

        assertEquals(true, Storm.isDebug());

        try {
            storm.init(getContext(), true);
            assertTrue(false);
        } catch (AssertionError e) {
            assertTrue(true);
        }
    }

    public void testTypeSerializers() {
        final LongSerializer<Date> serializer = new LongSerializer<Date>() {
            @Override
            public Date deserialize(long value) {
                return null;
            }

            @Override
            public long serialize(Date value) {
                return 0;
            }
        };

        final Storm storm = getStorm();
        storm.registerTypeSerializer(Date.class, serializer);
        storm.registerTypeSerializer(Date.class, serializer);

        assertEquals(1, StormMediator.getSerializers().size());

        storm.unregisterTypeSerializer(Date.class);

        assertEquals(0, StormMediator.getSerializers().size());
    }

    public void testInstanceCreators() {
        final InstanceCreator<Date> instanceCreator = new InstanceCreator<Date>() {
            @Override
            public Date create(Class<Date> clazz) {
                return null;
            }
        };

        final Storm storm = getStorm();
        storm.registerInstanceCreator(Date.class, instanceCreator);
        storm.registerInstanceCreator(Date.class, instanceCreator);

        assertEquals(1, StormMediator.getInstanceCreators().size());

        storm.unregisterInstanceCreator(Date.class);
        assertEquals(0, StormMediator.getInstanceCreators().size());
    }

    private Storm getStorm() {
        return Storm.getInstance();
    }
}
