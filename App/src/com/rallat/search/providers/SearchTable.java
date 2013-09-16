package com.rallat.search.providers;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

public class SearchTable implements BaseColumns {
    public static final String TABLE_NAME = "searchs";
    public static final String QUERY = "query";
    public static final String TIMESTAMP = "timestamp";

    public static final String[] PROJECTION = new String[] { QUERY, _ID, TIMESTAMP };

    public static final String AUTHORITY = "com.google.image.search.provider.Searchs";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/searchs");

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.images.search";

    public static final String DEFAULT_SORT_ORDER = TIMESTAMP + " ASC";

    private SearchTable() {
    }

    public static void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY   , " + QUERY
                            + " TEXT NOT NULL, " + TIMESTAMP + " INTEGER NOT NULL);");
        } catch (SQLException e) {
            Log.e("Can't create Table", e.getMessage());
        }

    }

    public static ContentValues getContentValues(String text) {
        ContentValues values = new ContentValues();
        values.put(QUERY, text);
        values.put(TIMESTAMP, System.currentTimeMillis());
        return values;
    }

}
