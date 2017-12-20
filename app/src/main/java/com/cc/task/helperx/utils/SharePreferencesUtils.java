package com.cc.task.helperx.utils;

import android.content.Context;

/**
 * Created by fangying on 2017/10/18.
 */

public class SharePreferencesUtils {
    private static SharePreferencesUtils mPreferenceUtils;
    private SharePreferencesUtils() {
    }

    /**
     * 单例模式，获取instance实例
     *
     * @param cxt
     * @return
     */
    public synchronized static SharePreferencesUtils getInstance(Context cxt) {
        if (mPreferenceUtils == null) {
            mPreferenceUtils = new SharePreferencesUtils();
            QSharePreferencesUtils.init(cxt);
        }
        return mPreferenceUtils;
    }

    public void setTimeStamp(long string) {
        QSharePreferencesUtils.getInstance().setTimeStamp(string);
    }

    public long getTimeStamp() {
        return QSharePreferencesUtils.getInstance().getTimeStamp();
    }

    public void setCurrentTime(long str){
        QSharePreferencesUtils.getInstance().setCurrent_Time(str);
    }

    public long getCurrentTime(){
        return QSharePreferencesUtils.getInstance().getCurrent_Time();
    }

    public void setTaskType(String type){
        QSharePreferencesUtils.getInstance().setTaskType(type);
    }

    public String getTaskType(){
        return QSharePreferencesUtils.getInstance().getTaskType();
    }

    public void setGroupParameter(String parameter){
        QSharePreferencesUtils.getInstance().setGroupParameter(parameter);
    }

    public String getGroupParameter(){
        return QSharePreferencesUtils.getInstance().getGroupParameter();
    }
}
