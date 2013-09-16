package com.rallat.search.views;

import android.app.Instrumentation;
import android.support.v4.widget.DrawerLayout;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.rallat.search.R;
import com.rallat.search.activities.MainActivity;

public class NavigationDrawerDelegateTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public NavigationDrawerDelegateTest() {
        super(MainActivity.class);
    }

    private String[] mScreenList;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private NavigationDrawerDelegate mTest;
    private MainActivity mActivity;
    private Instrumentation mInstrumentation;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mInstrumentation = getInstrumentation();

        mActivity = getActivity();

        mScreenList = new String[] { "Search", "History" };
        mDrawerLayout = (DrawerLayout) mActivity.findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) mActivity.findViewById(R.id.drawer_list);
        mTest = new NavigationDrawerDelegate(getActivity(), mDrawerLayout, mDrawerList, mScreenList);
        mActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mTest.onCreate();
            }
        });
        mInstrumentation.waitForIdleSync();
    }

    public void testOnCreate() {
        ListAdapter adapter = mDrawerList.getAdapter();
        assertEquals(mScreenList.length, adapter.getCount());
        for (int i = 0; i < adapter.getCount(); i++) {
            assertEquals(mScreenList[i], adapter.getItem(i));
        }
        assertEquals(-1, mDrawerList.getCheckedItemPosition());
    }

    public void testUpdateSelectionReturnsCorrectItemPosition() {
        mActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mTest.updateDrawerSelection(0);
            }
        });
        mInstrumentation.waitForIdleSync();
        assertEquals(0, mTest.getSelectedItemPosition());
        assertEquals(mScreenList[0], getActivity().getTitle());
        assertEquals(true, mDrawerLayout.isShown());
    }

    public void testUpdateSelectionIncorrectPosition() {
        mActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mTest.updateDrawerSelection(0);
                mTest.updateDrawerSelection(-1);
                mTest.updateDrawerSelection(2);
            }

        });
        mInstrumentation.waitForIdleSync();
        assertEquals(0, mTest.getSelectedItemPosition());
        assertEquals(mScreenList[0], getActivity().getTitle());
        assertEquals(true, mDrawerLayout.isShown());
    }
}
