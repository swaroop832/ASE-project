package com.example.bm_admin.timekeeper.bean;

/**
 * Created by suresh on 30/3/17.
 */

public class SettingsBean {

    private Integer id;
    private String email;
    private String fb_event;
    private String calendar_event;
    private String sound;
    private String notification;
    private String vibration ;
    private String createdDate ;
    private String label1;
    private String label2;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFb_event() {
        return fb_event;
    }

    public void setFb_event(String fb_event) {
        this.fb_event = fb_event;
    }

    public String getCalendar_event() {
        return calendar_event;
    }

    public void setCalendar_event(String calendar_event) {
        this.calendar_event = calendar_event;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getVibration() {
        return vibration;
    }

    public void setVibration(String vibration) {
        this.vibration = vibration;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getLabel1() {
        return label1;
    }

    public void setLabel1(String label1) {
        this.label1 = label1;
    }

    public String getLabel2() {
        return label2;
    }

    public void setLabel2(String label2) {
        this.label2 = label2;
    }
}
