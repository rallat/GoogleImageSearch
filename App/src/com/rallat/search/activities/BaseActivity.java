package com.rallat.search.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import com.rallat.search.R;
import com.rallat.search.fragments.HistoryFragment;
import com.rallat.search.fragments.SearchFragment;
import com.rallat.search.models.Search;
import com.rallat.search.views.NavigationDrawerDelegate;

public class BaseActivity extends Activity {

    private Fragment[] mFragments = new Fragment[] { new SearchFragment(), new HistoryFragment() };

    private int mSelectedFragment = -1;
    private NavigationDrawerDelegate mNavigationDrawer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNavigationDrawer = new NavigationDrawerDelegate(this, (DrawerLayout) findViewById(R.id.drawer_layout),
                        (ListView) findViewById(R.id.drawer_list), getResources().getStringArray(R.array.screen_array));
        mNavigationDrawer.onCreate();
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        } else {
            selectFragment(mNavigationDrawer.getSelectedItemPosition() == -1 ? 0 : mNavigationDrawer
                            .getSelectedItemPosition());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mNavigationDrawer.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selection", mSelectedFragment);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mSelectedFragment = savedInstanceState.getInt("selection");
        mNavigationDrawer.updateDrawerSelection(mSelectedFragment);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mNavigationDrawer.onPostCreate();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mNavigationDrawer.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void selectFragment(int position) {
        if (mSelectedFragment != position) {
            mSelectedFragment = position;
            Fragment fragment = mFragments[position];
            transactionToFragment(fragment);
        }
        mNavigationDrawer.updateDrawerSelection(mSelectedFragment);
    }

    public void selectSearchFragmentWithQuery(Search search) {
        mSelectedFragment = 0;
        Fragment fragment = new SearchFragment(search);
        transactionToFragment(fragment);
        mNavigationDrawer.updateDrawerSelection(mSelectedFragment);
    }

    private void transactionToFragment(Fragment fragment) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_content, fragment, fragment.getClass().getSimpleName());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    public void closeKeyBoard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return (activeNetworkInfo != null) && activeNetworkInfo.isConnected();
    }

}
