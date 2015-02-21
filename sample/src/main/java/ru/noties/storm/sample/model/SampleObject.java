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

package ru.noties.storm.sample.model;

import java.util.Date;

import ru.noties.storm.anno.Autoincrement;
import ru.noties.storm.anno.Column;
import ru.noties.storm.anno.PrimaryKey;
import ru.noties.storm.anno.Table;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 21.02.2015.
 */
@Table("sample_table")
public class SampleObject {

    @Column
    @PrimaryKey
    @Autoincrement
    private long id;

    @Column
    private Date someDate;

    @Column
    private String someString;

    @Column
    private int someInt;

    @Column
    private long someLong;

    @Column
    private float someFloat;

    @Column
    private double someDouble;

    @Column
    private boolean someBoolean;

    @Column
    private short someShort;

    @Column
    private Wrapper someWrapper;

    public static class Wrapper {

        private SampleObject sampleObject;

        public Wrapper(SampleObject sampleObject) {
            this.sampleObject = sampleObject;
            if (sampleObject != null) {
                sampleObject.setSomeWrapper(null);
            }
        }

        public void setSampleObject(SampleObject sampleObject) {
            this.sampleObject = sampleObject;
        }

        @Override
        public String toString() {
            return "Wrapper{" +
                    "sampleObject=" + (sampleObject == null ? null : sampleObject.toString()) +
                    '}';
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getSomeDate() {
        return someDate;
    }

    public void setSomeDate(Date someDate) {
        this.someDate = someDate;
    }

    public String getSomeString() {
        return someString;
    }

    public void setSomeString(String someString) {
        this.someString = someString;
    }

    public int getSomeInt() {
        return someInt;
    }

    public void setSomeInt(int someInt) {
        this.someInt = someInt;
    }

    public long getSomeLong() {
        return someLong;
    }

    public void setSomeLong(long someLong) {
        this.someLong = someLong;
    }

    public float getSomeFloat() {
        return someFloat;
    }

    public void setSomeFloat(float someFloat) {
        this.someFloat = someFloat;
    }

    public double getSomeDouble() {
        return someDouble;
    }

    public void setSomeDouble(double someDouble) {
        this.someDouble = someDouble;
    }

    public boolean isSomeBoolean() {
        return someBoolean;
    }

    public void setSomeBoolean(boolean someBoolean) {
        this.someBoolean = someBoolean;
    }

    public short getSomeShort() {
        return someShort;
    }

    public void setSomeShort(short someShort) {
        this.someShort = someShort;
    }

    public Wrapper getSomeWrapper() {
        return someWrapper;
    }

    public void setSomeWrapper(Wrapper someWrapper) {
        this.someWrapper = someWrapper;
    }

    @Override
    public String toString() {
        return "SampleObject{" +
                "id=" + id +
                ", someDate=" + someDate +
                ", someString='" + someString + '\'' +
                ", someInt=" + someInt +
                ", someLong=" + someLong +
                ", someFloat=" + someFloat +
                ", someDouble=" + someDouble +
                ", someBoolean=" + someBoolean +
                ", someShort=" + someShort +
                ", someWrapper=" + someWrapper +
                '}';
    }
}
