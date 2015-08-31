package com.dichungtaxi.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.dichungtaxi.main.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CHIP on 17/08/2015.
 */
public class MyFragmentManager {

    private List<Fragment> getFragmentList = new ArrayList<Fragment>();
    int viewId;
    public MyFragmentManager(int viewId) {
        this.viewId = viewId;
    }

    public List<Fragment> getFragmentList () {
        return getFragmentList;
    }

    public void add(Fragment fragment) {
        getFragmentList ().add(fragment);
    }

    public Fragment get(int index) {
        return getFragmentList ().get(index);
    }


    public static void replace(FragmentManager fragmentManager, int id, Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(id, fragment);
        fragmentTransaction.commit();
    }

    public static void replaceBackStack(FragmentManager fragmentManager, int id, Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(id, fragment);
        fragmentTransaction.commit();
    }

    public static void replaceSlide(FragmentManager fragmentManager, int id, Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up);
        fragmentTransaction.replace(id, fragment);
        fragmentTransaction.commit();
    }

    public static void replaceSlideBackStack(FragmentManager fragmentManager, int id, Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up);
        fragmentTransaction.replace(id, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void goTo(FragmentManager fragmentManager, int index) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(viewId, get(index));
        fragmentTransaction.commit();
    }

    public void goToBackStack(FragmentManager fragmentManager, int index) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(viewId, get(index));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
