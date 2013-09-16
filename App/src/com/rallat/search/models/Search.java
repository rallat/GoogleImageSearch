package com.rallat.search.models;

public class Search {
    private long mId;
    private String mQuery;
    private long mTimestamp;

    public Search(long id, String query, long timestamp) {
        this.mId = id;
        this.mQuery = query;
        this.mTimestamp = timestamp;
    }

    public long getId() {
        return mId;
    }

    public String getQuery() {
        return mQuery;
    }

    public long getTimestamp() {
        return mTimestamp;
    }

    public void reset() {
        mQuery = "";
        mId = -1;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + (int) (mId ^ (mId >>> 32));
        result = (prime * result) + ((mQuery == null) ? 0 : mQuery.hashCode());
        result = (prime * result) + (int) (mTimestamp ^ (mTimestamp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Search other = (Search) obj;
        if (mId != other.mId) {
            return false;
        }
        if (mQuery == null) {
            if (other.mQuery != null) {
                return false;
            }
        } else if (!mQuery.equals(other.mQuery)) {
            return false;
        }
        if (mTimestamp != other.mTimestamp) {
            return false;
        }
        return true;
    }

}
