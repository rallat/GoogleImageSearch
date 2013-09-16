package com.rallat.search.fragments;

import static com.rallat.search.providers.SearchTable.CONTENT_URI;
import static com.rallat.search.providers.SearchTable.PROJECTION;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.rallat.search.R;
import com.rallat.search.activities.BaseActivity;
import com.rallat.search.providers.SearchCursorDelegate;

public class HistoryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private ListView mList;
    private SimpleCursorAdapter mAdapter;
    private OnItemClickListener itemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> adapter, View nul, int position, long id) {
            if (adapter != null) {
                Cursor cursor = mAdapter.getCursor();
                SearchCursorDelegate search = new SearchCursorDelegate(cursor);
                BaseActivity activity = (BaseActivity) getActivity();
                activity.selectSearchFragmentWithQuery(search.getPosition(position));
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_history, null);
        mList = (ListView) layout.findViewById(R.id.search_list);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1, null, PROJECTION,
                        new int[] { android.R.id.text1 }, 0);
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(itemClickListener);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), CONTENT_URI, PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {
        mAdapter.swapCursor(null);
    }
}
