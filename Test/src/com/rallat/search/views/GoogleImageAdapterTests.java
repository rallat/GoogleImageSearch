package com.rallat.search.views;

import java.util.ArrayList;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.test.AndroidTestCase;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rallat.search.R;
import com.rallat.search.models.GoogleImage;

public class GoogleImageAdapterTests extends AndroidTestCase implements Callback {

    private ArrayList<GoogleImage> mImages;
    private GoogleImageAdapter mTest;
    private Handler mHandler;
    private GoogleImage mHire;
    private GoogleImage mMe;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        mImages = new ArrayList<GoogleImage>();
        mHire = new GoogleImage("1", "Israel Ferrer", "http://android.es");
        mMe = new GoogleImage("2", "android", "http://android.com");
        mImages.add(mHire);
        mImages.add(mMe);
        mHandler = new Handler();
        mTest = new GoogleImageAdapter(getContext(), mImages, mHandler);
    }

    public void testGetItem() {
        assertEquals(mHire, mTest.getItem(0));
    }

    public void testGetItemId() {
        assertEquals(0, mTest.getItemId(0));
    }

    public void testGetCount() {
        assertEquals(2, mTest.getCount());
    }

    public void testGetView() {
        View view = mTest.getView(0, null, null);

        TextView title = (TextView) view.findViewById(R.id.title);

        ImageView image = (ImageView) view.findViewById(R.id.image);

        assertNotNull(view);
        assertEquals(mHire.getTitleFormatted(), title.getText());
        assertNotNull(image.getDrawable());

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
