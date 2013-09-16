package com.rallat.search.tasks;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.test.ActivityInstrumentationTestCase2;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rallat.search.R;
import com.rallat.search.activities.MainActivity;
import com.rallat.search.views.GoogleImageAdapter.ViewHolder;

public class DownloadImageTaskTests extends ActivityInstrumentationTestCase2<MainActivity> implements AsyncTaskListener {
    private CountDownLatch signal = new CountDownLatch(3);
    private ViewHolder mHolder;
    private MockDownloadImageTask mTest;
    private LayoutInflater mInflater;
    private View mLayout;
    private Bitmap mResult;
    private LruCache<String, Bitmap> mCache;
    private String mUrl = "http://t3.gstatic.com/images?q=tbn:ANd9GcR8I1844pOFj_ZrHWzyo4SsO7lExEN7jG6Ek08oMJQLN0ECMSsDdUd7R08N";

    public DownloadImageTaskTests() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mHolder = new ViewHolder();
        mInflater = LayoutInflater.from(getActivity());
        mLayout = mInflater.inflate(R.layout.image_item, null);
        mHolder.mImageView = (ImageView) mLayout.findViewById(R.id.image);
        mHolder.mTitleView = (TextView) mLayout.findViewById(R.id.title);
        mCache = new LruCache<String, Bitmap>(1);
        mHolder.mImageView.setTag(mUrl);
        mTest = new MockDownloadImageTask(mHolder.mImageView, mCache, this);
    }

    public void testNormalFlow() throws InterruptedException {
        signal = new CountDownLatch(3);
        mTest.execute(new String[] { mUrl });
        signal.await();
        assertEquals(0, signal.getCount());
        assertEquals(mResult, mCache.get(mUrl));
        assertEquals(mResult, ((BitmapDrawable) mHolder.mImageView.getDrawable()).getBitmap());
    }

    public void testCancelledBeforDoInBackground() throws InterruptedException {
        signal = new CountDownLatch(1);
        mTest.execute(new String[] { mUrl });
        signal.await();
        assertEquals(0, signal.getCount());
        assertTrue(mTest.cancel(true));
        assertTrue(mTest.isCancelled());
    }

    public void testCancelledBeforPostExecute() throws InterruptedException {
        signal = new CountDownLatch(2);
        mTest.execute(new String[] { mUrl });
        signal.await();
        assertEquals(0, signal.getCount());
        assertTrue(mTest.cancel(true));
        assertTrue(mTest.isCancelled());
    }

    @Override
    public void onPreExecute() {
        signal.countDown();
    }

    @Override
    public void onPostExecute() {
        signal.countDown();

    }

    @Override
    public void doInBackground() {
        signal.countDown();

    }

    private class MockDownloadImageTask extends DownloadImageTask {

        private AsyncTaskListener mListener;

        public MockDownloadImageTask(ImageView imageView, LruCache<String, Bitmap> imageCache,
                        AsyncTaskListener listener) throws IOException {
            super(imageView, imageCache);
            this.mListener = listener;
        }

        @Override
        protected void onPreExecute() {
            mListener.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            mListener.doInBackground();
            return super.doInBackground(urls);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            mResult = result;
            mListener.onPostExecute();
        }
    }

}
