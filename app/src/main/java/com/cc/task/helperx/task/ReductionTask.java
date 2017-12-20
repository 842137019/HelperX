package com.cc.task.helperx.task;

import android.os.Handler;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.ProgressBar;


import com.cc.task.helperx.entity.TaskEntry;
import com.cc.task.helperx.service.HelperQQService;
import com.cc.task.helperx.utils.Constants;
import com.cc.task.helperx.utils.DateUtils;
import com.cc.task.helperx.utils.FileUtils;
import com.cc.task.helperx.utils.LogUtils;
import com.cc.task.helperx.utils.QQError;
import com.cc.task.helperx.utils.SharePreferencesUtils;
import com.cc.task.helperx.utils.Utils;

import java.io.File;

/**
 * 还原
 * Created by fangying on 2017/9/11.
 */

public class ReductionTask {

    private HelperQQService service;
    private Handler handler;

    public ReductionTask(HelperQQService service, Handler handler) {
        this.handler = handler;
        this.service = service;
    }

    public void ReductionData(TaskEntry entry) {
        if (entry == null) {
            handler.sendEmptyMessage(2);
            return;
        }
        Utils.exitApp(Constants.APP_NAME);
        Utils.sleep(3000L);
        if (Utils.validateIsModify(service, entry)) {
            Utils.openFlyModel(service);
            String qq_sign = entry.getWx_sign();
            String cachePath = "/sdcard" + File.separator + Constants.CACHE_PATH_NAME;
            FileUtils.createDir(cachePath);
            String cacheInfo = cachePath + File.separator + Constants.QQ_INFO;
            FileUtils.createDir(cacheInfo);

            String cacheFile_data = cacheInfo + File.separator + qq_sign + "_data";
            String cacheFile_cvqq = cacheInfo + File.separator + qq_sign + "_cvqq";
            Utils.sleep(3000L);
            if (!FileUtils.fileIsExists(cacheFile_data)) {
                LogUtils.logError("缓存文件不存在:进入登入页面" + qq_sign);
                Utils.clearData();
                //获取新的QQ初始信息
                String new_data = "/sdcard" + File.separator + Constants.CACHE_PATH_NAME + File.separator + "new_data";
                String new_cvqq = "/sdcard" + File.separator + Constants.CACHE_PATH_NAME + File.separator + "new_cvqq";
                Utils.reductionData(new_data, null, new_cvqq);
            } else {
                Utils.clearData();
                Utils.sleep(2000L);
                Utils.reductionData(cacheFile_data, null, cacheFile_cvqq);
            }
//            Utils.sleep(5000L);
            launcherApp(entry);
        } else {
            LogUtils.logError("xposed失效---");
            Utils.rebootSystem(service);
        }
    }

    private void launcherApp(TaskEntry entry) {
        Utils.launcherApp(service, Constants.APP_NAME);
        Utils.sleep(8 * 1000L);
        boolean isfindPb = true;
        int index = 0;
        while (isfindPb) {
            AccessibilityNodeInfo pbInfo = Utils.findViewByType(service, ProgressBar.class.getName());
            if (pbInfo != null) {
                Utils.sleep(3000L);
                index++;
            } else {
                isfindPb = false;
            }

            if (index == 5) {
                Utils.sleep(3000L);
                isfindPb = false;
            }
        }
        LoginTask.loginQQ(service);
        AccessibilityNodeInfo passBtn = Utils.findViewByText(service, Button.class.getName(), "新用户注册");
        if (passBtn != null) {
            LoginTask.login(service, entry, 0);
            boolean isTrue = QQError.validateLogin(service, entry.getWx_sign());
            if (isTrue) {
                reductionOk(entry);
            } else {
                FileUtils.writeFileToSDCard("\t \t 设置飞行模式" + DateUtils.format(System.currentTimeMillis(), DateUtils.DEFAULT_DATE_FORMAT) + "]", "RunTime", "runTimeLog", true, true);
                handler.sendEmptyMessage(2);
            }
        } else {
            reductionOk(entry);
        }
    }

    private void reductionOk(TaskEntry entry) {
        LogUtils.logInfo("還原完成");
        SharePreferencesUtils.getInstance(service).setTimeStamp(entry.getCurrent_time());
        SharePreferencesUtils.getInstance(service).setCurrentTime(entry.getRuntime());
        FileUtils.writeFileToSDCard("\t \t 還原完成" + DateUtils.format(System.currentTimeMillis(), DateUtils.DEFAULT_DATE_FORMAT) + "]", "RunTime", "runTimeLog", true, false);
        handler.sendEmptyMessage(1);
    }
}

