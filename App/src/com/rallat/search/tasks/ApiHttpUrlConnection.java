package com.rallat.search.tasks;

import static com.rallat.search.Pagination.ITEMS_PAGE;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map.Entry;

import android.net.Uri;

public class ApiHttpUrlConnection {
    private static final String HOST = "http://ajax.googleapis.com/ajax/services/search/images?v=1.0";
    private final HashMap<String, String> mQueryKeyPair = new HashMap<String, String>();
    private final HttpURLConnection mConnection;

    public ApiHttpUrlConnection(String[] params) throws IOException {
        mConnection = (HttpURLConnection) getURL(params).openConnection();
        setupConnection();
    }

    private void setupConnection() throws ProtocolException {
        if (mConnection != null) {
            mConnection.setRequestMethod("GET");
            mConnection.setRequestProperty("Accept", "text/javascript");
            mConnection.setRequestProperty("Referer", "test-android-app");
            mConnection.setRequestProperty("Accept-Charset", "utf-8");
            mConnection.setReadTimeout(7000);
            mConnection.setConnectTimeout(7000);
        }
    }

    public HttpURLConnection getHttpURLConnection() {
        return mConnection;
    }

    private URL getURL(String[] params) {
        if ((params != null) && (params.length > 1)) {
            String query = params[0];
            String start = params[1];
            mQueryKeyPair.put("q", query);
            mQueryKeyPair.put("start", start);
            mQueryKeyPair.put("rsz", ITEMS_PAGE.toString());
        }
        try {
            Uri.Builder builder = Uri.parse(HOST).buildUpon();
            for (Entry<String, String> queryParameter : mQueryKeyPair.entrySet()) {
                builder.appendQueryParameter(queryParameter.getKey(), queryParameter.getValue());
            }
            return new URL(builder.build().toString());
        } catch (MalformedURLException e) {
            return null;
        }
    }

}
