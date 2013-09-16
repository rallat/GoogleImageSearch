package com.rallat.search;

public class Pagination {
    public static final Integer ITEMS_PAGE = 8;
    private int mCurrentPage = -1;

    public Pagination() {
    }

    public Pagination(int initialPage) {
        this.mCurrentPage = initialPage;
    }

    public Integer getStartForNextPage() {
        mCurrentPage++;
        return mCurrentPage * ITEMS_PAGE;
    }

    public void reset() {
        mCurrentPage = -1;
    }

    public int getCountPagesForItemIndex(int index) {
        if (index < 0) {
            return 0;
        }
        return ((index + ITEMS_PAGE) / ITEMS_PAGE) - 1;
    }

    public int getCurrentPage() {
        return mCurrentPage;
    }
}
