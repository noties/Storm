package ru.noties.performance_test.ui;

import ru.noties.performance_test.OpType;
import ru.noties.performance_test.Time;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 14.02.2015.
 */
public class AdapterItem {

    public static enum Type {
        HEADER,
        ITEM
    }

    public static AdapterItem newHeader(OpType opType, int rounds) {
        return new AdapterItem(Type.HEADER, opType, rounds, null);
    }

    public static AdapterItem newItem(OpType opType, int rounds, Time time) {
        return new AdapterItem(Type.ITEM, opType, rounds, time);
    }

    public static AdapterItem newDummy() {
        return new AdapterItem(null, null, 0, null, true);
    }

    private final boolean mIsDummy;

    private final Type type;
    private OpType opType;
    private int rounds;
    private final Time time;

    private int color;
    private String name;
    private long value;

    private AdapterItem(Type type, OpType opType, int rounds, Time time) {
        this(type, opType, rounds, time, false);
    }

    private AdapterItem(Type type, OpType opType, int rounds, Time time, boolean isDummy) {
        this.type = type;
        this.opType = opType;
        this.rounds = rounds;
        this.time = time;
        this.mIsDummy = isDummy;
    }

    public Type getType() {
        return type;
    }

    public OpType getOpType() {
        return opType;
    }

    public int getRounds() {
        return rounds;
    }

    public void setOpType(OpType opType) {
        if (!mIsDummy) {
            throw new AssertionError("Cannot set value OpType to non dummy AdapterItem");
        }
        this.opType = opType;
    }

    public void setRounds(int rounds) {
        if (!mIsDummy) {
            throw new AssertionError("Cannot set value rounds to non dummy AdapterItem");
        }
        this.rounds = rounds;
    }

    public Time getTime() {
        return time;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdapterItem item = (AdapterItem) o;

        if (rounds != item.rounds) return false;
        if (opType != item.opType) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = opType != null ? opType.hashCode() : 0;
        result = 31 * result + rounds;
        return result;
    }
}
