package com.example.bm_admin.timekeeper.sqlite;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;

import com.example.bm_admin.timekeeper.utility.Utils;


public class DataBaseProvider extends ContentProvider {

    private SQLiteDatabase db;

    public DataBaseProvider() {
        super();
    }


    @Override
    public boolean onCreate() {
        try {
            Context context = getContext();
            DatabaseHelper dbHelper = null;

            if (Build.VERSION.SDK_INT >= 23) {
                if (Utils.checkPermission(context)) {
                    dbHelper = new DatabaseHelper(context);
                } else {
                    dbHelper = new DatabaseHelper(context, true);
                }
            }else{
                dbHelper = new DatabaseHelper(context);
            }

            db = dbHelper.getWritableDatabase();
            return (db == null) ? false : true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID;
        Uri _uri = uri;
        try {
            rowID = db.insertWithOnConflict(uri.getLastPathSegment(), null,
                    values, SQLiteDatabase.CONFLICT_REPLACE);
            if (rowID > 0) {
                rowID = 1;
            } else {
                rowID = 0;
            }
            _uri = ContentUris.withAppendedId(uri, rowID);
        } catch (Exception e) {
            //System.out.println("inside catch"+e.toString());
            _uri = ContentUris.withAppendedId(uri, 1);
            return _uri;
        }
        return _uri;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        try {
            if (sortOrder == null) {
                cursor = db.query(uri.getLastPathSegment(), projection,
                        selection, selectionArgs, null, null, null);
            } else {
                cursor = db.rawQuery(selection, selectionArgs);
            }
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        } catch (Exception e) {
        }
        return cursor;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        count = db.delete(uri.getLastPathSegment(), selection, selectionArgs);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count = 0;
        count = db.update(uri.getLastPathSegment(), values, selection,
                selectionArgs);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

}
