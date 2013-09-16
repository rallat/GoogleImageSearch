package com.rallat.search.tasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import android.os.Handler;
import android.test.ActivityInstrumentationTestCase2;

import com.rallat.search.activities.MainActivity;
import com.rallat.search.models.GoogleImage;

public class GoogleImageSearchTaskTest extends ActivityInstrumentationTestCase2<MainActivity> implements
                AsyncTaskListener {

    public GoogleImageSearchTaskTest() {
        super(MainActivity.class);
    }

    private CountDownLatch signal = new CountDownLatch(2);
    private MockGoogleImageSearchTask mTest;
    private ArrayList<GoogleImage> resultList;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mTest = new MockGoogleImageSearchTask(null, this);
    }

    public void testNormalFlow() throws InterruptedException {
        signal = new CountDownLatch(3);
        mTest.execute();
        signal.await();
        assertEquals(0, signal.getCount());
        assertEquals(8, resultList.size());
    }

    public void testCancelledBeforDoInBackground() throws InterruptedException {
        signal = new CountDownLatch(1);
        mTest.execute();
        signal.await();
        assertEquals(0, signal.getCount());
        assertTrue(mTest.cancel(true));
        assertTrue(mTest.isCancelled());
    }

    public void testCancelledBeforPostExecute() throws InterruptedException {
        signal = new CountDownLatch(2);
        mTest.execute();
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

    private class MockGoogleImageSearchTask extends GoogleImageSearchTask {
        private final AsyncTaskListener mListener;

        public MockGoogleImageSearchTask(Handler handler, AsyncTaskListener listener) throws IOException {
            super(handler, new ApiHttpUrlConnection(new String[] { "android", "0" }));
            this.mListener = listener;
        }

        @Override
        protected void onPreExecute() {
            mListener.onPreExecute();
        }

        @Override
        protected ArrayList<GoogleImage> doInBackground(Void... params) {
            mListener.doInBackground();
            return super.doInBackground(params);
        }

        @Override
        protected void onPostExecute(ArrayList<GoogleImage> images) {
            super.onPostExecute(images);
            resultList = images;
            mListener.onPostExecute();
        }

    }

}
