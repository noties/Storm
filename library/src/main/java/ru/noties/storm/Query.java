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

import android.os.Parcel;
import android.os.Parcelable;

import ru.noties.storm.query.Selection;
import ru.noties.storm.query.Sorting;

/**
 * Simple wrapper around SQLite query.
 * Implements {@link android.os.Parcelable} interface so could
 * be easily stored in a {@link android.os.Bundle}
 *
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) 01.02.15.
 */
public class Query implements Parcelable {

    private String      tableName;
    private Class<?>    tableClass;

    private String[]    columns;
    private String      selection;
    private String[]    selectionArgs;
    private String      groupBy;
    private String      having;
    private String      orderBy;
    private String      limit;

    public Query(String tableName) {
        this.tableName  = tableName;
    }

    public Query(Class<?> tableClazz) {
        this.tableClass = tableClazz;
    }

    public Query(Query source) {
        this.tableName      = source.tableName;
        this.tableClass     = source.tableClass;
        this.columns        = source.columns;
        this.selection      = source.selection;
        this.selectionArgs  = source.selectionArgs;
        this.groupBy        = source.groupBy;
        this.having         = source.having;
        this.orderBy        = source.orderBy;
        this.limit          = source.limit;
    }

    public Query columns(String... columns) {
        this.columns = columns;
        return this;
    }

    public Query selection(Selection selection) {
        this.selection      = selection.getSelection();
        this.selectionArgs  = selection.getSelectionArgs();
        return this;
    }

    public Query selection(String selection) {
        this.selection = selection;
        return this;
    }

    public Query selectionArgs(String... selectionArgs) {
        this.selectionArgs = selectionArgs;
        return this;
    }

    public Query groupBy(String groupBy) {
        this.groupBy = groupBy;
        return this;
    }

    public Query having(String having) {
        this.having = having;
        return this;
    }

    public Query orderBy(String orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    public Query orderBy(String column, Sorting sort) {
        this.orderBy = column + " " + sort.getValue();
        return this;
    }

    public Query limit(int limit) {
        this.limit = String.valueOf(limit);
        return this;
    }

    public String getTableName() {
        return tableName;
    }

    public String[] getColumns() {
        return columns;
    }

    public String getSelection() {
        return selection;
    }

    public String[] getSelectionArgs() {
        return selectionArgs;
    }

    public String getGroupBy() {
        return groupBy;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public String getHaving() {
        return having;
    }

    public String getLimit() {
        return limit;
    }

    public Class<?> getTableClass() {
        return tableClass;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tableName);
        dest.writeSerializable(this.tableClass);
        dest.writeStringArray(this.columns);
        dest.writeString(this.selection);
        dest.writeStringArray(this.selectionArgs);
        dest.writeString(this.groupBy);
        dest.writeString(this.having);
        dest.writeString(this.orderBy);
        dest.writeString(this.limit);
    }

    private Query(Parcel in) {
        this.tableName = in.readString();
        this.tableClass = (Class<?>) in.readSerializable();
        this.columns = in.createStringArray();
        this.selection = in.readString();
        this.selectionArgs = in.createStringArray();
        this.groupBy = in.readString();
        this.having = in.readString();
        this.orderBy = in.readString();
        this.limit = in.readString();
    }

    public static final Parcelable.Creator<Query> CREATOR = new Parcelable.Creator<Query>() {
        public Query createFromParcel(Parcel source) {
            return new Query(source);
        }

        public Query[] newArray(int size) {
            return new Query[size];
        }
    };
}
