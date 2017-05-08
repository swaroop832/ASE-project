package com.example.bm_admin.timekeeper;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {

    private int writePermission = 1;
    private int REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        getAndroidRunTimePermission();
    }

    private void startActivity() {
        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    private void getAndroidRunTimePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            writePermission = ContextCompat.checkSelfPermission(FullscreenActivity.this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (writePermission != 0) {
                getExternelStoragePermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_WRITE_EXTERNAL_STORAGE);
            }else{
                startActivity();
            }

        }else{
            startActivity();
        }
    }
    private void getExternelStoragePermission(final String permission, final int reqCode) {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(FullscreenActivity.this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(FullscreenActivity.this,
                    permission)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(FullscreenActivity.this)
                        .setMessage(getApplicationContext().getResources().getString(R.string.permission_msg))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(FullscreenActivity.this,
                                        new String[]{permission},
                                        reqCode);
                                dialog.dismiss();
                            }
                        }).show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(FullscreenActivity.this,
                        new String[]{permission},
                        reqCode);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                checkPermission(requestCode, grantResults);
                break;
            default:
                break;
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private boolean checkPermission(int requestCode, int[] grantResults) {

        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            writePermission = ContextCompat.checkSelfPermission(FullscreenActivity.this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

            startActivity();

        } else {
            startActivity();
        }
        return true;
    }

}
