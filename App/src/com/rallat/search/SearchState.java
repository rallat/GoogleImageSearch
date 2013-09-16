package com.rallat.search;

import com.rallat.search.models.Search;

public class SearchState {
    private Search mCurrentSearch;
    private Pagination mPagination;
    private boolean hasError;

    static final Object sLock = new Object();
    static SearchState sInstance;

    protected SearchState() {
        mCurrentSearch = new Search(-1, "", 0);
        mPagination = new Pagination();
    }

    public static SearchState getInstance() {
        synchronized (sLock) {
            if (sInstance == null) {
                sInstance = new SearchState();
            }
            return sInstance;
        }
    }

    public Pagination getPagination() {
        synchronized (sLock) {
            return mPagination;
        }
    }

    public void setPagination(Pagination mPagination) {
        synchronized (sLock) {
            this.mPagination = mPagination;
        }
    }

    public Search getCurrentSearch() {
        synchronized (sLock) {
            return mCurrentSearch;
        }
    }

    public void setCurrentSearch(Search mCurrentSearch) {
        synchronized (sLock) {
            this.mCurrentSearch = mCurrentSearch;
        }
    }

    public void reset() {
        mPagination.reset();
        mCurrentSearch.reset();
        hasError = false;
    }

    public boolean isHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }
}
