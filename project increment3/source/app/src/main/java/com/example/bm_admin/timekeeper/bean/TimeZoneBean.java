package com.example.bm_admin.timekeeper.bean;

/**
 * Created by bm-admin on 30/3/17.
 */
public class TimeZoneBean {

    private String title;
    private String subTitle;
    private String shortName;

    public TimeZoneBean() {
    }

    public TimeZoneBean(String title, String subTitle) {
        this.title = title;
        this.subTitle = subTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
