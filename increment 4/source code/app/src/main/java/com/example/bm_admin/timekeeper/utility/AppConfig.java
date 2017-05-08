package com.example.bm_admin.timekeeper.utility;

import android.app.Application;

import java.util.HashMap;
import java.util.TimeZone;

/**
 * Created by bm-admin on 28/3/17.
 */
public class AppConfig extends Application {

    private static AppConfig instance;

    public static synchronized AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }

    public String loginMail="";
    public String FBMAIL="FB";
    public String MAIL="MAIL";
    public String baselocationName="";
    public String locationName="";
    public TimeZone defaultTimeZone;
    public String locationKey = "LOCALE";

    public HashMap<String,String> answerMap=new HashMap<>();

    //Tables
    public static final String REGISTERTB = "register_tb";
    public static final String REMINDERTB = "reminder_tb";
    public static final String ALARMTB = "alarm_tb";
    public static final String NOTESTB = "notes_tb";
    public static final String LOCATIONTB = "location_tb";
    public static final String SETTINGSTB="settings_tb";

    //Create query
    public static final String CREATE_REGISTERTB = "create table register_tb(id INTEGER,name TEXT,email TEXT primary key NOT NULL,password TEXT,createdDate DATETIME CURRENT_TIMESTAMP,label1 TEXT,label2 TEXT)";
    public static final String CREATE_REMINDERTB = "create table reminder_tb(id INTEGER PRIMARY KEY,email TEXT,reqCode INTEGER DEFAULT 0,time DATETIME CURRENT_TIMESTAMP,desc TEXT,location TEXT,latitude double,longitude double,status TEXT DEFAULT 'N',interval TEXT,createdDate DATETIME CURRENT_TIMESTAMP,flag TEXT DEFAULT 'USR',label1 TEXT,label2 TEXT)";
    public static final String CREATE_ALARMTB = "create table alarm_tb(id INTEGER PRIMARY KEY,email TEXT,reqCode INTEGER DEFAULT 0,time DATETIME CURRENT_TIMESTAMP,isRepeat INTEGER DEFAULT 0,desc TEXT,interval TEXT,snooze INTEGER DEFAULT 0,createdDate DATETIME CURRENT_TIMESTAMP,label1 TEXT,label2 TEXT,maxCount INTEGER DEFAULT 0)";
    public static final String CREATE_NOTESTB = "create table notes_tb(id INTEGER PRIMARY KEY,email TEXT,notes TEXT,createdDate DATETIME CURRENT_TIMESTAMP,label1 TEXT,label2 TEXT)";
    public static final String CREATE_LOCATIONTB = "create table location_tb(id INTEGER PRIMARY KEY,email TEXT,location TEXT,createdDate DATETIME CURRENT_TIMESTAMP,label1 TEXT,label2 TEXT)";
    public static final String CREATE_SETTINGSTB = "create table settings_tb(id INTEGER PRIMARY KEY,email TEXT,fb_event TEXT DEFAULT 'N',calendar_event TEXT DEFAULT 'N',sound TEXT DEFAULT 'Y',notification TEXT DEFAULT 'Y',vibration TEXT DEFAULT 'Y',createdDate DATETIME CURRENT_TIMESTAMP,label1 TEXT,label2 TEXT)";

    //select query
    public static final String GETLOGINUSER = "select count(*) from register_tb where (email = ? and password = ? )";
    public static final String CHECKUSEREXIST = "select count(*) from register_tb where email = ? ";
    public static final String GETMYTASK= "select from reminder_tb where status=? and email=? order by time DESC";

    public static final String RawQuery = "RAW";

}
