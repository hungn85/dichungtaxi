package com.dichungtaxi.google;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.dichungtaxi.Utils.Util;
import com.dichungtaxi.fragment.AbstractMainFragments;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by CHIP on 15/08/2015.
 */
public class VillageAutocompleteAdapter extends GooglePlacesAutocompleteAdapter {

    public VillageAutocompleteAdapter(Context context, int textViewResourceId,int parentId) {

        super(context, textViewResourceId, parentId);

    }

    protected ArrayList getResultList(String query) {

        String id =  AbstractMainFragments.getCityId();
        ArrayList resultList = null;
        query = URLEncoder.encode(query);
        String url = "http://taxiairport.vn/prepare/cdv?q=" + query + "&type=city&id=" + id + "&select2=1";
        String sb = Util.getContentFromUrl(url, getContext()).toString();

        try {
            JSONArray jas =  new JSONArray(sb);
            int size = jas.length();

           resultList = new ArrayList(size);

            for(int i = 0; i < size; i ++) {
                resultList.add(jas.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    protected String getAddressKeyName() {
        return "name";
    }

    public String getVillageId(int index) {
        JSONObject obj = (JSONObject)resultList.get(index);
        try {
            return obj.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public LatLng getLatLng(int index) {
        if(resultList == null) return null;

        JSONObject obj = (JSONObject)resultList.get(index);

        try {
            ArrayList al = GmapUtils.autocomplete(obj.getString(getAddressKeyName()), getContext());
            if(al.size() > 0) {
                JSONObject jo = (JSONObject) al.get(0);
                JSONObject location = jo.getJSONObject("geometry").getJSONObject("location");
                Double lat = Double.parseDouble(location.getString("lat"));
                Double lng = Double.parseDouble(location.getString("lng"));
                return new LatLng(lat, lng);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

}