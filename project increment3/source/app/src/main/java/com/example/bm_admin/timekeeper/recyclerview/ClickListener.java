package com.example.bm_admin.timekeeper.recyclerview;

import android.view.View;

/**
 * Created by suresh on 10/7/16.
 */
public interface ClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
