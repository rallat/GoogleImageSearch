package com.rallat.search.providers;

import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import com.rallat.search.models.Search;

public class SearchCursorDelegateTests extends ProviderTestCase2<SearchProvider> {
    private SearchProvider mProvider;
    private MockContentResolver mResolver;
    private SearchCursorDelegate mTest;
    private static final String[] PROJECTION = new String[] { SearchTable._ID, SearchTable.QUERY, SearchTable.TIMESTAMP };

    public SearchCursorDelegateTests() {
        super(SearchProvider.class, SearchTable.AUTHORITY);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mProvider = getProvider();
        mResolver = new MockContentResolver();
        mResolver.addProvider(SearchTable.CONTENT_URI.toString(), mProvider);
        mProvider.insert(SearchTable.CONTENT_URI, SearchTable.getContentValues("search1"));
        mProvider.insert(SearchTable.CONTENT_URI, SearchTable.getContentValues("search2"));
        mProvider.insert(SearchTable.CONTENT_URI, SearchTable.getContentValues("search3"));
        mTest = new SearchCursorDelegate(mProvider.query(SearchTable.CONTENT_URI, PROJECTION, null, null, null));
    }

    public void testgetFirst() {
        Search search = mTest.getFirst();
        assertEquals("search1", search.getQuery());
    }

    public void testgetLast() {
        Search search = mTest.getPosition(mTest.getCount() - 1);
        assertEquals("search3", search.getQuery());
    }

}
