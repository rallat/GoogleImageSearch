package com.rallat.search.views;

import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.rallat.search.R;
import com.rallat.search.activities.BaseActivity;

public class NavigationDrawerDelegate implements ListView.OnItemClickListener {
    private final DrawerLayout mDrawerLayout;
    private final ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private final String[] mScreenList;
    private BaseActivity mContext;

    public NavigationDrawerDelegate(BaseActivity activity, DrawerLayout layout, ListView list, String[] screenList) {
        this.mDrawerLayout = layout;
        this.mDrawerList = list;
        this.mContext = activity;
        this.mScreenList = screenList;
    }

    public void onCreate() {
        mDrawerList.setAdapter(new ArrayAdapter<String>(mContext, R.layout.drawer_list_item, mScreenList));
        mDrawerToggle = new ActionBarDrawerToggle(mContext, mDrawerLayout, R.drawable.ic_navigation_drawer,
                        R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View view) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }
        };
        mDrawerList.setOnItemClickListener(this);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    public void onDestroy() {
        mDrawerList.setAdapter(null);
        mDrawerToggle = null;
        mContext = null;
    }

    public int getSelectedItemPosition() {
        return mDrawerList.getSelectedItemPosition();
    }

    public void updateDrawerSelection(int position) {
        if ((position > -1) && (position < mDrawerList.getCount())) {
            mDrawerList.setItemChecked(position, true);
            mDrawerLayout.closeDrawer(mDrawerList);
            mContext.setTitle(mScreenList[position]);
        }
    }

    public void onPostCreate() {
        mDrawerToggle.syncState();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mContext.selectFragment(position);
    }
}
