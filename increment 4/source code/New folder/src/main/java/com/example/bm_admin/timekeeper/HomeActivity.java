package com.example.bm_admin.timekeeper;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.bm_admin.timekeeper.adapter.Drawer_Adapter;
import com.example.bm_admin.timekeeper.bean.Converter;
import com.example.bm_admin.timekeeper.bean.TimeZoneBean;
import com.example.bm_admin.timekeeper.fragments.About;
import com.example.bm_admin.timekeeper.fragments.Alarm;
import com.example.bm_admin.timekeeper.fragments.Notes;
import com.example.bm_admin.timekeeper.fragments.RemindFragement;
import com.example.bm_admin.timekeeper.fragments.HomeFragement;
import com.example.bm_admin.timekeeper.fragments.Settings;
import com.example.bm_admin.timekeeper.utility.AppConfig;
import com.example.bm_admin.timekeeper.utility.PreferenceManager;

import java.util.ArrayList;

/**
 * Created by bm-admin on 25/3/17.
 */
public class HomeActivity extends AppCompatActivity {
    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;

    String titles[] = {"Home","Remind", "Alarm", "Notes", "About", "Settings", "Logout"};
    int icons[] = {R.drawable.ic_home,
            R.drawable.ic_reminder,
            R.drawable.ic_alarm,
            R.drawable.ic_notes,
            R.drawable.ic_about,
            R.drawable.ic_settings,
            R.drawable.ic_logout};

    //  String position =  getIntent().getExtras().getString("keyName");
    String name = "Menu";
    String email = "Raky1993@gmail.com";
    int profile = R.mipmap.ic_launcher;
    private Toolbar toolbar;
    Fragment fragment = null;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    DrawerLayout Drawer;
    int position;
    ActionBarDrawerToggle mDrawerToogle;
    AppConfig appConfig;
    private boolean isHome=true;
    DrawerLayout drawer;
    FragmentManager fragmentManager = getSupportFragmentManager();

    public HomeActivity() {
        appConfig=AppConfig.getInstance();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu);
        drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.contentfragment, new HomeFragement());
        tx.commit();

        getLocationName();
        toolbar = (Toolbar) findViewById(R.id.tool_bar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("HOME");


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new Drawer_Adapter(titles, icons, name, email, profile, this);

        mRecyclerView.setAdapter(mAdapter);

        final GestureDetector mGestureDetector = new GestureDetector(HomeActivity.this, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });

        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                    Drawer.closeDrawers();

                    position = mRecyclerView.getChildPosition(child);

                    // Toast.makeText(getApplicationContext(), "RecyclerView = " + mRecyclerView.getChildPosition(child), Toast.LENGTH_SHORT).show();
                    Log.d("motion = ", "" + position);

                    onTouchDrawer(mRecyclerView.getChildPosition(child));
                    return true;
                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {


            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);
//        mDrawerToogle.setHomeAsUpIndicator(R.mipmap.drawer);

        mDrawerToogle = new ActionBarDrawerToggle(this, Drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
     //   Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.mipmap.drawer,
       //         getApplicationContext().getTheme());
       // mDrawerToogle.setHomeAsUpIndicator(drawable);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerToogle.setDrawerIndicatorEnabled(false);
        mDrawerToogle.setHomeAsUpIndicator(R.drawable.ic_menu);

        Drawer.setDrawerListener(mDrawerToogle);
        mDrawerToogle.syncState();

        //setLocationName();
        mDrawerToogle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                menuOpen();
            }
        });

       // startVoiceRecognitionActivity();
    }


    public boolean menuOpen()
    {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
        return true;
    }


    public Fragment onTouchDrawer(int position) {

        Log.w("Position", "" + position);

        isHome=false;
        if (position == 1) {
            isHome=true;
            getSupportActionBar().setTitle("HOME");
            fragment = new HomeFragement();
            fragmentManager.beginTransaction().replace(R.id.contentfragment, fragment).commit();
        } else if (position == 2) {
            getSupportActionBar().setTitle("REMIND");
            fragment = new RemindFragement();
            fragmentManager.beginTransaction().replace(R.id.contentfragment, fragment).commit();
        } else if (position == 3) {
            getSupportActionBar().setTitle("ALARM");
            fragment = new Alarm();
            fragmentManager.beginTransaction().replace(R.id.contentfragment, fragment).commit();
        } else if (position == 4) {
            getSupportActionBar().setTitle("NOTES");
            fragment = new Notes();
            fragmentManager.beginTransaction().replace(R.id.contentfragment, fragment).commit();
        } else if (position == 5) {
            getSupportActionBar().setTitle("ABOUT");
            fragment = new About();
            fragmentManager.beginTransaction().replace(R.id.contentfragment, fragment).commit();
        } else if (position == 6) {
            getSupportActionBar().setTitle("SETTINGS");
            fragment = new Settings();
            fragmentManager.beginTransaction().replace(R.id.contentfragment, fragment).commit();
        } else if (position == 7) {
            exitDialog();
        }
        return fragment;
    }

    @Override
    public void onBackPressed() {
        try {
            Log.d("","");
            if (!isHome) {
                fragment = new HomeFragement();
                fragmentManager.beginTransaction().replace(R.id.contentfragment, fragment).commit();
                getSupportActionBar().setTitle("HOME");
                isHome = true;
            } else {
                exitDialog();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getLocationName(){
        try{
            TimeZoneBean timeZoneBean= Converter.gson.fromJson(PreferenceManager.retrieveFromSharedPrefrence
                    (getApplicationContext(),appConfig.loginMail),TimeZoneBean.class);
            System.out.println("locationName"+(PreferenceManager.retrieveFromSharedPrefrence(getApplicationContext(),appConfig.loginMail)));
            if(timeZoneBean.getTitle() != null) {
                String[] parts = timeZoneBean.getTitle().split("/");
                appConfig.locationName = parts[parts.length - 1];
                Log.d("locationName",""+appConfig.locationName);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void exitDialog(){
        new AlertDialog.Builder(this)
                .setTitle("Do you want to logout from Time Keeper?")
                //.setMessage("Are you sure you want to exit From Application ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        dialog.cancel();
                    }
                })
                .show();
    }

  /*  private void setLocationName(){
        try{
        String json=PreferenceManager.retrieveFromSharedPrefrence(getApplicationContext(), appConfig.loginMail);
        TimeZoneBean timeZoneBean=Converter.gson.fromJson(json,TimeZoneBean.class);
        String[] parts = timeZoneBean.getTitle().split("/");
        appConfig.locationName = parts[parts.length - 1];
    }*/

    public void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "SLEEP TRACKER");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            String words= "";
            for(Object word : matches) {
                words = words + word.toString() +","+
                        Log.w("#WORD :",word.toString());
            }

            boolean result = false;
            if (matches.contains("menu")) {

                result = menuOpen();
                //informationMenu(words,true);
            }else if(matches.contains("remind")) {
                Toast.makeText(getApplicationContext(),"Remind",Toast.LENGTH_SHORT).show();
                Fragment fragment = onTouchDrawer(2);
                //fragmentManager.beginTransaction().replace(R.id.contentfragment, fragment).commit();
                result = false;
            }else
            {
                Toast.makeText(getApplicationContext(),"Thanks for Using Voice Actions",Toast.LENGTH_SHORT).show();
                //informationMenu(words,false);
            }

            if(result)
            {
                startVoiceRecognitionActivity();
            }
        }
    }
}
