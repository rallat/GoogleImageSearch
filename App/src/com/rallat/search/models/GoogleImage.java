package com.rallat.search.models;

import android.os.Parcel;
import android.os.Parcelable;

public class GoogleImage implements Parcelable {
    private String mId;
    private String mTitleFormatted;
    private String mUrl;

    public static final Parcelable.Creator<GoogleImage> CREATOR = new Parcelable.Creator<GoogleImage>() {
        public GoogleImage createFromParcel(Parcel in) {
            return new GoogleImage(in);
        }

        public GoogleImage[] newArray(int size) {
            return new GoogleImage[size];
        }
    };

    public GoogleImage(String id, String title, String url) {
        this.mId = id;
        this.mTitleFormatted = title;
        this.mUrl = url;
    }

    public GoogleImage(Parcel in) {
        readFromParcel(in);
    }

    public String getId() {
        return mId;
    }

    public String getTitleFormatted() {
        return mTitleFormatted;
    }

    public String getUrl() {
        return mUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void readFromParcel(Parcel parcel) {
        mId = parcel.readString();
        mTitleFormatted = parcel.readString();
        mUrl = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(mId);
        parcel.writeString(mTitleFormatted);
        parcel.writeString(mUrl);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((mId == null) ? 0 : mId.hashCode());
        result = (prime * result) + ((mTitleFormatted == null) ? 0 : mTitleFormatted.hashCode());
        result = (prime * result) + ((mUrl == null) ? 0 : mUrl.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        GoogleImage other = (GoogleImage) obj;
        if (mId == null) {
            if (other.mId != null) {
                return false;
            }
        } else if (!mId.equals(other.mId)) {
            return false;
        }
        if (mTitleFormatted == null) {
            if (other.mTitleFormatted != null) {
                return false;
            }
        } else if (!mTitleFormatted.equals(other.mTitleFormatted)) {
            return false;
        }
        if (mUrl == null) {
            if (other.mUrl != null) {
                return false;
            }
        } else if (!mUrl.equals(other.mUrl)) {
            return false;
        }
        return true;
    }
}
