package com.example.bm_admin.timekeeper.sqlite;

/**
 * Created by suresh on 30/6/16.
 */
public class DBConfig {

    public static String DB_NAME = "timekeeper.db";
    public static int DB_VERSION = 1;
    public static final String URI = "content://" + DBConfig.AUTHORITY + "/";
    public static final String AUTHORITY = "com.example.bm_admin.timekeeper";
    public static final String DIRECTORYPATH="TimeKeeper";

}
