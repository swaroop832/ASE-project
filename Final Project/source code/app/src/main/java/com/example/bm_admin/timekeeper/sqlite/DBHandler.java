package com.example.bm_admin.timekeeper.sqlite;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;


public class DBHandler {

    private static DBHandler instance=new DBHandler();

    public static DBHandler getInstance() {
        if (instance == null) {
            instance = new DBHandler();
        }
        return instance;
    }

    private ContentValues contentValues;

    public ContentProviderOperation doMultipleInsertAndUpdate(
            String tableName, String jsonStr, String whereStr,
            String whereArgs[], boolean isUpdate, boolean isInsert,
            Context context) {
        ContentProviderOperation contentProviderOperation = null;
        try {

            if (jsonStr != null)
                contentValues = prePareContentValues(jsonStr);
            if (isInsert) {
                contentProviderOperation = ContentProviderOperation
                        .newInsert(Uri.parse(DBConfig.URI + tableName))
                        .withValues(contentValues).build();
            } else if (isUpdate) {
                contentProviderOperation = ContentProviderOperation
                        .newUpdate(Uri.parse(DBConfig.URI + tableName))
                        .withValues(contentValues)
                        .withSelection(whereStr, whereArgs).build();
            } else {
                contentProviderOperation = ContentProviderOperation
                        .newDelete(Uri.parse(DBConfig.URI + tableName))
                        .withSelection(whereStr, whereArgs).build();
            }

        } catch (Exception e) {
           e.printStackTrace();
        }
        return contentProviderOperation;

    }

    public boolean insertHandler(String tableName, String jsonStr,
                                 Context context) {
        try {
            contentValues = prePareContentValues(jsonStr);
            Uri uri = context.getContentResolver().insert(
                    Uri.parse(DBConfig.URI + tableName), contentValues);
            if (uri.getLastPathSegment().equals("0")) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateAndDeleteHandler(String tableName,
                                          String jsonStr, String whereStr, String whereArgs[],
                                          boolean isUpdate, Context context) {
        try {
            int retCount;
            if (jsonStr != null) {
                contentValues = prePareContentValues(jsonStr);
            }

            if (isUpdate) {
                retCount = context.getContentResolver().update(
                        Uri.parse(DBConfig.URI + tableName), contentValues,
                        whereStr, whereArgs);
            } else {
                retCount = context.getContentResolver().delete(
                        Uri.parse(DBConfig.URI + tableName), whereStr,
                        whereArgs);
            }
            if (retCount >= 1) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    public static String selectHandler(String tableName,
                                       String selectionArgs[], String where, String whereArgs[],
                                       String rawQuery, boolean isSingleRow, boolean isStringReq,
                                       boolean isArrayListReq, Context context) {
        String retStr = "";
        try {
            Cursor cursor = context.getContentResolver().query(
                    Uri.parse(DBConfig.URI + tableName), selectionArgs,
                    where, whereArgs, rawQuery);

            JSONObject jsonObject;
            JSONArray jsonArray = new JSONArray();
            jsonObject = new JSONObject();

            if (cursor.moveToFirst()) {
                do {
                    jsonObject = new JSONObject();
                    int count = 0;
                    while (count < cursor.getColumnCount()) {
                        jsonObject.put(cursor.getColumnName(count),
                                cursor.getString(count));
                        count++;
                    }
                    if (isSingleRow) {
                        retStr = jsonObject.toString();
                    } else if (isArrayListReq) {
                        retStr += cursor.getString(0);
                        if (!cursor.isLast())
                            retStr = retStr + ",";
                    } else if (isStringReq) {
                        retStr = cursor.getString(0);
                    } else {
                        jsonArray.put(jsonObject);
                    }
                } while (cursor.moveToNext());
            } else {
                retStr = null;
            }
            cursor.close();
            if (isSingleRow) {
                retStr = jsonObject.toString();
            }
            if ((!isSingleRow && !isStringReq) && !isArrayListReq) {
                retStr = jsonArray.toString();
            }

        } catch (Exception e) {
           e.printStackTrace();
        }
        return retStr;
    }

    private ContentValues prePareContentValues(String jsonStr) {

        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            contentValues = new ContentValues();
            Iterator<String> iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                if (jsonObject.getString(key) != null)
                    contentValues.put(key, jsonObject.getString(key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contentValues;
    }
}
