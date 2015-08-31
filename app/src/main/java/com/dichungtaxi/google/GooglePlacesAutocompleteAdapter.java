package com.dichungtaxi.google;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by CHIP on 15/08/2015.
 */
public class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {
    protected ArrayList resultList;
    int id;

    public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId,int parentId) {
        super(context, textViewResourceId);
        id  = parentId;
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public long getItemId(int position) {
        return id;
    }

    @Override
    public String getItem(int index) {
        JSONObject obj = (JSONObject)resultList.get(index);

        try {
            return obj.getString(getAddressKeyName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults filterResults = new FilterResults();
                if (constraint != null) {

                    // Retrieve the autocomplete results.
                    resultList = getResultList(constraint.toString());

                    // Assign the data to the FilterResults
                    filterResults.values = resultList;
                    filterResults.count = resultList.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

    protected ArrayList getResultList(String query)  {
        return GmapUtils.autocomplete(query, getContext());
    }

    protected String getAddressKeyName() {
        return "formatted_address";
    }

    public LatLng getLatLng(int index) {
        JSONObject obj = (JSONObject)resultList.get(index);
        try {
            JSONObject location = obj.getJSONObject("geometry").getJSONObject("location");
            Double lat = Double.parseDouble(location.getString("lat"));
            Double lng = Double.parseDouble(location.getString("lng"));
            return new LatLng(lat, lng);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}