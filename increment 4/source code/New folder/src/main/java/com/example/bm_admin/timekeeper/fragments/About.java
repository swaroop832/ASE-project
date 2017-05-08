package com.example.bm_admin.timekeeper.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.bm_admin.timekeeper.R;

/**
 * Created by bm-admin on 25/3/17.
 */
public class About extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about, container, false);

        String myData = "Time Keeper is an application having features like reminders, smart alarm and notes in a single application.<br><br>The reminder can be set along with the location of the task. <br><br>The smart alarm can be snoozed only on playing puzzle games or by speaking pre set voice commands.<br><br>In the notes feature, the notes can be saved and shared on social network sites.";

        String youtContentStr = String.valueOf(Html
                .fromHtml("<![CDATA[<body style=\"text-align:justify;color:#000000; \">"
                        + myData
                        + "</body>]]>"));


        WebView webView = (WebView) view.findViewById(R.id.webview1);
        webView.loadData(youtContentStr, "text/html", "utf-8");
        Log.d("Events", "fragment" + view);
        return view;
    }
}