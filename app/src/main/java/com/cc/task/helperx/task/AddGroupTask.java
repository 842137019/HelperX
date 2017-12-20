package com.cc.task.helperx.task;

import android.graphics.Rect;
import android.os.Handler;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.cc.task.helperx.entity.GroupInfo;
import com.cc.task.helperx.entity.TaskEntry;
import com.cc.task.helperx.http.HttpHandler;
import com.cc.task.helperx.http.HttpTask;
import com.cc.task.helperx.service.HelperQQService;
import com.cc.task.helperx.utils.Constants;
import com.cc.task.helperx.utils.FileUtils;
import com.cc.task.helperx.utils.LogUtils;
import com.cc.task.helperx.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 加群  969413b2679947e9ed1beaf97064f90c
 * Created by fangying on 2017/9/1.
 */

public class AddGroupTask {
    private HelperQQService service;
    private Handler handler;
    protected String nickName;
//    protected boolean home = true;//判断是否在首页还是加群的输入框页面

    public AddGroupTask(HelperQQService service, Handler handler) {
        this.handler = handler;
        this.service = service;
    }

    public void addGroup(final TaskEntry taskEntry, final int cheack) {

        LogUtils.logInfo("请求获取加群数据  " + taskEntry.getWx_sign());
//        //http://qq.down50.com/weixinoutput/wxapi.php?t=getgroup&str1=a885a65c6701037cbfaa1af547230d23&str2=1
        HttpHandler.getAddGroup(taskEntry.getWx_sign(), new HttpTask.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                if (!TextUtils.isEmpty(data)) {
                    try {
                        JSONObject obj = new JSONObject(data);
                        String groups = obj.optString("group_account");
                        String wordes = obj.optString("word");
                        if (!TextUtils.isEmpty(groups)) {
                            String[] word = Utils.splitString(wordes);
                            String[] group = Utils.splitString(groups);

                            if (!TextUtils.isEmpty(groups)) {
                                addQQGroup(taskEntry, group, word);
                                report(taskEntry.getWx_sign());//加完之后上报数据
                                if (cheack != 1) {
                                    handler.sendEmptyMessage(1);
                                } else {
                                    // 返回主页面 执行发送消息任务
                                    groupList.clear();
                                    if (Utils.findViewByText(service, Button.class.getName(), "取消") != null) {
                                        Utils.clickCompone(Utils.findViewByText(service, Button.class.getName(), "取消"));
                                        Utils.sleep(2000L);
                                    }
                                    if (Utils.findViewByDesc(service, "帐户及设置") == null) {
                                        Utils.pressBack(service);
                                        Utils.sleep(1500L);
                                    }
                                    if (Utils.findViewByDesc(service, "帐户及设置") == null) {
                                        Utils.pressBack(service);
                                        Utils.sleep(1500L);
                                    }
                                    Utils.pressBack(service);
                                    Utils.sleep(1500L);
                                    GroupMembersSendMessagesTask sendMessagesTask = new GroupMembersSendMessagesTask(service, handler);
                                    sendMessagesTask.sendTypeMsg(taskEntry);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (cheack != 1) {
                        LogUtils.logInfo("加群数据为空!");
                        HttpHandler.reportError(taskEntry.getWx_sign(), "加群数据为空!");
                        handler.sendEmptyMessage(1);
                    } else {
                        groupList.clear();
                        if (Utils.findViewByDesc(service, "帐户及设置") == null) {
                            Utils.pressBack(service);
                            Utils.sleep(1500L);
                        }
                        GroupMembersSendMessagesTask sendMessagesTask = new GroupMembersSendMessagesTask(service, handler);
                        sendMessagesTask.sendTypeMsg(taskEntry);
                    }
                }
            }

            @Override
            public void onFailure(String errMsg) {
                if (cheack != 1) {
                    LogUtils.logError("请求失败!!" + errMsg);
                    HttpHandler.reportError(taskEntry.getWx_sign(), "加群数据请求失败!");
                    handler.sendEmptyMessage(1);
                } else {
                    groupList.clear();
                    if (Utils.findViewByDesc(service, "帐户及设置") == null) {
                        Utils.pressBack(service);
                        Utils.sleep(1500L);
                    }
                    GroupMembersSendMessagesTask sendMessagesTask = new GroupMembersSendMessagesTask(service, handler);
                    sendMessagesTask.sendTypeMsg(taskEntry);
                }
            }

            @Override
            public void onFinished() {

            }
        });
    }

    final List<String> groupList = new ArrayList<String>();
    final List<String> groupFailList = new ArrayList<String>();
    final List<String> payGroup = new ArrayList<String>();
    final List<String> AlreadyAddedGroup = new ArrayList<String>();
    final List<String> provingGroup = new ArrayList<String>();
    final List<String> unableAdd = new ArrayList<String>();
    String uinname = null;

    public void addQQGroup(TaskEntry taskEntries, String[] groups, String[] words) {
        if (!Utils.isTragetActivity(Constants.QQ_HOME_ACTIVITY)) {
            Utils.startPage(Constants.QQ_HOME_ACTIVITY);
            Utils.sleep(5000L);
        }
        AccessibilityNodeInfo apply = null;
        LogUtils.logInfo(" 需加群的个数 = " + groups.length);
        for (int i = 0; i < groups.length; i++) {
            if (Utils.findViewByTextMatch(service, "联系人")==null){
                handler.sendEmptyMessage(1);
                return;
            }else {
                Utils.clickCompone(Utils.findViewByTextMatch(service, "联系人"));
                Utils.sleep(1000L);
                Utils.clickCompone(Utils.findViewById(service, "com.tencent.mobileqq:id/ivTitleBtnRightText"));
                Utils.sleep(1500L);
                String groupId = groups[i];
                Utils.clickCompone(Utils.findViewByType(service, EditText.class.getName()));
                Utils.sleep(2000L);
                AccessibilityNodeInfo text = Utils.findViewById(service, "com.tencent.mobileqq:id/ib_clear_text");
                if (text != null) {
                    Utils.clickCompone(text);
                    Utils.sleep(1000L);
                }
                Utils.inputText(groupId);
                Utils.sleep(5000L);
                AccessibilityNodeInfo qun = Utils.findViewByText(service, "找群:");
                AccessibilityNodeInfo findQun = Utils.findViewByDesc(service, "找群:" + groupId);
                if (qun != null || findQun != null) {
                    if (qun == null) {
                        Utils.clickComponeByXY(findQun);
                    } else {
                        Utils.clickComponeByXY(qun);
                    }
                    Utils.sleep(5000L);
                    if (Utils.findViewByType(service, ProgressBar.class.getName()) != null) {
                        unableAdd.add(groupId);
                        Utils.pressBack(service);
                        Utils.sleep(1500L);
                        Utils.pressBack(service);
                        Utils.sleep(1500L);
                    } else if (Utils.findViewByText(service, "正在加载") != null) {
                        unableAdd.add(groupId);
                        Utils.pressBack(service);
                        Utils.sleep(1500L);
                        Utils.pressBack(service);
                        Utils.sleep(1500L);
                    } else if (Utils.findViewByTextMatch(service, "没有找到相关结果") != null) {
                        unableAdd.add(groupId);
                        Utils.pressBack(service);
                        Utils.sleep(1500L);
                        Utils.pressBack(service);
                        Utils.sleep(1500L);
                    } else {
                        if (Utils.isTragetActivity(Constants.QQ_GROUP_TROOP)) {
                            apply = Utils.findViewByType(service, Button.class.getName());
                            if (apply != null) {
                                String btnStr = apply.getText() + "";
                                if (btnStr.contains("支付")) {
                                    //需要钱
                                    paly(groupId, taskEntries.getWx_sign(), "pay", "");
                                    Utils.pressBack(service);
                                    Utils.sleep(1500L);
                                } else if (btnStr.contains("发消息")) {
                                    // 已经加过此群
                                    sendMsg(groupId, taskEntries.getWx_sign());
                                } else if (btnStr.contains("申请加群")) {
                                    List<AccessibilityNodeInfo> list = Utils.findViewListByType(service, TextView.class.getName());
                                    if (list != null && list.size() > 0) {
                                        nickName = list.get(0).getText() + "";
                                    }
                                    applyGroup(apply, groupId, taskEntries.getWx_sign());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /***
     *  返回加群状态
     * @param Wx_sign
     */
    public void report(String Wx_sign) {
        final Map<String, List<String>> listMap = new HashMap<String, List<String>>();
        listMap.put("fail", groupFailList);
        listMap.put("success", groupList);
        listMap.put("pay", payGroup);
        listMap.put("added", AlreadyAddedGroup);
        listMap.put("proving", provingGroup);
        listMap.put("unable", unableAdd);
        // listMap.put("notdata", new ArrayList<>());

        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(JSON.toJSONString(listMap));
        LogUtils.logError(" json string = " + jsonObject.toString());
        HttpHandler.up_deal_group(Wx_sign, jsonObject, new HttpTask.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                LogUtils.logInfo("  Success   data  =" + data);
                groupFailList.clear();
                groupList.clear();
                payGroup.clear();
                AlreadyAddedGroup.clear();
                provingGroup.clear();
                unableAdd.clear();
                listMap.clear();
            }

            @Override
            public void onFailure(String errMsg) {
                groupFailList.clear();
                groupList.clear();
                payGroup.clear();
                AlreadyAddedGroup.clear();
                provingGroup.clear();
                unableAdd.clear();
                listMap.clear();
            }

            @Override
            public void onFinished() {

            }
        });
    }

    protected void paly(String groupId, String sign, String groupType, String troopUin) {
        List<AccessibilityNodeInfo> list = Utils.findViewListByType(service, TextView.class.getName());
        if (list != null && list.size() > 0) {
            String nickName = null;
            if (TextUtils.isEmpty(uinname)) {
                nickName = list.get(0).getText() + "";
            } else {
                nickName = uinname;
            }
            GroupInfo groupInfo = new GroupInfo();
            groupInfo.setGroupId(groupId);
            groupInfo.setGroupName(nickName);
            groupInfo.setGroupNameBase64(Utils.getBASE64(nickName));
            groupInfo.setWxsign(sign);
            groupInfo.setGroupType(groupType);
            if (!TextUtils.isEmpty(troopUin)) {
                groupInfo.setTroop_uin(troopUin);
            }
            groupInfo.saveThrows();
            uinname = "";
            switch (groupType) {
                case "pay": // 支付
                    payGroup.add(groupId);
                    break;
                case "success": // 添加成功
                    groupList.add(groupId);
                    break;
                case "fail": // 添加失败
                    groupFailList.add(groupId);
                    break;
                case "proving": // 验证
                    provingGroup.add(groupId);
                    break;
            }
        }
    }

    protected void sendMsg(String groupId, String sign) {
        List<AccessibilityNodeInfo> list = Utils.findViewListByType(service, TextView.class.getName());
        if (list != null && list.size() > 0) {
            nickName = list.get(0).getText() + "";
            GroupInfo groupInfo = new GroupInfo();
            groupInfo.setGroupName(nickName);
            groupInfo.setGroupNameBase64(Utils.getBASE64(nickName));
            groupInfo.setGroupId(groupId);
            groupInfo.setGroupType("success");
            groupInfo.setWxsign(sign);
            groupInfo.saveThrows();
            AlreadyAddedGroup.add(groupId);
        }
        Utils.pressBack(service);
        Utils.sleep(1500L);
    }

    protected void applyGroup(AccessibilityNodeInfo apply, String groupId, String sign) {
        Utils.clickCompone(apply);
        Utils.sleep(5 * 1000L);
        if (Utils.isTragetActivity(Constants.QQ_GROUP_TROOP)) {
            LogUtils.i("  Utils.isTragetActivity(Constants.QQ_GROUP_TROOP)  ");
            paly(groupId, sign, "fail", "");
            Utils.pressBack(service);
            Utils.sleep(2000);
            Utils.pressBack(service);
            Utils.sleep(2000);
            Utils.pressBack(service);
            Utils.sleep(2000);
        } else if (Utils.isTragetActivity(Constants.ADDGROUP_PROBLEM_SERIFICATION)) {
            LogUtils.i("Utils.isTragetActivity(Constants.ADDGROUP_PROBLEM_SERIFICATION)       ");
            paly(groupId, sign, "proving", "");
            verificationInfo();
        } else {
            //直接进群
            LogUtils.i("    直接进群   ");
//                home = true;
            joingroup(groupId, sign);
        }
    }

    private void joingroup(String groupId, String sign) {
        AccessibilityNodeInfo apply = Utils.findViewByText(service, Button.class.getName(), "申请加群");
        if (apply != null) {
            paly(groupId, sign, "fail", "");
            Utils.pressBack(service);
            Utils.sleep(1500L);
            Utils.pressBack(service);
            Utils.sleep(1500L);
            Utils.pressBack(service);
            Utils.sleep(1500L);
        } else {
            String troopUin = null;
            String path = "/sdcard" + File.separator + Constants.CACHE_PATH_NAME + File.separator + "HookDemo" + File.separator + "getIntent2";
            String intentStr = FileUtils.readStringToFile(path);
            if (!TextUtils.isEmpty(intentStr)) {
                String[] strs = intentStr.split("@@@@@");
                uinname = strs[0];
                troopUin = strs[2];
            }
            AccessibilityNodeInfo groupNotice = Utils.findViewByTextMatch(service, "群公告");
            if (groupNotice != null) {
                Utils.clickCompone(Utils.findViewByTextMatch(service, "我知道了"));
                Utils.sleep(2000);
            }
            AccessibilityNodeInfo groupChat = Utils.findViewByDesc(service, "群资料卡");
            if (groupChat != null) {
                Utils.clickCompone(groupChat);
                Utils.sleep(3000);

                // 判断申请加群或支付按钮是否还存在
                apply = Utils.findViewByType(service, Button.class.getName());
                if (apply != null) {
                    String str = apply.getText()+"";
                    if(str.contains("申请")||str.contains("支付")){
                        paly(groupId, sign, "fail", troopUin);
                    }
                    Utils.pressBack(service);
                    Utils.sleep(1500L);
                    Utils.pressBack(service);
                    Utils.sleep(1500L);
//                home = true;
                } else {
                    AccessibilityNodeInfo pc = null;
                    pc = Utils.findViewByText(service, "电脑端的消息提醒方式将与手机端保持同步。");
                    if (pc != null) {
                        Utils.clickCompone(Utils.findViewByText(service, "我知道了"));
                        Utils.sleep(3000L);
                    }

                    Utils.pressSmallScrollDown();
                    Utils.sleep(4000L);

                    List<AccessibilityNodeInfo> comBtns = Utils.findViewListByType(service, CompoundButton.class.getName());
                    if (comBtns.size() > 0) {
                        AccessibilityNodeInfo comBtn = comBtns.get(0);
                        if (!comBtn.isChecked()) {
                            Rect rect = new Rect();
                            comBtn.getBoundsInScreen(rect);
                            LogUtils.logError("x1:" + rect.left + "y1:" + rect.top + "x2:" + rect.right + "y2:" + rect.bottom);
                            int x = (rect.left + rect.right) / 2;
                            int y = (rect.top + rect.bottom) / 2;
                            Utils.tapScreenXY(x + " " + y);
                            // Utils.sleep(2000L);
                        }
                        Utils.sleep(3000L);
                        pc = Utils.findViewByText(service, "电脑端的消息提醒方式将与手机端保持同步。");
                        if (pc != null) {
                            Utils.clickCompone(Utils.findViewByText(service, "我知道了"));
                            Utils.sleep(3000L);
                        }
                        AccessibilityNodeInfo textBtn = null;
                        if ((textBtn = Utils.findViewByTextMatch(service, "群消息提示设置")) != null) {
                            Utils.clickCompone(textBtn);
                            Utils.sleep(2000L);

                            if (Utils.findViewById(service, "com.tencent.mobileqq:id/action_sheet_actionView") != null) {
                                // Utils.sleep(2000);
                                if ((textBtn = Utils.findViewByText(service, "屏蔽群消息")) != null) {
                                    Utils.clickCompone(textBtn);
                                    Utils.sleep(1500L);
                                }
                            }
                        }
                    }
                    paly(groupId, sign, "success", troopUin);
                }

                AccessibilityNodeInfo pc = Utils.findViewByText(service, "电脑端的消息提醒方式将与手机端保持同步。");
                if (pc != null) {
                    Utils.clickCompone(Utils.findViewByText(service, "我知道了"));
                    Utils.sleep(2000L);
                }
                AccessibilityNodeInfo backBtn = Utils.findViewByDesc(service, "返回按钮");
                if (backBtn != null) {
                    Utils.clickCompone(backBtn);
                    Utils.sleep(1500L);

                    AccessibilityNodeInfo group = Utils.findViewByDesc(service, "群资料卡");
                    if (group != null) {
                        if (Utils.findViewByDesc(service, "返回消息") != null) {
                            Utils.clickCompone(Utils.findViewByDesc(service, "返回消息"));
                        } else {
                            Utils.pressBack(service);
                        }
                        Utils.sleep(2000);
                    }
                }
            }
        }
    }

    protected void verificationInfo() {
        Utils.sleep(2000L);
        AccessibilityNodeInfo info = Utils.findViewByTextMatch(service, "个人介绍");
        if (info != null) {
            Utils.clickCompone(Utils.findViewByText(service, "发送"));
            Utils.sleep(2000L);
            int index = 0;
            boolean loding = true;
            while (loding) {
                if (Utils.findViewByText(service, "发送成功") != null) {
                    Utils.clickCompone(Utils.findViewByText(service, "关闭"));
                    Utils.sleep(2000);
                    loding = false;
                } else {
                    Utils.sleep(3000L);
                    index++;
                }
                if (index > 4) {
                    loding = false;
                }
            }
            Utils.pressBack(service);
            Utils.sleep(2000);
            Utils.pressBack(service);
            Utils.sleep(2000);
        } else {
            Utils.clickCompone(Utils.findViewByText(service, "群资料"));
            Utils.sleep(2000L);
            Utils.pressBack(service);
            Utils.sleep(2000);
            Utils.pressBack(service);
            Utils.sleep(2000);
            Utils.pressBack(service);
            Utils.sleep(2000);
        }
    }
}
