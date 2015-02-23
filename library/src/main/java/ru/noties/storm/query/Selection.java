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

package ru.noties.storm.query;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 30.01.2015.
 */
public class Selection {

    public static final String EQ   = " = ";
    public static final String NEQ  = " != ";
    public static final String B    = " > ";
    public static final String L    = " < ";
    public static final String BE   = " >= ";
    public static final String LE   = " <= ";
    public static final String IN   = " IN ";
    public static final String BTW  = " BETWEEN ";

    public static final String AND  = " AND ";
    public static final String OR   = " OR ";

    protected static final char SO = '(';
    protected static final char SC = ')';
    protected static final char SPACE = ' ';
    protected static final char HOLDER = '?';
    protected static final char COMMA = ',';

    public static Selection empty() {
        return new Selection();
    }

    public static Selection eq(String key, Object value) {
        return new Selection(key, EQ, String.valueOf(value));
    }

    public static Selection neq(String key, Object value) {
        return new Selection(key, NEQ, String.valueOf(value));
    }

    public static Selection in(String key, Object... values) {
        final StringBuilder builder = new StringBuilder();
        final List<String> args = new ArrayList<>();
        builder.append(key)
                .append(IN)
                .append(SO);
        for (int i = 0, size = values.length; i < size; i++) {
            builder.append(HOLDER);
            args.add(i, String.valueOf(values[i]));
            if (i != size -1) {
                builder.append(COMMA);
            }
        }
        builder.append(SC);
        return new Selection(builder, args);
    }

    public static Selection btw(String key, Object first, Object second) {
        final StringBuilder builder = new StringBuilder();
        final List<String> args = new ArrayList<>();
        builder.append(key)
                .append(BTW)
                .append(SO)
                .append(HOLDER)
                .append(COMMA)
                .append(HOLDER)
                .append(SC);
        args.add(0, String.valueOf(first));
        args.add(1, String.valueOf(second));
        return new Selection(builder, args);
    }

    public static Selection b(String key, Object value) {
        return new Selection(key, B, String.valueOf(value));
    }

    public static Selection be(String key, Object value) {
        return new Selection(key, BE, String.valueOf(value));
    }

    public static Selection l(String key, Object value) {
        return new Selection(key, L, String.valueOf(value));
    }

    public static Selection le(String key, Object value) {
        return new Selection(key, LE, String.valueOf(value));
    }

    private final StringBuilder builder;
    private final List<String> args;

    protected Selection() {
        this(new StringBuilder(), new ArrayList<String>());
    }

    protected Selection(StringBuilder builder, List<String> args) {
        this.builder = builder;
        this.args = args;
    }

    protected Selection(String key, String operand, String value) {
        this();
        add(key, operand, value);
    }

    protected void add(String key, String operand, String value) {
        builder.append(key)
                .append(operand)
                .append(HOLDER);
        args.add(value);
    }

    protected void add(Selection selection, @Nullable String what) {
        if (what != null) {
            builder.append(what);
        }

        builder.append(selection.builder);
        args.addAll(selection.args);
    }

    public Selection and(Selection selection) {
        add(selection, AND);
        return this;
    }

    public Selection or(Selection selection) {
        add(selection, OR);
        return this;
    }

    public Selection grp(Selection selection) {
        builder.append(SO);
        add(selection, null);
        builder.append(SC);
        return this;
    }

    public String getSelection() {
        return builder.toString();
    }

    public String[] getSelectionArgs() {
        final String[] array = new String[args.size()];
        args.toArray(array);
        return array;
    }

    @Override
    public String toString() {
        return "Selection(" +
                "selection: `" + builder +
                "`, args=" + Arrays.toString(getSelectionArgs()) +
                ")@" + hashCode();
    }
}
