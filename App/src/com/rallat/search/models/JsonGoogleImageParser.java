package com.rallat.search.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


public class JsonGoogleImageParser {
    public static final String TAG = JsonGoogleImageParser.class.getName();

    public ArrayList<GoogleImage> parseGoogleImages(InputStream json) throws IOException, JSONException {
        JSONObject jsonResponse = getJsonFromInputStream(json);
        JSONArray jsonResult = jsonResponse.getJSONObject("responseData").getJSONArray("results");
        ArrayList<GoogleImage> imagesList = new ArrayList<GoogleImage>();
        for (int i = 0; i < jsonResult.length(); i++) {
            try {
                JSONObject jsonImage = jsonResult.getJSONObject(i);
                GoogleImage image = new GoogleImage(jsonImage.getString("imageId"),
                                jsonImage.getString("titleNoFormatting"), jsonImage.getString("tbUrl"));
                imagesList.add(image);
            } catch (JSONException e) {
                Log.e(TAG, "JSON parser error", e);
            }
        }
        return imagesList;
    }

    protected JSONObject getJsonFromInputStream(InputStream json) throws IOException, JSONException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(json));
        StringBuilder sb = new StringBuilder();
        try {
            String line = reader.readLine();

            while (line != null) {
                sb.append(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            throw e;
        } finally {
            reader.close();
        }
        return new JSONObject(sb.toString());
    }

}
