package com.cc.task.helperx.task;

import android.os.Handler;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.TextView;

import com.cc.task.helperx.entity.TaskEntry;
import com.cc.task.helperx.http.HttpHandler;
import com.cc.task.helperx.http.HttpTask;
import com.cc.task.helperx.service.HelperQQService;
import com.cc.task.helperx.utils.Constants;
import com.cc.task.helperx.utils.DateUtils;
import com.cc.task.helperx.utils.FileUtils;
import com.cc.task.helperx.utils.LogUtils;
import com.cc.task.helperx.utils.QQError;
import com.cc.task.helperx.utils.SharePreferencesUtils;
import com.cc.task.helperx.utils.Utils;

import java.io.File;
import java.util.List;

/**
 * 备份
 * Created by fangying on 2017/9/11.
 */

public class BackupsTask {
    private HelperQQService service;
    private Handler handler;

    public BackupsTask(HelperQQService service, Handler handler) {
        this.handler = handler;
        this.service = service;
    }

    public void backups(final TaskEntry entry) {
        if (!Utils.isTragetActivity(Constants.QQ_HOME_ACTIVITY)) {
            Utils.startPage(Constants.QQ_HOME_ACTIVITY);
            Utils.sleep(5000L);
        }
        sleepTime();

        String name = "";
        AccessibilityNodeInfo nodeInfo = Utils.findViewByDesc(service, "帐户及设置");
        if (nodeInfo == null) {
            FileUtils.writeFileToSDCard("\t \t 帐户及设置空间未找到：" + DateUtils.format(System.currentTimeMillis(), DateUtils.DEFAULT_DATE_FORMAT) + "]", "RunTime", "runTimeLog", true, false);
            handler.sendEmptyMessage(1);
            return;
        }
        Utils.clickCompone(nodeInfo);
        Utils.sleep(2000);
        AccessibilityNodeInfo nickName = Utils.findViewById(service, "com.tencent.mobileqq:id/nickname");
        if (nickName != null && !TextUtils.isEmpty(nickName.getText())) {
            name = nickName.getText() + "";
            nickName.recycle();
            LogUtils.logInfo(" nickname=  " + name);
        }
        Utils.clickCompone(Utils.findViewById(service, "com.tencent.mobileqq:id/head_layout"));
        Utils.sleep(3000);
        if (Utils.isTragetActivity(Constants.QQ_USERINFO_ACTIVITY)) {
            List<AccessibilityNodeInfo> list = Utils.findViewListByType(service, TextView.class.getName());
            String nick = "";
            String id = "";
            boolean isfind = true;
            int index = 0;
            while (isfind) {
                if (!TextUtils.isEmpty(list.get(index).getText())) {
                    String nickname = list.get(index).getText().toString();
                    if (nickname.equals(name)) {
                        nick = nickname;
                        id = list.get(index + 2).getText().toString();
                        isfind = false;
                    }
                }
                index++;
            }

            LogUtils.logInfo("  nick = " + nick + " id =" + id);

            Utils.sleep(1000);
            Utils.pressBack(service);
            Utils.sleep(1500L);
            Utils.pressBack(service);
            Utils.sleep(1500L);

            Utils.exitApp(Constants.APP_NAME);
            Utils.sleep(3000L);

            if (entry.getWx_id().equals(id) && Utils.validateIsModify(service, entry)) {
                //备份
                exeBackupOper(entry);
                //上传
                uploadInfo(entry, id, Utils.getDeviceId(service), Utils.getSId(service), "", Utils.getSIM(service));
            } else {
                LogUtils.logError("ID 出错，或设备号不匹配");
                FileUtils.writeFileToSDCard("\t \t ID 出错，或设备号不匹配：" + DateUtils.format(System.currentTimeMillis(), DateUtils.DEFAULT_DATE_FORMAT) + "]", "RunTime", "runTimeLog", true, false);
                handler.sendEmptyMessage(2);
                return;
            }
        }
    }

    private void sleepTime() {
        boolean isrun = true;
        while (isrun) {
            long time = System.currentTimeMillis() / 1000;
            long oldtime = SharePreferencesUtils.getInstance(service).getTimeStamp();
            long currentTime = SharePreferencesUtils.getInstance(service).getCurrentTime();
            Utils.scrollScreenXY("340 417 325 692");
            Utils.sleep(2000L);
            LogUtils.logError(" 等待备份: " + "time=" + time + "  oldtime=" + oldtime + "   currentTime=" + currentTime + "  time - oldtime=" + (time - oldtime));
            if (time - oldtime >= currentTime) {
                SharePreferencesUtils.getInstance(service).setTimeStamp(0);
                SharePreferencesUtils.getInstance(service).setCurrentTime(0);
                isrun = false;
            } else {
                QQError.loginPromptAcitvity(service);
                Utils.sleep(40 * 1000L);
            }
        }
    }

    private void uploadInfo(final TaskEntry entry, String id, String imei, String sid, String mac, String sim) {
        HttpHandler.reportLocalInfo(entry.getWx_sign(), imei, null, sid, sim, id, null, null, null, new HttpTask.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                LogUtils.logInfo("上傳數據完成  " + data);
            }

            @Override
            public void onFailure(String errMsg) {
                LogUtils.logError("请求失败！" + errMsg);
                HttpHandler.reportError(entry.getWx_sign(), "上傳數據失败!");
                FileUtils.writeFileToSDCard("\t \t 上傳數據失败!" + DateUtils.format(System.currentTimeMillis(), DateUtils.DEFAULT_DATE_FORMAT) + "]", "RunTime", "runTimeLog", true, false);
                handler.sendEmptyMessage(2);
            }

            @Override
            public void onFinished() {
                FileUtils.writeFileToSDCard("\t \t 上傳數據完成!" + DateUtils.format(System.currentTimeMillis(), DateUtils.DEFAULT_DATE_FORMAT) + "]", "RunTime", "runTimeLog", true, false);
                handler.sendEmptyMessage(1);
            }
        });
    }

    private void exeBackupOper(final TaskEntry info) {
        new Thread() {
            @Override
            public void run() {
                String qq_sign = info.getWx_sign();
                String cachePath = "/sdcard" + File.separator + Constants.CACHE_PATH_NAME + File.separator + Constants.QQ_INFO;
                FileUtils.createDir(cachePath);
                String backups_data = cachePath + File.separator + qq_sign + "_data";
//                    String backups_imqq = cachePath + File.separator + qq_sign + "_imqq";
                String backups_cvqq = cachePath + File.separator + qq_sign + "_cvqq";
                Utils.backupsData(backups_data, null, backups_cvqq);
            }

        }.start();
    }


}
