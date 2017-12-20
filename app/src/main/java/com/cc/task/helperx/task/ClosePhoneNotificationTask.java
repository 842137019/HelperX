package com.cc.task.helperx.task;

import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Switch;

import com.cc.task.helperx.service.HelperQQService;
import com.cc.task.helperx.utils.Constants;
import com.cc.task.helperx.utils.DateUtils;
import com.cc.task.helperx.utils.FileUtils;
import com.cc.task.helperx.utils.LogUtils;
import com.cc.task.helperx.utils.Utils;

import java.util.List;

/**
 * 在通知栏关闭QQ通知
 * Created by fangying on 2017/11/17.
 */

public class ClosePhoneNotificationTask {
    private HelperQQService service;
    private Handler handler;

    public ClosePhoneNotificationTask(HelperQQService service, Handler handler) {
        this.service = service;
        this.handler = handler;
    }

    public void setqNotifcation() {
        //if () adb shell am start -n com.android.settings/.SubSettings
        String model = Build.MODEL;
        if (model.contains("SCL")) {
            HorCloseNotification();
        }else if (model.contains("MOS")){
            MosCloseNotification();
        }

        FileUtils.writeFileToSDCard("\t \t 通知栏关闭通知完成"+ DateUtils.format(System.currentTimeMillis(),DateUtils.DEFAULT_DATE_FORMAT)+"]","RunTime","runTimeLog",true,false);
        handler.sendEmptyMessage(1);
    }

    /***
     * 蓝魔
     */
    private void MosCloseNotification() {
        Utils.startPage(Constants.MOS_NOTIFICATION_ACTIVITY);
        Utils.sleep(2000L);
        Utils.clickCompone(Utils.findViewByTextMatch(service,"通知"));
        Utils.sleep(2000L);

        if (Utils.findViewByTextMatch(service,"完全不显示通知")==null){
            Utils.clickCompone(Utils.findViewByTextMatch(service,"设备锁定时"));
            Utils.sleep(3000L);

            Utils.clickCompone(Utils.findViewByTextMatch(service,"完全不显示通知"));
            Utils.sleep(2000L);
        }

        Utils.clickCompone(Utils.findViewByTextMatch(service,"应用通知"));
        Utils.sleep(2000L);

        boolean isfind = true;
        while (isfind) {
            AccessibilityNodeInfo qq = Utils.findViewByTextMatch(service, "QQ");
            if (qq != null) {
                Utils.clickCompone(qq);
                Utils.sleep(3000L);
                isfind = false;
            }else {
                Utils.pressScrollDown();
                Utils.sleep(2000L);
            }
        }

        List<AccessibilityNodeInfo> infos = Utils.findViewListByType(service, Switch.class.getName());
        if (infos != null && infos.size() > 0) {
            AccessibilityNodeInfo btn = null;
            btn = infos.get(0);
            if (!btn.isChecked()) {
                Rect rect = new Rect();
                btn.getBoundsInScreen(rect);
                LogUtils.logError("x1:" + rect.left + "y1:" + rect.top + "x2:" + rect.right + "y2:" + rect.bottom);
                int x = (rect.left + rect.right) / 2;
                int y = (rect.top + rect.bottom) / 2;
                Utils.tapScreenXY(x + " " + y);
                Utils.sleep(2500L);
            }
//            for (int i = 0; i < listnode.size(); i++) {
//                if (i != 1) {
////                    LogUtils.logError(" size =" + listnode.size() + "   i=" + i + "    btn =" + listnode.get(i));
//                    btn = listnode.get(i);
//                    if (btn.isChecked()) {
//                        Rect rect = new Rect();
//                        btn.getBoundsInScreen(rect);
//                        LogUtils.logError("x1:" + rect.left + "y1:" + rect.top + "x2:" + rect.right + "y2:" + rect.bottom);
//                        int x = (rect.left + rect.right) / 2;
//                        int y = (rect.top + rect.bottom) / 2;
//                        Utils.tapScreenXY(x + " " + y);
//                        Utils.sleep(2500L);
//                        btn = null;
//                    }
//                }
//            }
        }
        Utils.pressBack(service);
        Utils.sleep(2000);
        Utils.pressBack(service);
        Utils.sleep(2000);
        Utils.pressBack(service);
        Utils.sleep(2000);
    }

    /***
     * 华为
     */
    private void HorCloseNotification() {
        Utils.startPage(Constants.NOTIFICATION_ACTIVITY);
        boolean isfind = true;
        while (isfind) {
            AccessibilityNodeInfo qq = Utils.findViewByTextMatch(service, "QQ");
            if (qq != null) {
                Utils.clickCompone(qq);
                Utils.sleep(3000L);
                isfind = false;
            }else {
                Utils.pressScrollDown();
                Utils.sleep(2000L);
            }
        }

        List<AccessibilityNodeInfo> infos = Utils.findViewListByType(service, Switch.class.getName());
        if (infos != null && infos.size() > 0) {
            AccessibilityNodeInfo btn = null;
            btn = infos.get(0);
            if (btn.isChecked()) {
                Rect rect = new Rect();
                btn.getBoundsInScreen(rect);
                LogUtils.logError("x1:" + rect.left + "y1:" + rect.top + "x2:" + rect.right + "y2:" + rect.bottom);
                int x = (rect.left + rect.right) / 2;
                int y = (rect.top + rect.bottom) / 2;
                Utils.tapScreenXY(x + " " + y);
                Utils.sleep(2500L);
            }
//            for (int i = 0; i < listnode.size(); i++) {
//                if (i != 1) {
////                    LogUtils.logError(" size =" + listnode.size() + "   i=" + i + "    btn =" + listnode.get(i));
//                    btn = listnode.get(i);
//                    if (btn.isChecked()) {
//                        Rect rect = new Rect();
//                        btn.getBoundsInScreen(rect);
//                        LogUtils.logError("x1:" + rect.left + "y1:" + rect.top + "x2:" + rect.right + "y2:" + rect.bottom);
//                        int x = (rect.left + rect.right) / 2;
//                        int y = (rect.top + rect.bottom) / 2;
//                        Utils.tapScreenXY(x + " " + y);
//                        Utils.sleep(2500L);
//                        btn = null;
//                    }
//                }
//            }
        }

        Utils.pressBack(service);
        Utils.sleep(2000);
        Utils.pressBack(service);
        Utils.sleep(2000);
    }

}
