package com.rallat.search.views;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rallat.search.ImageCache;
import com.rallat.search.R;
import com.rallat.search.models.GoogleImage;
import com.rallat.search.tasks.DownloadImageTask;

public class GoogleImageAdapter extends ArrayAdapter<GoogleImage> {

    private static final String TAG = GoogleImageAdapter.class.getName();

    private LruCache<String, Bitmap> mImageCache;

    public GoogleImageAdapter(Context context, ArrayList<GoogleImage> images, Handler handler) {
        super(context, R.layout.image_item, R.id.title, images);

        this.mImageCache = ImageCache.getInstance().getLruCache();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        GoogleImage currentImage = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.image_item, null);
            holder = new ViewHolder();
            holder.mTitleView = (TextView) convertView.findViewById(R.id.title);
            holder.mImageView = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mTitleView.setText(currentImage.getTitleFormatted());
        holder.mImageView.setTag(currentImage.getUrl());
        holder.mImageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.placeholder));
        Bitmap image = mImageCache.get(currentImage.getUrl());
        if (image != null) {
            holder.mImageView.setImageBitmap(image);
        } else {
            new DownloadImageTask(holder.mImageView, mImageCache).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                            currentImage.getUrl());
        }
        return convertView;
    }

    public static class ViewHolder {
        public ImageView mImageView;
        public TextView mTitleView;
    }

}
