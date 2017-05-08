package com.example.bm_admin.timekeeper.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.bm_admin.timekeeper.utility.AppConfig;
import com.example.bm_admin.timekeeper.utility.Utils;


/**
 * Created by suresh on 23/9/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, Utils.createDirIfNotExists(DBConfig.DIRECTORYPATH) + "/"
                + DBConfig.DB_NAME, null, DBConfig.DB_VERSION);
        System.out.println("DB File Directory");
    }

    public DatabaseHelper(Context context, boolean b) {
        super(context, context.getFilesDir().getAbsolutePath().replace("files", "databases") + "/"
                + DBConfig.DB_NAME, null, DBConfig.DB_VERSION);
        System.out.println("DB cache Directory");

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(AppConfig.CREATE_REGISTERTB);
        db.execSQL(AppConfig.CREATE_ALARMTB);
        db.execSQL(AppConfig.CREATE_LOCATIONTB);
        db.execSQL(AppConfig.CREATE_NOTESTB);
        db.execSQL(AppConfig.CREATE_SETTINGSTB);
        db.execSQL(AppConfig.CREATE_REMINDERTB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (newVersion) {
            case 2:
              /*  db.execSQL("ALTER table order_tb add column expiryDate TEXT");
                db.execSQL("ALTER table order_tb add column standardDescription TEXT");
                db.execSQL("ALTER table order_tb add column shortDesc TEXT");
*/
                break;
            default:
                break;
        }
    }
}
