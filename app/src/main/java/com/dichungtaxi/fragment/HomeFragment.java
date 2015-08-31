package com.dichungtaxi.fragment;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dichungtaxi.Utils.Util;
import com.dichungtaxi.google.GMapV2Direction;
import com.dichungtaxi.google.GooglePlacesAutocompleteAdapter;
import com.dichungtaxi.google.VillageAutocompleteAdapter;
import com.dichungtaxi.main.MyActivity;
import com.dichungtaxi.main.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment
        extends AbstractMainFragments
        implements AdapterView.OnItemClickListener, OnMapReadyCallback
{
    static AutoCompleteTextView autoEndAddress;
    static AutoCompleteTextView autoStartAddress;
    GooglePlacesAutocompleteAdapter autoEndAdapterGoogle;
    VillageAutocompleteAdapter autoEndAdapterVillage;
    GooglePlacesAutocompleteAdapter autoStartAdapterGoogle;
    VillageAutocompleteAdapter autoStartAdapterVillage;
    ArrayList<LatLng> directionPoint;
    boolean isDrawRoute = false;
    SupportMapFragment mMapFragment;
    GoogleMap map;
    HashMap markers = new HashMap();
    GMapV2Direction md;
    View myView;
    Polyline polyline;

    private void addMarker(LatLng paramLatLng, String paramString)
    {
        int i = R.drawable.ic_start;
        if (paramString.equalsIgnoreCase("B")) {
            i = R.drawable.ic_end;
        }
        if (this.markers.containsKey(paramString)) {
            ((Marker)this.markers.get(paramString)).remove();
        }
        this.markers.put(paramString, this.map.addMarker(new MarkerOptions().position(paramLatLng).title(paramString).icon(BitmapDescriptorFactory.fromResource(i))));
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(paramLatLng);
        bound(localArrayList);
    }

    private void bound(ArrayList<LatLng> paramArrayList)
    {
        this.isDrawRoute = false;
        CameraUpdate localCameraUpdate;
        if (paramArrayList.size() == 1) {
            localCameraUpdate = CameraUpdateFactory.newLatLngZoom(
                    new LatLng(((LatLng) paramArrayList.get(0)).latitude, ((LatLng) paramArrayList.get(0)).longitude), 12.0F);

        }
        else{
            LatLngBounds.Builder localBuilder = new LatLngBounds.Builder();
            Iterator localIterator = paramArrayList.iterator();
            while (localIterator.hasNext()) {
                localBuilder.include((LatLng) localIterator.next());
            }
            localCameraUpdate = CameraUpdateFactory.newLatLngBounds(localBuilder.build(), 3);
            this.isDrawRoute = true;
        }

        this.map.animateCamera(localCameraUpdate, new GoogleMap.CancelableCallback() {
            public void onCancel() {
            }

            public void onFinish() {
                if (isDrawRoute) {
                    map.animateCamera(CameraUpdateFactory.zoomOut());
                }
            }
        });
        return;
    }

    private void clearMarkers()
    {
        String[] arrayOfString = "A,B".split(",");
        for (int i = 0; i < arrayOfString.length; i++)
        {
            String str = arrayOfString[i];
            if (this.markers.containsKey(str))
            {
                ((Marker)this.markers.get(str)).remove();
                this.markers.remove(str);
            }
        }
    }

    private void disableAddress(AutoCompleteTextView paramAutoCompleteTextView, String paramString)
    {
        paramAutoCompleteTextView.setEnabled(false);
        paramAutoCompleteTextView.setText(paramString);
    }

    private void drawRoute(LatLng paramLatLng1, LatLng paramLatLng2)
    {
        hideKeyboard();
        if (this.polyline != null) {
            this.polyline.remove();
        }
        this.map.clear();
        this.md = new GMapV2Direction(paramLatLng1, paramLatLng2, getActivity().getApplicationContext());
        this.directionPoint = this.md.getDirection();
        int i = this.directionPoint.size();
        if (i > 0)
        {
            addMarker((LatLng)this.directionPoint.get(0), "A");
            addMarker((LatLng)this.directionPoint.get(-1 + this.directionPoint.size()), "B");
        }
        PolylineOptions localPolylineOptions = new PolylineOptions().width(5.0F).color(-16776961);
        for (int j = 0; j < i; j++) {
            localPolylineOptions.add((LatLng)this.directionPoint.get(j));
        }
        this.polyline = this.map.addPolyline(localPolylineOptions);
        this.polyline.setGeodesic(true);
        bound(this.directionPoint);
        getTextView(R.id.btn_view_km).setVisibility(View.VISIBLE);
        getTextView(R.id.btn_view_km).setText("Khoảng cách " + Math.round(this.md.getMet() / 1000) + "Km");
    }

    private void drawRouteOrAddMarker()
    {
        if ((pickPos != null) && (dropPos != null))
        {
            if (this.polyline != null) {
                this.polyline.remove();
            }
            drawRoute(pickPos, dropPos);
            getMinMaxPrice();
        }
        else if(pickPos != null) {
            addMarker(pickPos, "A");
        }
        else if(dropPos != null) {
            addMarker(dropPos, "B");
        }

    }

    private void enableAddress(AutoCompleteTextView paramAutoCompleteTextView)
    {
        paramAutoCompleteTextView.setEnabled(true);
        paramAutoCompleteTextView.setText("");
    }

    private JSONArray getChunksMatch()
    {
        HashMap chunkTypeConf = new HashMap();
        chunkTypeConf.put("0", "");
        chunkTypeConf.put("1", "4");
        chunkTypeConf.put("2", "5");
        JSONArray matchChunks = new JSONArray();
        try
        {
            String cityId = getCityId();
            JSONArray chunks = MyActivity.jsonServer.getJSONArray("chunk");

            for ( int i = 0;  i < chunks.length(); i++)
            {
                JSONObject jchunk = chunks.getJSONObject(i);
                String id = jchunk.getString("id");
                String cityIds = jchunk.getString("data-city-id");
                boolean requireAirport = jchunk.getString("data-require-airport").equalsIgnoreCase("1");
                String chunkTypeId = jchunk.getString("data-chunk-type-id");
                jchunk.put("index", i);

                String chunkTypes = chunkTypeConf.get(tabSelectedIndex + "").toString();

                if(id.length() == 0 || cityIds.indexOf(","+cityId+",") == -1) {
                    continue;
                }

                if(tabSelectedIndex == 0 && requireAirport) {
                    System.out.println(jchunk);
                    matchChunks.put(jchunk);
                }
                else if(chunkTypes.indexOf(chunkTypeId ) != -1) {
                    System.out.println(jchunk);
                    matchChunks.put(jchunk);
                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();

        }

        chunkMatchArr = matchChunks;
        return matchChunks;
    }

    private void initChunk()
    {
        getChunk().setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                if (notCityAirportSelected()) {
                    selectCityOrAirport(new Thread() {
                        public void run() {
                            selectChunk();
                        }
                    });
                    return;
                }
                selectChunk();
            }
        });
    }

    private void initGoogleMap()
    {
        FragmentManager localFragmentManager = getActivity().getSupportFragmentManager();
        this.mMapFragment = ((SupportMapFragment)localFragmentManager.findFragmentById(R.id.map));
        if (this.mMapFragment == null)
        {
            this.mMapFragment = SupportMapFragment.newInstance();
            localFragmentManager.beginTransaction().replace(R.id.map, this.mMapFragment).commit();
        }
        this.mMapFragment.getMapAsync(this);
    }

    private void initTop()
    {
        MyFragmentManager.replace(getFragmentManager(), R.id.home_top_fragment, new HomeTopFragment());
    }

    private boolean useGoogle()
    {
        try
        {
            if (chunkSelected != null)
            {
                boolean bool = chunkSelected.getString("data-use-google").equalsIgnoreCase("1");
                if (bool) {
                    return true;
                }
            }
            return false;
        }
        catch (JSONException localJSONException)
        {
            localJSONException.printStackTrace();
        }
        return true;
    }

    protected TextView getChunk()
    {
        return getTextView(R.id.chunk_id);
    }

    protected CharSequence[] getChunkItems()
    {
        JSONArray chunksMatch = getChunksMatch();
        CharSequence[] arrayOfCharSequence = new CharSequence[chunksMatch.length()];

        for (int i = 0; i < chunksMatch.length(); i ++)
        {
            try
            {
                arrayOfCharSequence[i] = chunksMatch.getJSONObject(i).getString("name");

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

        }
        return arrayOfCharSequence;
    }

    GooglePlacesAutocompleteAdapter getEndAdapterGoogle()
    {
        if (this.autoEndAdapterGoogle == null) {
            this.autoEndAdapterGoogle = new GooglePlacesAutocompleteAdapter(getActivity(), 2130968602, 2131558518);
        }
        return this.autoEndAdapterGoogle;
    }

    VillageAutocompleteAdapter getEndAdapterVillage()
    {
        if (this.autoEndAdapterVillage == null) {
            this.autoEndAdapterVillage = new VillageAutocompleteAdapter(getActivity(), 2130968602, 2131558518);
        }
        return this.autoEndAdapterVillage;
    }

    AutoCompleteTextView getEndAddress()
    {
        if (autoEndAddress == null)
        {
            autoEndAddress = (AutoCompleteTextView)this.myView.findViewById(R.id.end_address);
            autoEndAddress.setOnItemClickListener(this);
            autoEndAddress.setOnFocusChangeListener(new View.OnFocusChangeListener()
            {
                public void onFocusChange(View paramAnonymousView, boolean paramAnonymousBoolean)
                {
                    if ((paramAnonymousBoolean) && (notCityAirportSelected())) {
                        selectCityOrAirport(null);
                    }
                }
            });
        }

        autoEndAddress.setAdapter(useGoogle() ? getEndAdapterGoogle() : getEndAdapterVillage());

        return autoEndAddress;
    }

    int getLayout()
    {
        return R.layout.fragment_home;
    }

    protected void getMinMaxPrice()
    {
        new LoadingMinMaxTask().execute();
    }

    GooglePlacesAutocompleteAdapter getStartAdapterGoogle()
    {
        if (this.autoStartAdapterGoogle == null) {
            this.autoStartAdapterGoogle = new GooglePlacesAutocompleteAdapter(getActivity(), 2130968602, 2131558517);
        }
        return this.autoStartAdapterGoogle;
    }

    VillageAutocompleteAdapter getStartAdapterVillage()
    {
        if (this.autoStartAdapterVillage == null) {
            this.autoStartAdapterVillage = new VillageAutocompleteAdapter(getActivity(), 2130968602, 2131558517);
        }
        return this.autoStartAdapterVillage;
    }

    AutoCompleteTextView getStartAddress()
    {
        if (autoStartAddress == null)
        {
            autoStartAddress = (AutoCompleteTextView)this.myView.findViewById(R.id.start_address);
            autoStartAddress.setOnItemClickListener(this);
            autoStartAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View paramAnonymousView, boolean paramAnonymousBoolean) {
                    if ((paramAnonymousBoolean) && (notCityAirportSelected())) {
                        selectCityOrAirport(null);
                    }
                }
            });
        }
        autoStartAddress.setAdapter(useGoogle()?getStartAdapterGoogle():getStartAdapterVillage());
        return autoStartAddress;
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
    {
        if (this.myView == null)
        {
            this.myView = paramLayoutInflater.inflate(getLayout(), null, false);
            initGoogleMap();
            initTop();
        }
        return this.myView;
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        hideKeyboard();

        boolean isStart = R.id.start_address == id;
        boolean isEnd = R.id.end_address == id;

        if(isStart) {
            if (useGoogle()) {
                pickAddress = getStartAdapterGoogle().getItem(position);
                pickPos = getStartAdapterGoogle().getLatLng(position);

            } else {
                pickAddress = getStartAdapterVillage().getItem(position);
                pickPos = getStartAdapterVillage().getLatLng(position);
                strVillageId = getStartAdapterVillage().getVillageId(position);
            }
            drawRouteOrAddMarker();
        }

        if(isEnd) {
            if (useGoogle()) {
                dropAddress = getEndAdapterGoogle().getItem(position);
                dropPos = getEndAdapterGoogle().getLatLng(position);

            } else {
                dropAddress = getEndAdapterVillage().getItem(position);
                dropPos = getEndAdapterVillage().getLatLng(position);
                strVillageId = getEndAdapterVillage().getVillageId(position);
            }
            drawRouteOrAddMarker();
        }
    }

    public void onMapReady(GoogleMap paramGoogleMap)
    {
        this.map = paramGoogleMap;
        MapsInitializer.initialize(getActivity());
        this.map.setMyLocationEnabled(true);
        CameraUpdate localCameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(21.021488999999999D, 105.834284D), 12.0F);
        this.map.animateCamera(localCameraUpdate);
    }

    public void onViewCreated(View paramView, Bundle paramBundle)
    {
        super.onViewCreated(paramView, paramBundle);
        initChunk();
        getButton(R.id.seat4_btn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                AbstractMainFragments.vehicleId = 4;
                getMinMaxPrice();
            }
        });
        getButton(R.id.seat7_btn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                AbstractMainFragments.vehicleId = 7;
                getMinMaxPrice();
            }
        });
        getButton(R.id.seat15_btn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                AbstractMainFragments.vehicleId = 15;
                getMinMaxPrice();
            }
        });
    }

    void processAirportAddress() {

        if (isRequireAirport()) {
            LatLng  localLatLng = getAirportLatLng();
            String name = getString(R.string.airport) + " " + getAirportName();

            if (isFromAirport()) {
                pickPos = localLatLng;
                disableAddress(getStartAddress(), name);
                enableAddress(getEndAddress());
            }
            else {
                dropPos = localLatLng;
                disableAddress(getEndAddress(), name);
                enableAddress(getStartAddress());
            }
        }
    }

    void processFixAddress()
    {
        try
        {
            String fixDropPos = chunkSelected.getString("data-fix-drop-position");
            String fixPickPos = chunkSelected.getString("data-fix-pick-position");
            if(isRequireAirport()) {
                if ((isFromAirport()) && (fixDropPos.length() > 0)) {
                    dropPos = strToLatLng(fixDropPos);
                }

                if ((isToAirport()) && (fixPickPos.length() > 0)) {
                    pickPos = strToLatLng(fixPickPos);
                }
            }
            else {
                String[] arrayOfString = chunkSelected.getString("name").split("-");

                if (fixPickPos.length() > 0) {
                    pickPos = strToLatLng(fixPickPos);
                    disableAddress(getStartAddress(), arrayOfString[0].trim());
                }
                if (fixDropPos.length() > 0) {
                    dropPos = strToLatLng(fixDropPos);
                    disableAddress(getEndAddress(), arrayOfString[1].trim());
                }
            }
        }
        catch (JSONException localJSONException)
        {
            localJSONException.printStackTrace();
            return;
        }
    }

    void processFocusAddress() {
        if(pickPos == null)    {
            getStartAddress().requestFocus();
        }
        else if( dropPos == null) {
            getEndAddress().requestFocus();
        }
    }

    protected void selectChunk()
    {
        final CharSequence[] items = getChunkItems();
        if(items.length == 1) {
            selectChunkPos(items, 0);

        }
        else {
            AlertDialog.Builder localBuilder = new AlertDialog.Builder(getActivity());
            localBuilder.setTitle(getString(R.string.select_chunk));
            localBuilder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int position) {
                    selectChunkPos(items, position);
                }
            });
            localBuilder.create().show();
        }
    }

    void selectChunkPos(CharSequence[] items, int pos) {
        clearMarkers();
        map.clear();
        getChunk().setText(items[pos]);
        try {
            chunkSelected = chunkMatchArr.getJSONObject(pos);
            resetAddress();
            getStartAddress();
            getEndAddress();
            processAirportAddress();
            processFixAddress();
            drawRouteOrAddMarker();
            processFocusAddress();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    class LoadingMinMaxTask
            extends AsyncTask<Void, Void, Void>
    {
        StringBuilder sb;

        LoadingMinMaxTask() {}

        protected Void doInBackground(Void... paramVarArgs)
        {
            sb = Util.getContentFromUrl("http://dichungtaxi.com/home/get_min_max_price?dimension=1&city=" + getCityId() +
                    "&airport=" + getAirportId() +
                    "&chunk=" + getChunkId() +
                    "&depart_district=" + strVillageId +
                    "&vehicle_id=" + vehicleId +
                    "&city_id=" + getCityId(), getActivity());
            System.out.println(this.sb);
            try
            {
                JSONObject minmax = new JSONObject(this.sb.toString());
                final String privatePrice = minmax.getJSONObject("1").getString("price");
                final String sharePrice = minmax.getJSONObject("2").getString("price");
                getActivity().runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        getTextView(R.id.min_private_price).setText("Đi Riêng Rẻ nhất: " + privatePrice + "đ");
                        getTextView(R.id.min_share_price).setText("Đi Chung Rẻ nhất: " + sharePrice + "đ");
                    }
                });
                return null;
            }
            catch (JSONException localJSONException)
            {
                localJSONException.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void paramVoid)
        {
            getActivity().runOnUiThread(new Runnable()
            {
                public void run()
                {
                    getRelativeLayout(R.id.homeLoadingPrice).setVisibility(View.GONE);
                    getRelativeLayout(R.id.home_layout_price).setVisibility(View.VISIBLE);
                }
            });
        }

        protected void onPreExecute()
        {
            super.onPreExecute();
            getRelativeLayout(R.id.home_layout_price).setVisibility(View.GONE);
            getRelativeLayout(R.id.homeLoadingPrice).setVisibility(View.VISIBLE);

        }
    }
}
