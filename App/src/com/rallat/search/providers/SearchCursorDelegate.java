package com.rallat.search.providers;

import android.database.Cursor;

import com.rallat.search.models.Search;

public class SearchCursorDelegate extends CursorDelegate<Search> {

    public SearchCursorDelegate(Cursor cursor) {
        super(cursor);
    }

    @Override
    protected Search getObject() {
        return new Search(getLong(SearchTable._ID), getString(SearchTable.QUERY), getLong(SearchTable.TIMESTAMP));
    }

}
