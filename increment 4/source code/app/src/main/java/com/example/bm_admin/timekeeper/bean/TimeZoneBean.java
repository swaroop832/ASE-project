package com.example.bm_admin.timekeeper.bean;


import java.util.TimeZone;

/**
 * Created by bm-admin on 30/3/17.
 */
public class TimeZoneBean {

    private TimeZone timeZone;
    private String title;
    private String subTitle;
    private String shortName;

    public TimeZoneBean() {
    }

    public TimeZoneBean(TimeZone timeZone,String title, String subTitle) {
        this.timeZone = timeZone;
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

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }
}
