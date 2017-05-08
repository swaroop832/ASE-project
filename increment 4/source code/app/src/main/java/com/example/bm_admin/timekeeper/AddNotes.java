package com.example.bm_admin.timekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bm_admin.timekeeper.bean.Converter;
import com.example.bm_admin.timekeeper.bean.NoteBean;
import com.example.bm_admin.timekeeper.sqlite.DBHandler;
import com.example.bm_admin.timekeeper.utility.AppConfig;
import com.example.bm_admin.timekeeper.utility.Utils;
import com.example.bm_admin.timekeeper.utility.Validation;

import java.util.ArrayList;

/**
 * Created by bm-admin on 3/4/17.
 */
public class AddNotes extends AppCompatActivity {

    private EditText textToShare;
    private AppConfig appConfig;
    private DBHandler dbHandler;
    private boolean isChange=false;

    public AddNotes() {
        appConfig = AppConfig.getInstance();
        dbHandler = DBHandler.getInstance();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addnotes);

        textToShare = (EditText) findViewById(R.id.texttoshare);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    private void insertNote() {
        try {
            System.out.println("EMAIL :"+appConfig.loginMail);
            NoteBean noteBean = new NoteBean();
            noteBean.setCreatedDate(Utils.getCurrentDayWithTime());
            noteBean.setNotes(textToShare.getText().toString());
            noteBean.setEmail(appConfig.loginMail);
            if (dbHandler.insertHandler(AppConfig.NOTESTB, Converter.gson.toJson(noteBean), this)) {
                Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
                textToShare.setText("");
                isChange=true;
                onBackPressed();
            } else {
                System.out.println("Insert notes failure");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Insert notes failure");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }else if(item.getItemId()==R.id.action_save){
            if (!textToShare.getText().toString().trim().isEmpty()) {
                insertNote();
            }else{

            }
        }else{
            //
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(isChange){
            setResult(100);
            finish();
        }else {
            super.onBackPressed();
        }
    }
}

