package com.chinamobile.shop.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yjj on 2017/1/13.
 */

public class Activitycollector {

    public static List<Activity> activities = new ArrayList<>();

    public static void addAcitivity(Activity activity){
        activities.add(activity);
    }

    public static void removeAcitivity(Activity activity){
        activities.remove(activity);
    }

    public static void finishAll(){
       for (Activity activity : activities){
           if (!activity.isFinishing()){
               activity.finish();
           }
       }
    }
}
