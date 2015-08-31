package com.dichungtaxi.google;

import android.content.Context;
import android.util.Log;

import com.dichungtaxi.Utils.Util;
import com.dichungtaxi.main.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by CHIP on 15/08/2015.
 */
public class GmapUtils {
    private static final String LOG_TAG = "Google Places Autocomplete";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place/textsearch/json";


    public static ArrayList autocomplete(String input, Context context) {
        ArrayList resultList = null;
        input += ", Việt Nam";
        StringBuilder url = new StringBuilder(PLACES_API_BASE );
        url.append("?key=" + context.getString(R.string.gg_map_key));
        url.append("&components=country:vn");
        try {
            url.append("&query=" + URLEncoder.encode(input, "utf8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        StringBuilder jsonResults = Util.getContentFromUrl(url.toString(), context);


        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("results");

            // Extract the Place descriptions from the results
            resultList = new ArrayList(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                resultList.add(predsJsonArray.getJSONObject(i));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }
}
