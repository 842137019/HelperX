package com.cc.task.helperx.task;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;

import com.cc.task.helperx.service.HelperQQService;
import com.cc.task.helperx.utils.DateUtils;
import com.cc.task.helperx.utils.FileUtils;
import com.cc.task.helperx.utils.Utils;

/**
 * 修改密码
 * Created by fangying on 2017/12/12.
 */

public class ModifyPasswordTask {
    private HelperQQService service;
    private Handler handler;

    public ModifyPasswordTask(HelperQQService service, Handler handler) {
        this.service = service;
        this.handler = handler;
    }

    public void modifyPassword() {
        AccessibilityNodeInfo accountinfo = Utils.findViewByDesc(service, "帐户及设置");
        if (accountinfo == null) {
            FileUtils.writeFileToSDCard("\t \t 帐户及设置" + DateUtils.format(System.currentTimeMillis(), DateUtils.DEFAULT_DATE_FORMAT) + "]", "RunTime", "runTimeLog", true, false);
            handler.sendEmptyMessage(1);
            return;
        }
        Utils.clickCompone(accountinfo);
        Utils.sleep(3 * 1000L);
        AccessibilityNodeInfo setting = Utils.findViewByText(service, "设置");
        if (setting != null) {
            Utils.clickCompone(setting);
            Utils.sleep(2000);

            Utils.clickCompone(Utils.findViewByTextMatch(service,"帐号、设备安全"));
            Utils.sleep(1500L);

            Utils.clickCompone(Utils.findViewByTextMatch(service,"修改密码"));
            Utils.sleep(5000L);

            String mode = Build.MODEL;
            if (mode.contains("SCL")){
                Utils.tapScreenXY("318 295");
                Utils.sleep(1000L);
                Utils.inputText("");
                Utils.sleep(3000L);
                Utils.tapScreenXY("318 295");
                Utils.sleep(1000L);
                Utils.inputText("");
                Utils.sleep(2000L);
            }else if (mode.contains("MOS")){

            }

            Utils.clickCompone(Utils.findViewByText(service, Button.class.getName(),"确定"));
            Utils.sleep(1500L);
        }
    }
}
