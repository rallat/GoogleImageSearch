package com.rallat.search.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class SearchProvider extends ContentProvider {

    private static final UriMatcher URI_MATCHER;

    private SearchDBHelper mDatabaseHelper;

    private static final String DATABASE_NAME = "search.db";
    private static final int SCHEMA_VERSION = 1;

    private static final int SEARCHS = 1;
    private static final int SEARCH_QUERY = 2;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(SearchTable.AUTHORITY, "searchs", SEARCHS);
        URI_MATCHER.addURI(SearchTable.AUTHORITY, "searchs/*", SEARCH_QUERY);
    }

    @Override
    public int delete(Uri uri, String whereClause, String[] whereArgs) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        switch (URI_MATCHER.match(uri)) {
        case SEARCHS:
            return db.delete(SearchTable.TABLE_NAME, whereClause, whereArgs);
        case SEARCH_QUERY:
            String searchQuery = uri.getPathSegments().get(1);
            String where = SearchTable.QUERY + "='" + searchQuery + "'"
                            + (!TextUtils.isEmpty(whereClause) ? " AND (" + whereClause + ")" : "");
            return db.delete(SearchTable.TABLE_NAME, where, whereArgs);
        default:
            throw new IllegalArgumentException("Unsupported: " + uri);
        }
    }

    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
        case SEARCHS:
            return SearchTable.CONTENT_TYPE;
        default:
            throw new IllegalArgumentException("Unsupported: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if ((URI_MATCHER.match(uri) != SEARCHS) && (values != null)) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

        long rowId = db.insert(SearchTable.TABLE_NAME, SearchTable.QUERY, values);
        if (rowId > 0) {
            Uri searchUri = ContentUris.withAppendedId(SearchTable.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(searchUri, null);
            return searchUri;
        }

        throw new SQLException("Failed to insert row: " + uri);

    }

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new SearchDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(SearchTable.TABLE_NAME);

        switch (URI_MATCHER.match(uri)) {
        case SEARCHS:
            break;

        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        Cursor cursor = builder.query(mDatabaseHelper.getReadableDatabase(), projection, selection, selectionArgs,
                        null, null, getsortOrder(sortOrder));
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    private String getsortOrder(String sortOrder) {
        return TextUtils.isEmpty(sortOrder) ? SearchTable.DEFAULT_SORT_ORDER : sortOrder;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    private static class SearchDBHelper extends SQLiteOpenHelper {
        public SearchDBHelper(Context context) {
            super(context, DATABASE_NAME, null, SCHEMA_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            SearchTable.onCreate(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

}
