package com.dichungtaxi.nav;

/**
 * Created by CHIP on 13/08/2015.
 */
public class NavItem {
    private String title, subTitle;
    int resIcon;

    public NavItem(String title, String subTitle, int resIcon) {
        this.title = title;
        this.subTitle = subTitle;
        this.resIcon = resIcon;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public void setResIcon(int resIcon) {
        this.resIcon = resIcon;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public int getResIcon() {
        return resIcon;
    }
}
