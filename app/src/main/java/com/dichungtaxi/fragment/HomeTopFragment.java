package com.dichungtaxi.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TabHost;
import com.dichungtaxi.main.R;


/**
 * Created by CHIP on 18/08/2015.
 */
public class HomeTopFragment extends AbstractMainFragments  {



    @Override
    int getLayout() {
        return R.layout.fragment_home_top;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTabHost();
    }

    private void initTabHost() {
        tabHost = (TabHost) myView.findViewById(R.id.top_tabHost);
        tabHost.setup();

        String tabNames [] = {"Taxi Sân bay", "Liên Tỉnh", "Thành Phố"};
        for (int i = 0; i < tabNames.length; i++) {
            TabHost.TabSpec tabSpec = tabHost.newTabSpec(tabNames[i]);
            tabSpec.setIndicator(tabNames[i]);
            tabSpec.setContent(new FakeContent(getActivity()));

            tabHost.addTab(tabSpec);
        }

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {

                if(cancelSelectTab) { cancelSelectTab = false; return;}
                 selectCityOrAirport(null);
            }
        });

        for(int i = 0; i < tabNames.length; i++) {
            final int index = i;
            tabHost.getTabWidget().getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN && index == tabSelectedIndex) {

                        selectCityOrAirport(null);
                    }
                    return false;
                }
            });
        }
    }

    public class FakeContent implements TabHost.TabContentFactory {

        Context context;
        public FakeContent (Context context) {
            this.context = context;
        }
        @Override
        public View createTabContent(String tag) {
            View fakeView = new View(context);
            fakeView.setMinimumHeight(0);
            fakeView.setMinimumWidth(0);
            return fakeView;
        }
    }


}
