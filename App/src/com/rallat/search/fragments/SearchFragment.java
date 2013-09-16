package com.rallat.search.fragments;

import static com.rallat.search.providers.SearchTable.CONTENT_URI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.rallat.search.Pagination;
import com.rallat.search.R;
import com.rallat.search.SearchState;
import com.rallat.search.activities.BaseActivity;
import com.rallat.search.models.GoogleImage;
import com.rallat.search.models.Search;
import com.rallat.search.providers.SearchTable;
import com.rallat.search.tasks.ApiHttpUrlConnection;
import com.rallat.search.tasks.GoogleImageSearchTask;
import com.rallat.search.views.GoogleImageAdapter;

public class SearchFragment extends Fragment implements Callback, OnScrollListener {
    private static final String TAG = SearchFragment.class.getName();
    private static final String PAGE_KEY = "page";
    private static final String QUERY_KEY = "query";

    public static final String FOOTER_KEY = "show_footer";
    // request minimum 50 elements
    public static final int MIN_ITEMS = 50;
    public static final String RESULT_KEY = "result";

    private static Handler mHandler;

    private GridView mImageGridView;
    private EditText mSearchInput;
    private SearchState mSearchState;
    private ArrayList<GoogleImage> mImageList;
    private ArrayAdapter<GoogleImage> mAdapter;
    private boolean isHistorySearch;
    private ImageView mFooter;
    private Animation mAnimation;
    private ImageView mLoading;
    private ExecutorService mExecutor;

    private OnEditorActionListener actionSearchListener = new OnEditorActionListener() {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (isSearchEvent(actionId, event) && !TextUtils.isEmpty(v.getText())) {
                try {
                    mImageList.clear();
                    mAdapter.notifyDataSetChanged();
                    mSearchState.reset();
                    String query = v.getText().toString();
                    mSearchState.setCurrentSearch(new Search(-1, query, -1));
                    if (getBaseActivity().isNetworkAvailable()) {
                        downloadImagePagesFor(query);
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    }
                    getActivity().getContentResolver().insert(CONTENT_URI, SearchTable.getContentValues(query));
                } catch (IllegalArgumentException e) {
                    Log.e(TAG, "Insert Error ", e);
                }
            }
            return true;
        }

        private boolean isSearchEvent(int actionId, KeyEvent event) {
            return (actionId == EditorInfo.IME_ACTION_SEARCH) || isEnterEvent(event);
        }

        private boolean isEnterEvent(KeyEvent event) {
            return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_UP);
        }
    };

    private void downloadImagePagesFor(String query) {
        getBaseActivity().closeKeyBoard(mSearchInput);
        mLoading.setVisibility(View.VISIBLE);
        mLoading.startAnimation(mAnimation);
        int count = mSearchState.getPagination().getCountPagesForItemIndex(MIN_ITEMS);
        for (int i = 0; i <= count; i++) {
            String[] params = new String[] { query, mSearchState.getPagination().getStartForNextPage().toString() };
            getMoreResults(params);
        }
    }

    private void getMoreResults(String[] params) {
        try {
            GoogleImageSearchTask searchTask = new GoogleImageSearchTask(mHandler, new ApiHttpUrlConnection(params));
            searchTask.executeOnExecutor(mExecutor);
        } catch (IOException e) {
            mSearchState.setHasError(true);
            Log.e(TAG, "Error on API Request", e);
        }
    }

    private BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    public SearchFragment() {
        setSearchState();
        this.isHistorySearch = false;
    }

    public SearchFragment(Search search) {
        setSearchState();
        this.mSearchState.setCurrentSearch(search);
        this.isHistorySearch = true;
    }

    private void setSearchState() {
        this.mSearchState = SearchState.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_search, null);
        setUpFragment(layout);
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
        mExecutor = Executors.newSingleThreadExecutor();
        mAdapter = new GoogleImageAdapter(getActivity(), mImageList, mHandler);
        mImageGridView.setAdapter(mAdapter);
        mImageGridView.setOnScrollListener(this);
        Search currentSearch = mSearchState.getCurrentSearch();
        if (currentSearch.getQuery() != null) {
            mSearchInput.setText(currentSearch.getQuery());
            if (isHistorySearch) {
                isHistorySearch = false;
                downloadImagePagesFor(currentSearch.getQuery());
            }
        }
        return layout;
    }

    private void setUpFragment(View layout) {
        mSearchInput = (EditText) layout.findViewById(R.id.edit_search);
        mImageGridView = (GridView) layout.findViewById(R.id.grid_image);
        mFooter = (ImageView) layout.findViewById(R.id.footer_image);
        mSearchInput.setOnEditorActionListener(actionSearchListener);
        mLoading = (ImageView) layout.findViewById(R.id.loading_image);
        mImageList = new ArrayList<GoogleImage>();
        mHandler = new Handler(this);
        mAnimation = new RotateAnimation(0.0F, 360F, 1, 0.5F, 1, 0.5F);
        mAnimation.setDuration(700L);
        mAnimation.setRepeatCount(Animation.INFINITE);
        setSearchState();
    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {
        String query = savedInstanceState.getString(QUERY_KEY);
        updateSearchState(savedInstanceState, query);
        mImageList = savedInstanceState.getParcelableArrayList(RESULT_KEY);
    }

    private void updateSearchState(Bundle savedInstanceState, String query) {
        mSearchState.setCurrentSearch(new Search(-1, query == null ? "" : query, -1));
        mSearchState.setPagination(new Pagination(savedInstanceState.getInt(PAGE_KEY)));
        mSearchState.setHasError(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if ((mSearchInput != null) && !TextUtils.isEmpty(mSearchInput.getText())) {
            outState.putString(QUERY_KEY, mSearchInput.getText().toString());
        }
        outState.putInt(PAGE_KEY, mSearchState.getPagination().getCurrentPage());
        outState.putParcelableArrayList(RESULT_KEY, mImageList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.mSearchState.reset();
        this.mSearchState = null;
        this.mImageGridView.setAdapter(null);
        this.mAdapter = null;
        this.mExecutor.shutdownNow();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public boolean handleMessage(Message msg) {
        Log.d(TAG, "Handle msg");
        ArrayList<GoogleImage> resultList = msg.getData().getParcelableArrayList(RESULT_KEY);
        if (resultList != null) {
            mImageList.addAll(resultList);
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
        }
        setFooterVisibility(View.INVISIBLE);
        mLoading.setVisibility(View.GONE);
        mLoading.clearAnimation();
        return true;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if ((totalItemCount > MIN_ITEMS) && ((firstVisibleItem + visibleItemCount) == (totalItemCount))
                        && (totalItemCount < 63) && !mSearchState.isHasError()) {
            String[] params = new String[] { mSearchState.getCurrentSearch().getQuery(),
                            mSearchState.getPagination().getStartForNextPage().toString() };
            setFooterVisibility(View.VISIBLE);
            getMoreResults(params);
        }

    }

    private void setFooterVisibility(int visibility) {
        if (visibility == View.VISIBLE) {
            mFooter.setVisibility(View.VISIBLE);
            mFooter.startAnimation(mAnimation);
        } else {
            mFooter.setVisibility(View.GONE);
            mFooter.clearAnimation();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

}
