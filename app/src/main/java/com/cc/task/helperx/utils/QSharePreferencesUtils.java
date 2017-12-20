package com.cc.task.helperx.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by fangying on 2017/10/18.
 */

public class QSharePreferencesUtils {

    private static final String PREFERENCE_NAME = "timeStamp";
    private static SharedPreferences mSharedPreferences;
    private static QSharePreferencesUtils mPreferenceUtils;
    private static SharedPreferences.Editor editor;
    private static final String SHARED_KEY_TIMESTAMP = "shared_key_timeStamp";
    private static final String SHARED_KEY_CURRENT_TIME = "shared_key_current_time";
    private static final String TASK_TYPE="task_type";
    private static final String GROUP_PARAMETER ="group_parameter";

    public QSharePreferencesUtils(Context context) {
        mSharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
    }

    public static void init(Context context) {
        if (mPreferenceUtils == null) {
            mPreferenceUtils = new QSharePreferencesUtils(context);
        }
    }

    public static QSharePreferencesUtils getInstance() {
        if (mPreferenceUtils == null) {
            throw new RuntimeException("please init first!");
        }

        return mPreferenceUtils;
    }

    public void setTimeStamp(long time) {
        editor.putLong(SHARED_KEY_TIMESTAMP, time);
        editor.commit();
    }

    public long getTimeStamp() {
        return mSharedPreferences.getLong(SHARED_KEY_TIMESTAMP, 0);
    }

    public void setCurrent_Time(long time){
        editor.putLong(SHARED_KEY_CURRENT_TIME,time);
        editor.commit();
    }

    public long getCurrent_Time(){
        return mSharedPreferences.getLong(SHARED_KEY_CURRENT_TIME,0);
    }

    public void setTaskType(String type){
        editor.putString(TASK_TYPE,type);
        editor.commit();
    }

    public String getTaskType(){
        return mSharedPreferences.getString(TASK_TYPE,"");
    }

    public void setGroupParameter(String parameter){
        editor.putString(GROUP_PARAMETER,parameter);
        editor.commit();
    }

    public String getGroupParameter(){
        return mSharedPreferences.getString(GROUP_PARAMETER,"");
    }
}
