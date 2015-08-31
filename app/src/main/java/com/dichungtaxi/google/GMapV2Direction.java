package com.dichungtaxi.google;

/**
 * Created by CHIP on 15/08/2015.
 */

import android.content.Context;
import android.util.Log;

import com.dichungtaxi.Utils.Util;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GMapV2Direction {
    public final static String MODE_DRIVING = "driving";
    public final static String MODE_WALKING = "walking";


    JSONObject jsonObj;
    public GMapV2Direction(LatLng start, LatLng end, Context context) {
        String url = "http://maps.googleapis.com/maps/api/directions/json?"

                + "origin=" + start.latitude + "," + start.longitude
                + "&destination=" + end.latitude + "," + end.longitude
                + "&sensor=false&units=metric&mode=driving";


        StringBuilder jsonResults = Util.getContentFromUrl(url.toString(), context);
        try {
            jsonObj = new JSONObject(jsonResults.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<LatLng> getDirection() {

        ArrayList<LatLng> resultList = new ArrayList<LatLng>();
        try {

            // Create a JSON object hierarchy from the results

            JSONArray steps = getLeg().getJSONArray("steps");
            for(int i = 0; i < steps.length(); i++) {
                JSONObject jLatLng = steps.getJSONObject(i);

                String polyline = "";
                polyline = (String)((JSONObject)jLatLng.get("polyline")).get("points");
                List<LatLng> list = decodePoly(polyline);

                for(int l=0;l<list.size();l++){
                    resultList.add(list.get(l));
                }

//                JSONObject jStart = jLatLng.getJSONObject("start_location");
//                String lat = jStart.getString("lat");
//                String lng = jStart.getString("lng");
//                resultList.add(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)));
//
//
//
//                JSONObject eStart = jLatLng.getJSONObject("end_location");
//                lat = eStart.getString("lat");
//                lng = eStart.getString("lng");
//                resultList.add(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)));

            }
        } catch (JSONException e) {
            Log.e("getDirection", "Cannot process JSON results", e);
        }
        return resultList;
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }

    private JSONObject getLeg() {
        try {
            return jsonObj.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public int getMet() {
        try {
            return getLeg().getJSONObject("distance").getInt("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;

    }

    public String getDuration() {
        try {
            return getLeg().getJSONObject("duration").getString("text");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";

    }
}