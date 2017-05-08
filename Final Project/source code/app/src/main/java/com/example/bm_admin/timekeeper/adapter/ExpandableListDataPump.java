package com.example.bm_admin.timekeeper.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by bm-admin on 27/3/17.
 */
public class ExpandableListDataPump {
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> cricket = new ArrayList<String>();
        cricket.add("27 Mar 2017 10:50 AM, Every \n hour, India");


        List<String> football = new ArrayList<String>();
        football.add("27 Mar 2017 10:50 AM, Every \n hour, India");

        List<String> basketball = new ArrayList<String>();
        basketball.add("27 Mar 2017 10:50 AM, Every \n hour, India");


        expandableListDetail.put("Task 1", cricket);
        expandableListDetail.put("Task 2", football);
        expandableListDetail.put("Task 3", basketball);
        return expandableListDetail;
    }
}