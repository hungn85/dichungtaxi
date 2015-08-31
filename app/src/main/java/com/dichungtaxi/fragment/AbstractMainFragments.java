package com.dichungtaxi.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.dichungtaxi.main.MainActivity;
import com.dichungtaxi.main.MyActivity;
import com.dichungtaxi.main.R;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by CHIP on 13/08/2015.
 */
abstract public class AbstractMainFragments extends Fragment {

    protected View myView;

    protected static TabHost tabHost;
    static int tabSelectedIndex = 0;
    static int tmpTabSelectedIndex = 0;
    protected static String[] citys;
    protected static int citySelectedIndex = -1;
    public static int vehicleId = 4;
    protected static String[] airports;
    protected static int airportSelectedIndex = -1;

    protected static String strVillageId;
    protected static String pickAddress;
    protected static LatLng pickPos;

    protected static String dropAddress;
    protected static LatLng dropPos;

    public static JSONArray chunkMatchArr;
    public static JSONObject chunkSelected;


    Boolean cancelSelectTab = false;

    final public static String CHUNK_AIRPORT_CITY = "1";
    final public static String CHUNK_CITY_AIRPORT = "2";
    final public static String CHUNK_AIRPORT_PROVINCE = "3";
    final public static String CHUNK_CITY_PROVINCE = "4";
    final public static String CHUNK_IN_CITY = "5";

    public static boolean useAirport() {
        return tmpTabSelectedIndex == 0;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(getLayout(), container, false);
        return myView;
    }

    abstract int getLayout();

