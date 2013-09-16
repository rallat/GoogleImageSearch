package com.rallat.search.providers;

import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;

import com.rallat.search.models.Search;

public class SearchProviderTests extends ProviderTestCase2<SearchProvider> {

    private SearchProvider mTest;
    private static final String[] PROJECTION = new String[] { SearchTable._ID, SearchTable.QUERY, SearchTable.TIMESTAMP };

    public SearchProviderTests() {
        super(SearchProvider.class, SearchTable.AUTHORITY);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mTest = getProvider();
        mTest.insert(SearchTable.CONTENT_URI, SearchTable.getContentValues("search1"));
    }

    public void testInsertSearch() {
        assertEquals(mTest.query(SearchTable.CONTENT_URI, PROJECTION, null, null, null).getCount(), 1);
    }

    public void testSelectAllSearches() {
        Cursor cursor = mTest.query(SearchTable.CONTENT_URI, PROJECTION, null, null, null);
        SearchCursorDelegate delegate = new SearchCursorDelegate(cursor);
        Search search = delegate.getFirst();
        assertEquals("search1", search.getQuery());
    }

    public void testDeleteOneSearch() {
        Uri uri = SearchTable.CONTENT_URI.buildUpon().appendPath("search1").build();
        assertEquals(1, mTest.delete(uri, null, null));
    }

    public void testDeleteAllSearches() {
        mTest.insert(SearchTable.CONTENT_URI, SearchTable.getContentValues("search2"));
        assertEquals(2, mTest.delete(SearchTable.CONTENT_URI, null, null));
    }
}
