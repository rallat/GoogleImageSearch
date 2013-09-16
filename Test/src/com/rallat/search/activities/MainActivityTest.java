package com.rallat.search.activities;

import static com.rallat.search.providers.SearchTable.CONTENT_URI;
import static com.rallat.search.providers.SearchTable.PROJECTION;
import android.app.Instrumentation;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.rallat.search.R;
import com.rallat.search.SearchState;
import com.rallat.search.fragments.HistoryFragment;
import com.rallat.search.fragments.SearchFragment;
import com.rallat.search.providers.SearchTable;
import com.rallat.search.views.NavigationDrawerDelegate;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Instrumentation mInstrumentation;
    private MainActivity mActivity;
    private NavigationDrawerDelegate mNavigationDrawer;
    private EditText mEditText;
    private GridView mImageGridView;
    private ListAdapter mAdapter;
    private SearchState mSearchState;
    private ListView mHistoryList;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mInstrumentation = getInstrumentation();
        mActivity = getActivity();
        mEditText = (EditText) mActivity.findViewById(R.id.edit_search);
        mImageGridView = (GridView) mActivity.findViewById(R.id.grid_image);
        mAdapter = mImageGridView.getAdapter();
        mNavigationDrawer = new NavigationDrawerDelegate(mActivity,
                        (DrawerLayout) mActivity.findViewById(R.id.drawer_layout),
                        (ListView) mActivity.findViewById(R.id.drawer_list), mActivity.getResources().getStringArray(
                                        R.array.screen_array));
        mSearchState = SearchState.getInstance();
    }

    @Override
    public void tearDown() throws Exception {
        Uri uri = SearchTable.CONTENT_URI.buildUpon().appendPath("test").build();
        Uri uri2 = SearchTable.CONTENT_URI.buildUpon().appendPath("Android").build();
        mActivity.getContentResolver().delete(uri, null, null);
        mActivity.getContentResolver().delete(uri2, null, null);
        super.tearDown();
    }

    public void testSearchFragmentShowOnCorrectly() {
        assertTrue(mActivity.getFragmentManager().findFragmentByTag(SearchFragment.class.getSimpleName()).isVisible());
        assertEquals(mActivity.getString(R.string.search_hint), mEditText.getHint());
        assertEquals(View.VISIBLE, mEditText.getVisibility());
        assertEquals(EditorInfo.IME_ACTION_SEARCH, mEditText.getImeOptions());
        assertEquals(0, mImageGridView.getAdapter().getCount());

    }

    public void testSearchFragmentShowWithQuery() throws InterruptedException {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mEditText.setText("test");
                mEditText.onEditorAction(EditorInfo.IME_ACTION_SEARCH);
            }
        });
        mInstrumentation.waitForIdleSync();
        assertEquals("test", mEditText.getText().toString());
        assertEquals("test", mSearchState.getCurrentSearch().getQuery());
        Thread.sleep(10000);
        assertEquals(56, mAdapter.getCount());
    }

    public void testSearchFragmentShowWithQueryOnOrientationChange() throws InterruptedException {
        testSearchFragmentShowWithQuery();
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getInstrumentation().waitForIdleSync();
        assertEquals("test", mEditText.getText().toString());
        assertEquals("test", mSearchState.getCurrentSearch().getQuery());
        Thread.sleep(100);
        assertEquals(56, mAdapter.getCount());
    }

    public void testHistoryFragmentShowOnClickNavigationDrawerItem() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mNavigationDrawer.onItemClick(null, null, 1, 1);
            }
        });
        mInstrumentation.waitForIdleSync();
        assertTrue(mActivity.getFragmentManager().findFragmentByTag(HistoryFragment.class.getSimpleName()).isVisible());
    }

    public void testHistoryFragmentWithSearchStored() {
        // adds search to the db
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mEditText.setText("test");
                mEditText.onEditorAction(EditorInfo.IME_ACTION_SEARCH);
                mEditText.setText("Android");
                mEditText.onEditorAction(EditorInfo.IME_ACTION_SEARCH);
                mNavigationDrawer.onItemClick(null, null, 1, 1);
            }
        });
        mInstrumentation.waitForIdleSync();
        assertTrue(mActivity.getFragmentManager().findFragmentByTag(HistoryFragment.class.getSimpleName()).isVisible());
        mHistoryList = (ListView) getActivity().findViewById(R.id.search_list);
        assertEquals(getActivity().getContentResolver().query(CONTENT_URI, PROJECTION, null, null, null).getCount(),
                        mHistoryList.getAdapter().getCount());
    }

    public void testHistoryFragmentOnClickSearchOpenSearchFragment() {
        testHistoryFragmentWithSearchStored();
        mHistoryList = (ListView) getActivity().findViewById(R.id.search_list);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mHistoryList.performItemClick(null, 0, 0);
            }
        });
        mInstrumentation.waitForIdleSync();
        assertTrue(mActivity.getFragmentManager().findFragmentByTag(SearchFragment.class.getSimpleName()).isVisible());
        assertEquals("Android", mEditText.getText().toString());
    }
}
