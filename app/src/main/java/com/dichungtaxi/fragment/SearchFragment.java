package com.dichungtaxi.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.dichungtaxi.main.R;


/**
 * Created by CHIP on 18/08/2015.
 */
public class SearchFragment extends Fragment {


    View myView;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.search_layout, container, false);
        return myView;
    }


}
