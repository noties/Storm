package ru.noties.performance_test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ru.noties.storm.InstanceCreator;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 31.01.2015.
 */
public class TestRandomizer {

    private final Random random;

    public TestRandomizer() {
        random = new Random();
    }

    public void randomize(IObject object) {
        object.setSomeBool(random.nextBoolean());
        object.setSomeDouble(random.nextDouble());
        object.setSomeFloat(random.nextFloat());
        object.setSomeString("string" + random.nextGaussian());
        object.setSomeInt(random.nextInt());
        object.setSomeLong(random.nextLong());
        object.setSomeShort((short) random.nextInt());
    }

    public <T extends IObject> List<T> create(int howHuch, InstanceCreator<T> creator) {
        final List<T> list = new ArrayList<>(howHuch);
        T object;
        for (int i = 0; i < howHuch; i++) {
            object = creator.create(null);
            randomize(object);
            list.add(object);
        }
        return list;
    }
}
