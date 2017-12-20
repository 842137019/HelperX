package com.cc.task.helperx.task;

import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.cc.task.helperx.entity.ContactInfo;
import com.cc.task.helperx.entity.TaskEntry;
import com.cc.task.helperx.http.HttpHandler;
import com.cc.task.helperx.http.HttpTask;
import com.cc.task.helperx.service.HelperQQService;
import com.cc.task.helperx.utils.Constants;
import com.cc.task.helperx.utils.LogUtils;
import com.cc.task.helperx.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * 加人
 * <p>
 * Created by fangying on 2017/9/1.
 */

public class AddPeopleTask {
    private HelperQQService service;
    private Handler handler;
    private SQLiteDatabase database;
    private ContactInfo info;
    private String DBname = "1654851405";

    public AddPeopleTask(HelperQQService service, Handler handler) {
        this.service = service;
        this.handler = handler;
        info = new ContactInfo();
    }

//    public void peopleHttp() {
//        HttpHandler.accountInfo(new HttpTask.HttpCallback() {
//            @Override
//            public void onSuccess(String data) {
////                addContacts();
//            }
//
//            @Override
//            public void onFailure(String errMsg) {
//
//            }
//
//            @Override
//            public void onFinished() {
//                addContacts();
//            }
//        });
//    }

    public void addContacts(final TaskEntry taskEntry) {

        HttpHandler.getAddPeople(taskEntry.getWx_sign(), new HttpTask.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                if (!TextUtils.isEmpty(data)) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        String accounts = jsonObject.getString("");
                        String[] account = Utils.splitString(accounts);
                        addPeople(taskEntry,account);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    LogUtils.logInfo("加人数据为空!");
                    HttpHandler.reportError(taskEntry.getWx_sign(), "加人数据为空!");
                    handler.sendEmptyMessage(1);
                }

            }

            @Override
            public void onFailure(String errMsg) {
                LogUtils.logError("加人数据请求失败!!");
                HttpHandler.reportError(taskEntry.getWx_sign(), "加人数据请求失败!");
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void addPeople(TaskEntry taskEntry,String[] strs) {
        if( !Utils.isTragetActivity(Constants.QQ_HOME_ACTIVITY)){
            Utils.startPage(Constants.QQ_HOME_ACTIVITY);
            Utils.sleep(5000L);
        }
        AccessibilityNodeInfo contacts = Utils.findViewByText(service, "联系人");
        if (contacts == null) {
            handler.sendEmptyMessage(1);
            return;
        }
        Utils.clickCompone(contacts);
        Utils.sleep(2000);
        Utils.clickCompone(Utils.findViewById(service, "com.tencent.mobileqq:id/ivTitleBtnRightText"));
        Utils.sleep(2000);
        for (int i = 0; i < strs.length; i++) {
            List<ContactInfo> contactInfos = DataSupport.where("contactId = ?", strs[i]).find(ContactInfo.class);
            if (contactInfos.size() != 0) {
                continue;
            }
            inputText(strs[i]);

//        AccessibilityNodeInfo people = Utils.findViewByNodeId(service, "com.tencent.mobileqq:id/btn_cancel_search");
            AccessibilityNodeInfo people = Utils.findViewByText(service, "搜索");
            if (people == null) {
                Utils.clickCompone(Utils.findViewByText(service, "找人:"));
            } else {
                Utils.clickCompone(people);
            }
            Utils.sleep(8000);
            AccessibilityNodeInfo sousuo = Utils.findViewByText(service, "正在搜索…");
            if (sousuo != null) {
                Utils.pressBack(service);
                Utils.sleep(2000);
                continue;
            }
            AccessibilityNodeInfo nopeople = Utils.findViewByText(service, "没有找到相关结果");
            if (nopeople == null) {
                if (Utils.isTragetActivity(Constants.QQ_USERINFO_ACTIVITY)) {
                    List<AccessibilityNodeInfo> list = Utils.findViewListByType(service, TextView.class.getName());

                    String nickName = list.get(1).getText().toString();
                    LogUtils.logInfo("nickName =" + nickName);

                    AccessibilityNodeInfo add = Utils.findViewByDesc(service, "加好友");
                    if (add != null) {
                        Utils.clickCompone(add);
                        Utils.sleep(2000);
                        info.setContactId(strs[i]);
                        info.setContactName(nickName);
                        info.setWxsign(taskEntry.getWx_sign());
                        info.saveThrows();

                        List<ContactInfo> infos =DataSupport.where("contactId = ?", strs[i]).find(ContactInfo.class);
                        for (int a = 0; a < infos.size(); a++) {
                            LogUtils.logInfo("name = " + infos.get(a).getContactName() + " id =" + infos.get(a).getContactId());
                        }

                        AccessibilityNodeInfo titleName = Utils.findViewById(service, "com.tencent.mobileqq:id/ivTitleName");
                        if (titleName.getText().equals("添加好友")) {
                            Utils.clickCompone(Utils.findViewByType(service, EditText.class.getName()));
                            Utils.sleep(1000);
                            inputText("");
                            Utils.clickCompone(Utils.findViewByText(service, "发送"));
                            Utils.sleep(4000);
                        }
                    }
                }
            }

            Utils.clickCompone(Utils.findViewByText(service, "返回"));
            Utils.sleep(1000);

        }
        AccessibilityNodeInfo qingk = Utils.findViewByDesc(service, "清空");
        if (qingk != null) {
            Utils.clickCompone(qingk);
            Utils.sleep(2000);
        }
        boolean isfind = true;
        while (isfind) {
            AccessibilityNodeInfo cancel = Utils.findViewByText(service, "取消");
            if (cancel != null) {
                Utils.clickCompone(cancel);
                Utils.sleep(2000);
            } else {
                isfind = false;
            }
            Utils.sleep(1000);
        }
        Utils.sleep(2000);
        AccessibilityNodeInfo lianxi;
        if ((lianxi = Utils.findViewByText(service, "联系人")) != null) {
            Utils.clickCompone(lianxi);
            Utils.sleep(1000);
        }

    }

    private void inputText(String str) {
        LogUtils.logInfo("输入内容：" + str);
        AccessibilityNodeInfo et_text = Utils.findViewByType(service, EditText.class.getName());
        if (et_text == null) {
            AccessibilityNodeInfo contacts = Utils.findViewByText(service, "联系人");
            if (contacts != null) {
                Utils.clickCompone(contacts);
                Utils.sleep(2000);
                Utils.clickCompone(Utils.findViewById(service, "com.tencent.mobileqq:id/ivTitleBtnRightText"));
                Utils.sleep(2000);
            }
        }
        if (!TextUtils.isEmpty(str)) {
            Utils.clickCompone(et_text);
            Utils.sleep(2000);

            AccessibilityNodeInfo empty = Utils.findViewByDesc(service, "清空");
            if (empty != null) {
                Utils.clickCompone(empty);
                Utils.sleep(2000);
            }

            Utils.inputText(str);
            Utils.sleep(2000);
        }
    }
}
