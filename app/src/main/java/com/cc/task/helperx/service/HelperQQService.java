package com.cc.task.helperx.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;

import com.cc.task.helperx.task.TaskController;
import com.cc.task.helperx.utils.Constants;
import com.cc.task.helperx.utils.EventBusUtils;
import com.cc.task.helperx.utils.LogUtils;
import com.cc.task.helperx.utils.Utils;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

/**
 * Created by fangying on 2017/8/30.
 */


public class HelperQQService extends AccessibilityService {
    private TaskController controller;

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.logInfo("辅助服务启动");
        EventBusUtils.getInstance().register(this);
        controller = new TaskController(this);

//        Utils.startMsgView(HelperQQService.this,"69942368","1");
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Utils.saveObject(0, Constants.TASK_EXECUTE_TIME);
        LogUtils.logInfo("辅助服务连接");
    }

    @SuppressLint( "WrongConstant" )
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    @Subscriber( tag = "executeTask" )
    public void executeTask(String type) {
        LogUtils.logInfo("  接收任务编号  " + type);
        controller.executeTask(type);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
    }

    @Override
    public void onInterrupt() {
        EventBus.getDefault().unregister(this);
        LogUtils.logInfo("辅助服务停止");
    }

//    @Override
//    public void onDestroy() {
//        Intent localIntent = new Intent();
//        localIntent.setClass(this, HelperQQService.class); // 销毁时重新启动Service
//        this.startService(localIntent);
//    }
}
