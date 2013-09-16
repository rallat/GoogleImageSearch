package com.rallat.search;

import android.graphics.Bitmap;
import android.util.LruCache;

public class ImageCache {
    private final LruCache<String, Bitmap> mImageCache;

    static final Object sLock = new Object();
    static ImageCache sInstance;

    private ImageCache() {
        mImageCache = new LruCache<String, Bitmap>(getcacheSize()) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    private int getcacheSize() {
        final int maxMemoryInKb = (int) (Runtime.getRuntime().maxMemory() / 1024);
        return maxMemoryInKb / 6;
    }

    public static ImageCache getInstance() {
        synchronized (sLock) {
            if (sInstance == null) {
                sInstance = new ImageCache();
            }
            return sInstance;
        }
    }

    public LruCache<String, Bitmap> getLruCache() {
        synchronized (sLock) {
            return mImageCache;
        }
    }

}
