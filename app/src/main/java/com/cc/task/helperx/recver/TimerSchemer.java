package com.cc.task.helperx.recver;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;

import com.cc.task.helperx.http.HttpHandler;
import com.cc.task.helperx.http.HttpTask;
import com.cc.task.helperx.task.DelBackTask;
import com.cc.task.helperx.task.TimeTask;
import com.cc.task.helperx.utils.Constants;
import com.cc.task.helperx.utils.FileUtils;
import com.cc.task.helperx.utils.LogUtils;
import com.cc.task.helperx.utils.ShellUtils;
import com.cc.task.helperx.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yzq on 2017/3/6.
 */

public class TimerSchemer extends TimeTask {

    private static final String ACTION_UPDATE_NEW_VERSION = "com.cc.task.UPDATE";

    private final String UPDATE_TOOLS_STEP_TIME = "updateToolsTime";

    private final String DEL_BACK_STEP_TIME = "delBackTime";

    private final long TIME_OUT_STEP = 20; //任务时间

    private Context context;

    private DelBackTask delBackTask;

    public TimerSchemer(Context context) {
        this.context = context;
        delBackTask = new DelBackTask();
    }

    private void timeOutLisener() {
        Object obj = Utils.getObject(Constants.TASK_EXECUTE_TIME);
        if (obj != null) {
            int preExeTime = Integer.valueOf(obj.toString());
            boolean isCheckSend = false;
            Object isCheckObj = Utils.getObject(Constants.KEY_CHECK_SEND);
            if (isCheckObj != null) {
                isCheckSend = Boolean.valueOf(isCheckObj.toString());
            }
            //判断超时
            if (!isCheckSend && preExeTime > TIME_OUT_STEP) {
                LogUtils.logError("任务超时!");
                Utils.reboot();
            }
        }
    }

    private void updateToolsListener() {
        LogUtils.logInfo("检查更新APk");
        Object obj = Utils.getObject(UPDATE_TOOLS_STEP_TIME);
        if (obj != null) {
            long updateToolsTime = Long.valueOf(obj.toString());
            if (System.currentTimeMillis() - updateToolsTime > 5 * 60 * 1000) {
                checkUpdate();
            }
        } else {
            checkUpdate();
        }
    }

    private void checkUpdate() {
        Utils.saveObject(System.currentTimeMillis(), UPDATE_TOOLS_STEP_TIME);
        String macId = FileUtils.readStringToFile(Constants.MAC_ID_FILE);
        if (!TextUtils.isEmpty(macId)) {
            HttpHandler.requestUpdateTools(macId, new HttpTask.HttpCallback() {
                @Override
                public void onSuccess(String data) {
                    if (!TextUtils.isEmpty(data)) {
                        try {
                            JSONObject obj = new JSONObject(data);
                            String versionName = obj.optString("version_id");
                            String downloadUrl = obj.optString("down_url");
                            if (isNeedUpdate("com.android.cc.service", versionName) && !TextUtils.isEmpty(downloadUrl)) {
                                downloadUpdate(downloadUrl);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(String errMsg) {

                }

                @Override
                public void onFinished() {

                }
            });
        }
    }

    private boolean isNeedUpdate(String packageName, String versionName) {
        boolean isTrue = false;
        if (!TextUtils.isEmpty(versionName)) {
            try {
                PackageInfo pi = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
                if (!versionName.equals(pi.versionName)) {
                    isTrue = true;
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                isTrue = true;
            }
        }
        return isTrue;
    }

//    private void updateBrower(Context context){
//        String savePath = "/sdcard" + File.separator + Constants.CACHE_PATH_NAME+ File.separator +"update"+ File.separator + "sogou.apk";
////        if(!Utils.apkIsInstall(context,Constants.BROWER_PKG)){
////            if(!FileUtils.fileIsExists(savePath)) {
////                LogUtils.logInfo("下载浏览器");
////                HttpHandler.requestDowloadBrowser(Constants.BROWER_UPDATE_URL,savePath, new HttpTask.HttpCallback() {
////
////                    @Override
////                    public void onSuccess(String data) {
////                        if (FileUtils.fileIsExists(data)) {
////                            installBrower(data);
////                        }
////                    }
////
////                    @Override
////                    public void onFailure(String errMsg) {
////
////                    }
////
////                    @Override
////                    public void onFinished() {
////
////                    }
////                });
////            }else{
////                LogUtils.logInfo("安装浏览器");
////                installBrower(savePath);
////            }
////        }
//        if(Utils.apkIsInstall(context,Constants.BROWER_PKG)){
//            Utils.uninstallApk("sogou.mobile.explorer");
//            FileUtils.deleteFile(savePath);
//        }
//    }

//    private void installBrower(String data){
//        Utils.installApk(data);
//    }

    private void downloadUpdate(String downloadUrl) {
        HttpHandler.requestDowloadTools(downloadUrl, new HttpTask.HttpCallback() {

            @Override
            public void onSuccess(String data) {
                if (FileUtils.fileIsExists(data)) {
                    exeUpdateToolTask(data);
                }
            }

            @Override
            public void onFailure(String errMsg) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void exeUpdateToolTask(final String path) {
        new Thread() {
            @Override
            public void run() {
                Utils.exitApp("com.android.cc.service");
                exeInstall(path);
            }
        }.start();
    }

    private void exeInstall(String data) {
        String fileName = Utils.getFileNameByUrl(data);
        String model = Build.MODEL;
        String[] commands;
        if (model.contains("SCL")) {
            commands = new String[]{"mount -o remount,rw /system", "rm /system/app/" + fileName, "rm /system/priv-app/" + fileName, "cp " + data + " /system/app/", "chmod 755 /system/app/" + fileName, "pm install -r /system/app/" + fileName, "reboot"};
        } else {
            commands = new String[]{"mount -o remount,rw /system", "rm /system/app/" + fileName, "rm /system/priv-app/" + fileName, "cp " + data + " /system/app/", "chmod 755 /system/app/" + fileName, "reboot"};
        }
        ShellUtils.execCommand(commands, true);

    }

    private void sendTimeBrocast() {
        //发送更新广播
        Intent timeIntent = new Intent(ACTION_UPDATE_NEW_VERSION);
        timeIntent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        context.sendBroadcast(timeIntent);
    }

    @Override
    public void run() {
        LogUtils.logInfo("时间监听");
        Object obj = Utils.getObject(Constants.TASK_EXECUTE_TIME);
        if (obj != null) {
            int preExeTime = Integer.valueOf(obj.toString());
            Utils.saveObject((preExeTime + 1), Constants.TASK_EXECUTE_TIME);
        }
        String model = Build.MODEL;
        if (model.contains("SCL")) {
            Utils.openScreen(context);
        }
        sendTimeBrocast();// 时间监听发送广播
//        updateToolsListener();// 下载tools
        timeOutLisener();
        // delBackTask();
        //updateBrower(context);
    }

    private void delBackTask() {
        Object obj = Utils.getObject(DEL_BACK_STEP_TIME);
        if (obj != null) {
            int updateToolsTime = Integer.valueOf(obj.toString());
            if (updateToolsTime == 24 * 60) {
                Utils.saveObject(0, DEL_BACK_STEP_TIME);
                delBackTask.delbackTask();
            } else {
                Utils.saveObject((updateToolsTime + 1), DEL_BACK_STEP_TIME);
            }
        } else {
            Utils.saveObject(1, DEL_BACK_STEP_TIME);
            delBackTask.delbackTask();
        }
    }

}
