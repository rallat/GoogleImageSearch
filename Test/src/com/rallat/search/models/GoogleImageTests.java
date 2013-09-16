package com.rallat.search.models;

import android.os.Parcel;
import android.test.AndroidTestCase;

public class GoogleImageTests extends AndroidTestCase {

    private GoogleImage mTest;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mTest = new GoogleImage("1", "Hire me!", "http://android.es");
    }

    public void testParcelable() {
        Parcel parcel = Parcel.obtain();
        mTest.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        GoogleImage emptyImage = new GoogleImage(null, null, null);
        emptyImage.readFromParcel(parcel);
        assertEquals(mTest, emptyImage);
    }
}
