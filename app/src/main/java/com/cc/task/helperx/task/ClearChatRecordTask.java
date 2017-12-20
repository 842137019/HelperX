package com.cc.task.helperx.task;

import android.os.Handler;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ProgressBar;

import com.cc.task.helperx.service.HelperQQService;
import com.cc.task.helperx.utils.Constants;
import com.cc.task.helperx.utils.DateUtils;
import com.cc.task.helperx.utils.FileUtils;
import com.cc.task.helperx.utils.LogUtils;
import com.cc.task.helperx.utils.Utils;

/**
 * 清空消息列表
 * Created by fangying on 2017/10/30.
 */

public class ClearChatRecordTask {

    private HelperQQService service;
    private Handler handler;

    public ClearChatRecordTask(HelperQQService service, Handler handler) {
        this.service = service;
        this.handler = handler;
    }

    /***
     *
     * 清空消息列表
     *
     */
    public void emptyRecords() {
        if (!Utils.isTragetActivity(Constants.QQ_HOME_ACTIVITY)) {
            Utils.startPage(Constants.QQ_HOME_ACTIVITY);
            Utils.sleep(5000L);
        }
        AccessibilityNodeInfo nodeInfo = Utils.findViewByDesc(service, "帐户及设置");
        if (nodeInfo != null) {
            Utils.clickCompone(nodeInfo);
            Utils.sleep(2000);

            AccessibilityNodeInfo setting = Utils.findViewByText(service, "设置");
            if (setting != null) {
                Utils.clickCompone(setting);
                Utils.sleep(2000);

                Utils.clickCompone(Utils.findViewByText(service, "聊天记录"));
                Utils.sleep(2000);

                AccessibilityNodeInfo chat = Utils.findViewByTextMatch(service, "同步最近聊天记录至本机");
                if (chat != null) {
                    AccessibilityNodeInfo checkBox = Utils.getChildNode(chat.getParent(), android.widget.CompoundButton.class.getName());
                    if (checkBox != null) {
                        if (checkBox.isChecked()) {
                            Utils.clickComponeNoRecycle(checkBox);
                            Utils.sleep(4000L);
                        }
                    }
                }

                Utils.clickCompone(Utils.findViewByText(service, "清空所有聊天记录"));
                Utils.sleep(1500);

                Utils.clickCompone(Utils.findViewByText(service, "清空所有聊天记录"));
                Utils.sleep(7 * 1000L);

                if (Utils.findViewByType(service, ProgressBar.class.getName()) != null) {
                    Utils.pressBack(service);
                    Utils.sleep(2000L);
                }

                Utils.pressBack(service);
                Utils.sleep(2000);
                Utils.pressBack(service);
                Utils.sleep(2000);
                Utils.pressBack(service);
                Utils.sleep(2000);
                LogUtils.logInfo("清空消息行完成");
                FileUtils.writeFileToSDCard("\t \t 清空消息列表執行完成" + DateUtils.format(System.currentTimeMillis(), DateUtils.DEFAULT_DATE_FORMAT) + "]", "RunTime", "runTimeLog", true, false);
                handler.sendEmptyMessage(1);

            }
        } else {
            LogUtils.logInfo("清空消息列表執行完成");
            FileUtils.writeFileToSDCard("\t \t 清空消息列表執行完成" + DateUtils.format(System.currentTimeMillis(), DateUtils.DEFAULT_DATE_FORMAT) + "]", "RunTime", "runTimeLog", true, true);
            handler.sendEmptyMessage(1);
        }
    }
}
