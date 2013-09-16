package com.rallat.search.providers;

import java.util.HashMap;

import android.database.Cursor;
import android.util.Log;

public abstract class CursorDelegate<T> {
    private static final String TAG = CursorDelegate.class.getName();
    private final HashMap<String, Integer> mColumnNameToIndexMap;
    protected final Cursor mCursor;

    public CursorDelegate(Cursor cursor) {
        mColumnNameToIndexMap = new HashMap<String, Integer>();
        if (cursor == null) {
            throw new NullPointerException("Cursor cannot be null.");
        } else {
            this.mCursor = cursor;
            return;
        }
    }

    public void close() {
        mCursor.close();
    }

    public int getCount() {
        return mCursor.getCount();
    }

    protected Integer getIndex(String columnName) {
        if (!mColumnNameToIndexMap.containsKey(columnName)) {
            mColumnNameToIndexMap.put(columnName, Integer.valueOf(mCursor.getColumnIndex(columnName)));
        }
        return (mColumnNameToIndexMap.get(columnName).intValue() == -1) ? null : mColumnNameToIndexMap.get(columnName);
    }

    protected Long getLong(String columnName) {
        Integer index = getIndex(columnName);
        Long long1;
        if (index == null) {
            Log.v(TAG, "attempted to retrieve non-existent column: " + columnName);
            long1 = null;
        } else {
            long1 = Long.valueOf(mCursor.getLong(index.intValue()));
        }
        return long1;
    }

    protected abstract T getObject();

    public T getFirst() {
        T obj;
        if (mCursor.moveToFirst()) {
            obj = getObject();
        } else {
            obj = null;
        }
        return obj;
    }

    public T getPosition(int position) {
        T obj;
        if (mCursor.moveToPosition(position)) {
            obj = getObject();
        } else {
            obj = null;
        }
        return obj;
    }

    protected String getString(String columnName) {
        Integer index = getIndex(columnName);
        String s1;
        if (index == null) {
            Log.v(TAG, "attempted to retrieve non-existent column: " + columnName);
            s1 = null;
        } else {
            s1 = mCursor.getString(index.intValue());
        }
        return s1;
    }

}