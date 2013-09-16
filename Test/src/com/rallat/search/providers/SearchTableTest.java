package com.rallat.search.providers;

import android.content.ContentValues;
import android.test.AndroidTestCase;

public class SearchTableTest extends AndroidTestCase {
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    public void testGetContentValues() {
        ContentValues values = SearchTable.getContentValues("search1");
        assertEquals("search1", values.get(SearchTable.QUERY));
    }

    public void testGetContentValuesWithNull() {
        ContentValues values = SearchTable.getContentValues(null);
        assertEquals(null, values.get(SearchTable.QUERY));
    }

    public void testGetContentValuesWithEmptyString() {
        ContentValues values = SearchTable.getContentValues("");
        assertEquals("", values.get(SearchTable.QUERY));
    }
}
