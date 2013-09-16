package com.rallat.search.tasks;

import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.ImageView;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    private ImageView mView;
    private String mURL;
    private LruCache<String, Bitmap> mImageCache;

    public DownloadImageTask(ImageView imageView, LruCache<String, Bitmap> imageCache) {
        this.mView = imageView;
        this.mImageCache = imageCache;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        try {
            mURL = urls[0];
            if ((urls != null) && (urls.length > 0)) {
                URL url = new URL(urls[0]);
                return BitmapFactory.decodeStream(url.openStream());
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        if ((result != null) && (mURL != null)) {
            mImageCache.put(mURL, result);
            if (mURL.equals(mView.getTag())) {
                mView.setImageBitmap(result);
            }
        }
    }

}
