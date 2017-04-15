package com.example.bm_admin.timekeeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.bm_admin.timekeeper.bean.Converter;
import com.example.bm_admin.timekeeper.bean.NoteBean;
import com.example.bm_admin.timekeeper.utility.Validation;

public class NoteViewerActivity extends AppCompatActivity {

    private WebView noteDesc;
    private NoteBean noteBean;
    private String intentStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_viewer);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        noteDesc = (WebView) findViewById(R.id.noteDesc);

        try {
            intentStr = getIntent().getStringExtra("NOTE");
            noteBean = Converter.gson.fromJson(intentStr, NoteBean.class);

            if (noteBean != null) {
                if (noteBean.getNotes() != null){
                    String youtContentStr = String.valueOf(Html
                            .fromHtml("<![CDATA[<body style=\"text-align:justify;color:#000000; \">"
                                    + noteBean.getNotes()
                                    + "</body>]]>"));
                    noteDesc.loadData(youtContentStr, "text/html", "utf-8");

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.note_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }else if(item.getItemId()==R.id.action_share){
            if (noteBean != null) {
                if (noteBean.getNotes() != null){
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, noteBean.getNotes());
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                }
            }

        }else{
            //
        }
        return super.onOptionsItemSelected(item);
    }
}
