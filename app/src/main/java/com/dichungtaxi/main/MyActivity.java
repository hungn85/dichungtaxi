package com.dichungtaxi.main;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dichungtaxi.Utils.Util;
import com.dichungtaxi.fragment.AboutFragment;
import com.dichungtaxi.fragment.HomeFragment;
import com.dichungtaxi.fragment.MyFragmentManager;
import com.dichungtaxi.nav.LeftNavListAdapter;
import com.dichungtaxi.nav.NavItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CHIP on 18/08/2015.
 */
public class MyActivity extends MainActivity {

    DrawerLayout drawerLayout;
    RelativeLayout drawerPane;
    ListView lvNav;
    List<NavItem> listNavItems;

    private NetworkStateReceiver networkStateReceiver;

    ActionBarDrawerToggle actionBarDrawerToggle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //prepare
        prepare();

    }

    private void prepare() {

        //set title
        setTitle(R.string.app_name);

        //top bar
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
        ab.setBackgroundDrawable(new ColorDrawable(Color.parseColor(getString(R.string.default_color))));

        //strict
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        processServerData();

        //assign layout
        prepareControls();

        //create nav items
        prepareListNavDatas();

        //set adapter for ListView lvNav
        prepareListNavAdapter_ClickEvent();

        //add com.dichungtaxi.slidingmenu.fragments
        prepareFragment();

        //prepare something
        goToFragment(0);

        //prepare actionBarDrawerToggle
        prepareActionBarDrawerToggle();


//        Intent intent = new Intent(this, MyActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

    }



    private void prepareControls() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerPane = (RelativeLayout) findViewById(R.id.drawer_pane);
        lvNav = (ListView) findViewById(R.id.nav_list);
    }

    private void prepareListNavDatas () {
        listNavItems = new ArrayList<NavItem>();
        listNavItems.add(new NavItem("Trang chủ", "Đặt taxi online", R.drawable.ic_action_home));
        listNavItems.add(new NavItem("Giới thiệu", "Giới thiệu về Dichungtaxi.com", R.drawable.ic_action_about));

//         listNavItems.add(new NavItem("Setting", "Setting page", R.drawable.ic_action_setting));
    }

    MyFragmentManager mfm;
    private void prepareFragment() {
        mfm = new MyFragmentManager(R.id.main_content);
        mfm.add(new HomeFragment());
        mfm.add(new AboutFragment());
    }


    public void goToFragment(int i) {
        mfm.goTo(getSupportFragmentManager(), i);

        //setTitle(listNavItems.get(i).getTitle());
        lvNav.setItemChecked(i, true);
        drawerLayout.closeDrawer(drawerPane);
    }

    private void prepareActionBarDrawerToggle() {
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_opened, R.string.drawer_closed) {
            @Override
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                invalidateOptionsMenu();
                super.onDrawerClosed(drawerView);
            }

        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
    }

    private void prepareListNavAdapter_ClickEvent() {
        LeftNavListAdapter navListAdapter = new LeftNavListAdapter(getApplicationContext(), R.layout.left_nav_item, listNavItems);
        lvNav.setAdapter(navListAdapter);
        lvNav.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                goToFragment(position);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.back_button:

                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    final String PREFS_NAME = "MyPrefsFile";
    public static JSONObject jsonServer;
    private void processServerData() {
        StringBuilder sb = Util.getContentFromUrl("http://taxiairport.vn/api/app_data", this);
        String serverData = null;
        if(sb != null) serverData = sb.toString();

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        try {
            if(serverData != null) {

                jsonServer = new JSONObject(serverData);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("data", serverData);
                editor.commit();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(jsonServer == null) {
            serverData  = settings.getString("data", null);
            if(serverData != null) {
                try {
                    jsonServer = new JSONObject(serverData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    public static Button getBtnCityAirport(Activity activity) {
        return (Button) activity.findViewById(R.id.btn_city_airport);
    }

    public static TextView getChunk(Activity activity) {
        return (TextView) activity.findViewById(R.id.chunk_id);
    }

    public static AutoCompleteTextView getPickAddress(Activity activity) {
        return (AutoCompleteTextView) activity.findViewById(R.id.start_address);
    }

    public static AutoCompleteTextView getDropAddress(Activity activity) {
        return (AutoCompleteTextView) activity.findViewById(R.id.end_address);
    }
}
