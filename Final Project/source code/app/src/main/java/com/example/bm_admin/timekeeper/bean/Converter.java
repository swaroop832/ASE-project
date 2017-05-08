package com.example.bm_admin.timekeeper.bean;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bm-admin on 28/3/17.
 */
public class Converter {

    public static Gson gson = new Gson();


    public String getJSON() {
        return gson.toJson(this);
    }

    public <T> T getObject(String json) {
        GsonBuilder b = new GsonBuilder();
        b.setDateFormat("yyyy-MM-dd HH:mm:ss");
        Gson gson = b.create();
        return (T) gson.fromJson(json, this.getClass());
    }

    public static ArrayList<ReminderBean> convertJsonToReminderBeans(
            String json) {
        Type collectionType = new TypeToken<List<ReminderBean>>() {
        }.getType();
        return gson.fromJson(json, collectionType);
    }

    public static ArrayList<AlarmBean> convertJsonToAlarmBeans(
            String json) {
        Type collectionType = new TypeToken<List<AlarmBean>>() {
        }.getType();
        return gson.fromJson(json, collectionType);
    }

    public static ArrayList<RegisterBean> convertJsonToRegisterBeans(
            String json) {
        Type collectionType = new TypeToken<List<RegisterBean>>() {
        }.getType();
        return gson.fromJson(json, collectionType);
    }

    public static ArrayList<SettingsBean> convertJsonToSettingsBeans(
            String json) {
        Type collectionType = new TypeToken<List<SettingsBean>>() {
        }.getType();
        return gson.fromJson(json, collectionType);
    }

    public static ArrayList<NoteBean> convertJsonToNoteBeans(
            String json) {
        Type collectionType = new TypeToken<List<NoteBean>>() {
        }.getType();
        return gson.fromJson(json, collectionType);
    }

}