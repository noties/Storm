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

import junit.framework.TestCase;

import java.util.Arrays;

import ru.noties.storm.query.Selection;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 23.02.2015.
 */
public class SelectionTest extends TestCase {

    public void testEmpty() {
        final Selection selection = Selection.empty();
        assertEquals(0, selection.getSelection().length());
        assertEquals(0, selection.getSelectionArgs().length);
    }

    public void testEquals() {
        final Selection selection = Selection.eq("key", "value");
        assertEquals("key = ?", selection.getSelection());
        assertTrue(Arrays.equals(new String[] {"value"}, selection.getSelectionArgs()));
    }

    public void testNotEquals() {
        final Selection selection = Selection.neq("key", "value");
        assertEquals("key != ?", selection.getSelection());
        assertTrue(Arrays.equals(new String[] {"value"}, selection.getSelectionArgs()));
    }

    public void testBigger() {
        final Selection selection = Selection.b("key", "value");
        assertEquals("key > ?", selection.getSelection());
        assertTrue(Arrays.equals(new String[] {"value"}, selection.getSelectionArgs()));
    }

    public void testLess() {
        final Selection selection = Selection.l("key", "value");
        assertEquals("key < ?", selection.getSelection());
        assertTrue(Arrays.equals(new String[] {"value"}, selection.getSelectionArgs()));
    }

    public void testBiggerEquals() {
        final Selection selection = Selection.be("key", "value");
        assertEquals("key >= ?", selection.getSelection());
        assertTrue(Arrays.equals(new String[] {"value"}, selection.getSelectionArgs()));
    }

    public void testLessEquals() {
        final Selection selection = Selection.le("key", "value");
        assertEquals("key <= ?", selection.getSelection());
        assertTrue(Arrays.equals(new String[] {"value"}, selection.getSelectionArgs()));
    }

    public void testIN() {
        final Selection selection = Selection.in("key", "value1", "value2", "value3");
        assertEquals("key IN (?,?,?)", selection.getSelection());
        assertTrue(Arrays.equals(new String[] {"value1", "value2", "value3"}, selection.getSelectionArgs()));
    }

    public void testBetween() {
        final Selection selection = Selection.btw("key", "value1", "value2");
        assertEquals("key BETWEEN ? AND ?", selection.getSelection());
        assertTrue(Arrays.equals(new String[]{"value1", "value2"}, selection.getSelectionArgs()));
    }

    public void testAnd() {
        final Selection selection = Selection.eq("key1", "value1")
                .and(Selection.neq("key2", "value2"));
        assertEquals("key1 = ? AND key2 != ?", selection.getSelection());
        assertTrue(Arrays.equals(new String[] { "value1", "value2" }, selection.getSelectionArgs()));
    }

    public void testOr() {
        final Selection selection = Selection.eq("key1", "value1")
                .or(Selection.neq("key2", "value2"));
        assertEquals("key1 = ? OR key2 != ?", selection.getSelection());
        assertTrue(Arrays.equals(new String[] { "value1", "value2" }, selection.getSelectionArgs()));
    }

    public void testGroup() {
        final Selection group = Selection.empty();
        group.grp(Selection.eq("key2", "value2").or(Selection.neq("key3", "value3")));
        final Selection selection = Selection.eq("key1", "value1")
                .and(group);
        assertEquals("key1 = ? AND (key2 = ? OR key3 != ?)", selection.getSelection());
        assertTrue(Arrays.equals(new String[] {"value1", "value2", "value3"}, selection.getSelectionArgs()));
    }
}
