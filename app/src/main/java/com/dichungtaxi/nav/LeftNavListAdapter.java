package com.dichungtaxi.nav;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dichungtaxi.main.R;

import java.util.List;

/**
 * Created by CHIP on 13/08/2015.
 */
public class LeftNavListAdapter extends ArrayAdapter<NavItem> {

    Context context;
    int resLayout;
    List<NavItem> listNavItems;
    public LeftNavListAdapter(Context context, int resource, List<NavItem> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resLayout = resource;
        this.listNavItems = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, resLayout, null);
        TextView tvTitle = (TextView) v.findViewById(R.id.nav_info_title);
        //TextView tvSubTitle = (TextView) v.findViewById(R.id.nav_info_subtitle);
        ImageView ivIcon = (ImageView) v.findViewById(R.id.nav_icon_home);

        NavItem navItem = listNavItems.get(position);
        tvTitle.setText(navItem.getTitle());
        //tvSubTitle.setText(navItem.getSubTitle());
        ivIcon.setImageResource(navItem.getResIcon());
        return  v;
    }
}
