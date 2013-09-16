package com.rallat.search.activities;

import android.test.AndroidTestCase;

import com.rallat.search.Pagination;

public class PaginationTests extends AndroidTestCase {
    private Pagination mTest;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mTest = new Pagination();
    }

    public void testGetNextPage() {
        assertEquals(Integer.valueOf(0), mTest.getStartForNextPage());
        assertEquals(Integer.valueOf(8), mTest.getStartForNextPage());
        assertEquals(Integer.valueOf(16), mTest.getStartForNextPage());
    }

    public void testCountPagesForItemIndex() {
        assertEquals(6, mTest.getCountPagesForItemIndex(50));
        assertEquals(1, mTest.getCountPagesForItemIndex(15));
    }

    public void testCountPagesForItemIndexBoundaryValues() {
        assertEquals(0, mTest.getCountPagesForItemIndex(0));
        assertEquals(0, mTest.getCountPagesForItemIndex(7));
        assertEquals(1, mTest.getCountPagesForItemIndex(8));
    }

    public void testCountPagesForItemIndexNegative() {
        assertEquals(0, mTest.getCountPagesForItemIndex(-10));
    }
}
