package com.rallat.search.tasks;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.test.AndroidTestCase;

public class ApiHttpUrlConnectionTests extends AndroidTestCase {
    private ApiHttpUrlConnection mTest;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mTest = new ApiHttpUrlConnection(new String[] { "android", "0" });
    }

    public void testGetUrl() throws MalformedURLException {
        assertEquals(new URL("http://ajax.googleapis.com/ajax/services/search/images?v=1.0&start=0&rsz=8&q=android"),
                        mTest.getHttpURLConnection().getURL());
    }

    public void testGetUrlNoParams() throws IOException {
        mTest = new ApiHttpUrlConnection(null);
        assertEquals(new URL("http://ajax.googleapis.com/ajax/services/search/images?v=1.0"), mTest
                        .getHttpURLConnection().getURL());
    }
}
