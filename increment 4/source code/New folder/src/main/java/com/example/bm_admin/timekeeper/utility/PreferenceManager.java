package com.example.bm_admin.timekeeper.utility;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by suresh on 17/3/17.
 */

public class PreferenceManager {

    public static void storeIntoSharedPrefrence(Context context, String key, String value) {
        try {
            SharedPreferences preferences = android.preference.PreferenceManager
                    .getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key, value);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static String retrieveFromSharedPrefrence(Context context, String key) {
        try {
            SharedPreferences preferences = android.preference.PreferenceManager
                    .getDefaultSharedPreferences(context);
            return preferences.getString(key, "");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    public static void storeBooleanSharedPrefrence(Context context, String key, boolean value) {
        try {
            SharedPreferences preferences = android.preference.PreferenceManager
                    .getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(key, value);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean retrieveBooleanSharedPrefrence(Context context, String key) {
        try {
            SharedPreferences preferences = android.preference.PreferenceManager
                    .getDefaultSharedPreferences(context);
            return preferences.getBoolean(key, false);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