    public static String[] getCitys() {
        if (citys == null) {
            try {

                JSONArray jCitys = MyActivity.jsonServer.getJSONArray("city");
                citys = new String [jCitys.length()];
                for(int i = 0; i < citys.length; i++) {
                    JSONObject joc = jCitys.getJSONObject(i);
                    citys[i] = joc.getString("name");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return citys;
    }

    public static String getCityId()  {
        String cityId = "";
        try {

            if(airportSelectedIndex != -1) {
                cityId = MyActivity.jsonServer.getJSONArray("airport").getJSONObject(airportSelectedIndex).getString("city_id");
            }
            else if(citySelectedIndex != -1) {
                cityId = MyActivity.jsonServer.getJSONArray("city").getJSONObject(citySelectedIndex).getString("id");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cityId;
    }

    public static String getAirportId() {
        try {

            JSONObject jo = MyActivity.jsonServer.getJSONArray("airport").getJSONObject(airportSelectedIndex);
            return jo.getInt("id")+"";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getAirportName() {
        try {

            JSONObject jo = MyActivity.jsonServer.getJSONArray("airport").getJSONObject(airportSelectedIndex);
            return jo.getString("name")+"";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static LatLng getAirportLatLng() {
        try {

            JSONObject jo = MyActivity.jsonServer.getJSONArray("airport").getJSONObject(airportSelectedIndex);
            String ll = jo.getString("ll");
            return strToLatLng(ll);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static LatLng strToLatLng(String ll) {
        String [] arr = ll.split(",");
        Double lat = Double.parseDouble(arr[0]);
        Double lng = Double.parseDouble(arr[1]);
        return new LatLng(lat, lng);
    }

    public static String[] getAirports() {

        if (airports == null) {
            try {
                JSONArray jAirports = MyActivity.jsonServer.getJSONArray("airport");
                airports = new String [jAirports.length()];
                for(int i = 0; i < airports.length; i++) {
                    JSONObject joc = jAirports.getJSONObject(i);
                    airports[i] = joc.getString("name");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return airports;
    }


    public static String getChunkId() {
        try {
            return chunkSelected.getInt("id")+"";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isRequireAirport() {
        return isFromAirport() || isToAirport();
    }

    public static boolean isFromAirport() {

        if(chunkSelected != null) {
            try {
                String ctId = chunkSelected.getString("data-chunk-type-id");
                return ctId.equalsIgnoreCase(CHUNK_AIRPORT_PROVINCE) || ctId.equalsIgnoreCase(CHUNK_AIRPORT_CITY);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean isToAirport() {
        if(chunkSelected != null) {
            try {
                String ctId = chunkSelected.getString("data-chunk-type-id");
                return ctId.equalsIgnoreCase(CHUNK_CITY_AIRPORT);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void resetCityAirportSelected(String type) {
        if(type.equalsIgnoreCase("city")) {
            airportSelectedIndex = -1;
        }
        else {
            citySelectedIndex = -1;
        }
        MyActivity.getChunk(getActivity()).setText("");
    }

    public boolean notCityAirportSelected() {
        return airportSelectedIndex == -1 && citySelectedIndex == -1;
    }

    public void selectCityDialog(final Runnable t) {
        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
        b.setTitle(getString(R.string.select_city));
        b.setItems(getCitys(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int index) {
                dialog.dismiss();
                citySelectedIndex = index;
                doSelectCityAirport(t, index, "city");
            }
        });

        b.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                cancelSelectTab = true;
                tabHost.setCurrentTab(tabSelectedIndex);

            }

        });

        b.show();

    }


    public void selectAirportDialog( final Runnable t ) {

        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
        b.setTitle(getString(R.string.select_airport));
        b.setItems(getAirports(), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int index) {

            dialog.dismiss();
            airportSelectedIndex = index;
            doSelectCityAirport(t, index, "airport");
            }
        });
        b.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                cancelSelectTab = true;
                tabHost.setCurrentTab(tabSelectedIndex);
            }
        });
        b.show();
    }

    protected  void doSelectCityAirport(Runnable t, int index, String type) {
        resetCityAirportSelected(type);
        tabSelectedIndex = tabHost.getCurrentTab();
        showCityAirportSelected(index, type);
        resetChunkAndAddress();

        if (t != null) {
            getActivity().runOnUiThread(t);
        }
        else {
            MyActivity.getChunk(getActivity()).performClick();
        }
    }

    protected void resetChunkAndAddress() {
        chunkSelected = null;
        chunkMatchArr = null;
        chunkSelected = null;

        MyActivity.getChunk(getActivity()).setText("");
        resetAddress();
    }


    protected void resetAddress() {

        int color = Color.parseColor("#000000");
        if(HomeFragment.autoStartAddress != null) {
            HomeFragment.autoStartAddress.setEnabled(true);
            HomeFragment.autoStartAddress.setTextColor(color);
        }
        if(HomeFragment.autoEndAddress != null) {
            HomeFragment.autoEndAddress.setEnabled(true);
            HomeFragment.autoEndAddress.setTextColor(color);
        }


        strVillageId = null;
        pickAddress = null;
        pickPos = null;
        dropAddress = null;
        dropPos = null;

        MyActivity.getPickAddress(getActivity()).setText("");
        MyActivity.getDropAddress(getActivity()).setText("");
        getButton(R.id.btn_view_km).setVisibility(View.GONE);
        getRelativeLayout(R.id.home_layout_price).setVisibility(View.GONE);
        getRelativeLayout(R.id.homeLoadingPrice).setVisibility(View.GONE);

    }

    protected void hideKeyboard() {
        // Check if no view has focus:
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    protected void selectCityOrAirport(Thread t) {
        hideKeyboard();
        tmpTabSelectedIndex = tabHost.getCurrentTab();
        if (useAirport()) {
            selectAirportDialog(t);

        } else {
            selectCityDialog( t);
        }

    }

    private void showCityAirportSelected (int index, String k) {

        String airportName = "";
        try {
            airportName = MyActivity.jsonServer.getJSONArray(k).getJSONObject(index).getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MyActivity.getBtnCityAirport(getActivity()).setText(airportName);
    }

    protected RelativeLayout getRelativeLayout(int id) {
        return (RelativeLayout) getActivity().findViewById(id);
    }

    protected LinearLayout getLinearLayout(int id) {
        return (LinearLayout) getActivity().findViewById(id);
    }

    protected TextView getTextView(int id) {
        return (TextView) getActivity().findViewById(id);
    }

    protected Button getButton(int id) {
        return (Button) getActivity().findViewById(id);
    }

    protected AutoCompleteTextView getAutocompleteTextView(int id) {
        return (AutoCompleteTextView) getActivity().findViewById(id);
    }
}
