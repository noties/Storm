package ru.noties.performance_test;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 09.02.2015.
 */
public interface IObject {

    public void setId(long id);

    public void setSomeString(String someString);

    public void setSomeLong(long someLong);

    public void setSomeShort(short someShort);

    public void setSomeBool(boolean someBool);

    public void setSomeFloat(float someFloat);

    public void setSomeDouble(double someDouble);

    public void setSomeInt(int someInt);
}
