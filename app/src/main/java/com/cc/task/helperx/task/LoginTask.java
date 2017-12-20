package com.cc.task.helperx.task;

import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import com.cc.task.helperx.entity.TaskEntry;
import com.cc.task.helperx.service.HelperQQService;
import com.cc.task.helperx.utils.Constants;
import com.cc.task.helperx.utils.LogUtils;
import com.cc.task.helperx.utils.QQError;
import com.cc.task.helperx.utils.ShellUtils;
import com.cc.task.helperx.utils.Utils;

import java.util.List;


public class LoginTask {

    public static void login(HelperQQService service, TaskEntry entry,int type) {
        if (!TextUtils.isEmpty(entry.getWx_id())) {
            telLogin(service, entry,type);
        }
    }

    public static void accountLogin(HelperQQService service, TaskEntry taskEntry) {
        List<AccessibilityNodeInfo> editors = Utils.findViewListByType(service, EditText.class.getName());
        if (editors != null && editors.size() > 1) {
            String account = taskEntry.getWx_id();
            AccessibilityNodeInfo firstPwd = editors.get(0);
            AccessibilityNodeInfo secondPwd = editors.get(1);
            Utils.inputText(service, firstPwd, account);
            Utils.sleep(3 * 1000L);
            Utils.inputText(service, secondPwd, taskEntry.getWx_pass());
            Utils.sleep(3 * 1000L);
            Utils.clickCompone(Utils.findViewByText(service, Button.class.getName(), "登录"));
        }
    }


    private static void telLogin(HelperQQService service, TaskEntry taskEntry,int type) {
        LogUtils.logError("   进入登陆界面     输入帐密");
        List<AccessibilityNodeInfo> editors = Utils.findViewListByType(service, EditText.class.getName());
        if (editors != null && editors.size() > 1) {
            if (type ==1){
                if (Utils.findViewByDesc(service,"帐号列表")!=null){
                    Utils.clickCompone(Utils.findViewByDesc(service,"帐号列表"));
                    Utils.sleep(2000);

                    Utils.tapScreenXY("640 417");
                    Utils.sleep(2000);

                    if (Utils.findViewByText(service,"确定删除QQ号")!=null){
                        Utils.clickCompone(Utils.findViewByText(service,"删除"));
                        Utils.sleep(5000L);
                    }
                }
            }

            Utils.inputText(service, Utils.findViewByDesc(service, Constants.QQ_LOGIN_ACCOUNT), taskEntry.getWx_id());
            Utils.sleep(1500L);

            Utils.inputText(service, Utils.findViewById(service, Constants.QQ_LOGIN_PASSWORD), taskEntry.getWx_pass());
            Utils.sleep(1500L);

            Utils.clickCompone(Utils.findViewByText(service, Button.class.getName(), Constants.QQ_LOGIN_BTN));
            Utils.sleep(3 * 1000L);
            loginAndVerification(service,taskEntry.getWx_sign());
        }
    }


    public static void loginQQ(HelperQQService service) {
        LogUtils.logInfo("   判断界面  ");
        Utils.sleep(5 * 1000L);
        AccessibilityNodeInfo loginBtn;
        AccessibilityNodeInfo clear = Utils.findViewByText(service, "跳过");
        if (clear != null) {
            LogUtils.i("   有视频播放    ");
            Utils.clickCompone(clear);
            Utils.sleep(1500);
        }
        if (Utils.findViewByTextMatch(service, "新用户") != null) {
            if ((loginBtn = Utils.findViewById(service, "com.tencent.mobileqq:id/btn_login")) != null) {
                Utils.clickCompone(loginBtn);
                Utils.sleep(2 * 1000L);
            }
        }
    }

    private static void loginAndVerification(HelperQQService service,String wxsign) {
        boolean isfind = true;
        int index = 0;
        while (isfind) {
            if (index > 2) {
                isfind = false;
                return;
            }
            AccessibilityNodeInfo logining = Utils.findViewByText(service, "登录中");
            if (logining != null) {
                index++;
                Utils.sleep(2000);
            } else {
                isfind = false;
            }
        }

        Utils.sleep(3000);
        AccessibilityNodeInfo verification = Utils.findViewById(service, "com.tencent.mobileqq:id/ivTitleName");
        if (verification != null && !TextUtils.isEmpty(verification.getText())) {
            String title = verification.getText() + "";
            if (title.equals("验证码")) {
                AccessibilityNodeInfo sur = Utils.findViewByType(service, WebView.class.getName());
                if (sur != null) {
                    LogUtils.logError("   亲爱的用户，我们只想确认你不是机器人。   ");
                    QQError.reportError(wxsign,"-4");
                    String typePhone = Utils.getSystemModel();
                    switch (typePhone) {
                        case Constants.HONOR_PHONE_MODEL:
                            Utils.sleep(1000);
                            LogUtils.logInfo(" 华为----点击验证码输入框 ");
                            Utils.tapScreenXY("420 440");
                            LogUtils.logInfo(" 等待输入验证码 ");
                            Utils.sleep(30000);
                            LogUtils.logInfo(" 等待输入验证码    1   ");

                            Utils.tapScreenXY("354 700");
                            Utils.sleep(2000);
                            break;

                        case Constants.RAMOS_PHONE_MODEL:
                            LogUtils.logInfo(" 蓝魔----点击验证码输入框 ");
                            Utils.sleep(1000);
                            Utils.tapScreenXY("583 630");
                            LogUtils.logInfo(" 等待输入验证码 ");
                            Utils.sleep(30000);
                            LogUtils.logInfo(" 等待输入验证码    1   ");

                            Utils.tapScreenXY("537 1050");
                            Utils.sleep(2000);
                            break;
                    }
                }
            }
        }

        isfind = true;
        while (isfind) {
            AccessibilityNodeInfo logining = Utils.findViewByText(service, "登录中");
            if (logining != null) {
                Utils.sleep(3000);
            } else {
                isfind = false;
            }
        }
        Utils.sleep(3000);
    }
}
