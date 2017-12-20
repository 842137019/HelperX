package com.cc.task.helperx.task;//package com.cc.helperqq.task;
//
//import android.database.sqlite.SQLiteDatabase;
//import android.graphics.Rect;
//import android.os.Handler;
//import android.text.TextUtils;
//import android.view.accessibility.AccessibilityNodeInfo;
//import android.widget.Button;
//import android.widget.CompoundButton;
//import android.widget.EditText;
//import android.widget.GridView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.cc.helperqq.Entity.GroupInfo;
//import com.cc.helperqq.Entity.MessageEntity;
//import com.cc.helperqq.Entity.ReplyMsgInfo;
//import com.cc.helperqq.Entity.TaskEntry;
//import com.cc.helperqq.http.HttpHandler;
//import com.cc.helperqq.http.HttpTask;
//import com.cc.helperqq.service.HelperQQService;
//import com.cc.helperqq.utils.Constants;
//import com.cc.helperqq.utils.FileUtils;
//import com.cc.helperqq.utils.LogUtils;
//import com.cc.helperqq.utils.Utils;
//
//import org.json.JSONException;
//
//import java.io.File;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.List;
//
///**
// * Created by fangying on 2017/11/2.
// */
//
//public class TestMassMsgTask {
//
//    private HelperQQService service;
//    private Handler handler;
//    private static final int SEND_PHOTO = 1;
//    private static final int SEND_TEXT = 2;
//    private int index = 0;
//    private int isTrue = 0;
//
//    public TestMassMsgTask(HelperQQService service, Handler handler) {
//        this.handler = handler;
//        this.service = service;
//    }
//
//    /***
//     * 获取sqlite 数据
//     * @param taskEntry
//     */
//    public void getSendMassMsgDB(TaskEntry taskEntry) {
//        LogUtils.logInfo("   Wx_sign  =  " + taskEntry.getWx_sign());
//
//        List<ReplyMsgInfo> replyMsgInfos = operator.queryAllReplyMsg(database);
//        LogUtils.logInfo(" replyMsgInfos  size = " + replyMsgInfos.size() + "   version  =" + database.getVersion());
//        for (ReplyMsgInfo replyMsgInfo : replyMsgInfos) {
//            if (!(operator.isDataExists(database, DBConfig.TB_QQGROUP, DBConfig.C_QQGROUP_ID, DBConfig.C_QQGROUP_ID, replyMsgInfo.getMsgid()))) {
//                GroupInfo groupInfo = new GroupInfo();
////                groupInfo.setId();
//                LogUtils.logInfo("  getMsgid  = " + replyMsgInfo.getMsgid() + "   getMsgName = " + replyMsgInfo.getMsgName());
//                LogUtils.logInfo("通过id 添加");
//                groupInfo.setGroupId(replyMsgInfo.getMsgid());
//                groupInfo.setGroupName(replyMsgInfo.getMsgName());
//                groupInfo.setGroupType("success");
//                groupInfo.setGroupIsTrue("0");
//                operator.addGroupData(database, groupInfo);
//            } else if (!(operator.isDataExists(database, DBConfig.TB_QQGROUP, DBConfig.C_QQGROUP_NAME, DBConfig.C_QQGROUP_NAME, replyMsgInfo.getMsgName()))) {
//                LogUtils.logInfo("通过昵称添加");
//                LogUtils.logInfo("  getMsgid  = " + replyMsgInfo.getMsgid() + "   getMsgName = " + replyMsgInfo.getMsgName());
//                GroupInfo groupInfo = new GroupInfo();
//                groupInfo.setGroupId(replyMsgInfo.getMsgid());
//                groupInfo.setGroupName(replyMsgInfo.getMsgName());
//                groupInfo.setGroupType("success");
//                groupInfo.setGroupIsTrue("0");
//                operator.addGroupData(database, groupInfo);
//            }
//        }
//
//        List<GroupInfo> groupInfos = operator.queryAllGroup(database);
//        LogUtils.logInfo("  groupInfos  size = " + groupInfos.size());
//        if (groupInfos.size() > 0 && (isTrue == 0)) {
//            if (index < groupInfos.size()) {
//                String groupId = groupInfos.get(index).getGroupId();
//                String groupName = groupInfos.get(index).getGroupName();
//                String groupType = groupInfos.get(index).getGroupType();
//
//                if (groupType.equals("success")) {
//                    LogUtils.logInfo(" groupId = " + groupId + " groupName=  " + groupName + "   groupType  =" + groupType);
//                    LogUtils.logInfo("   operator.isDataExists(database, DbConfig.TB_REPLYMSG, DbConfig.C_REPLYMSG_ID, DbConfig.C_REPLYMSG_ID, groupId)  =  "
//                            + operator.isDataExists(database, DBConfig.TB_REPLYMSG, DBConfig.C_REPLYMSG_ID, DBConfig.C_REPLYMSG_ID, groupId));
//                    if (operator.isDataExists(database, DBConfig.TB_REPLYMSG, DBConfig.C_REPLYMSG_ID, DBConfig.C_REPLYMSG_ID, groupId)) {
//                        ReplyMsgInfo replyMsgInfo = operator.queryReplyMsg(database, DBConfig.C_REPLYMSG_ID, groupId);
//                        String replyTime = replyMsgInfo.getMsgTime();
////                        LogUtils.logInfo("  replyTime   =  " + replyTime);
//                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss ");
//                        long stopTime = 0;
//                        try {
//                            Date date = sdf.parse(replyTime);
//                            stopTime = date.getTime() / 1000;
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                        long time = System.currentTimeMillis() / 1000;
////                        String date = sdf.format(new java.util.Date());
//                        LogUtils.logInfo("  stopTime   =  " + stopTime + "    time = " + time);
//                        //
//                        if ((time - stopTime) > 83200) {
//                            getMassMsgTask(taskEntry, groupId, groupInfos.get(index), null);
//                        } else {
//                            // 包含则执行下一条数据
//                            index++;
//                            getSendMassMsgDB(taskEntry);
//                        }
//                    } else {
//                        getMassMsgTask(taskEntry, groupId, groupInfos.get(index), null);
//                    }
//                } else {
//                    index++;
//                    getSendMassMsgDB(taskEntry);
//                }
//            } else {
//                LogUtils.logInfo("群发消息執行完成");
//                handler.sendEmptyMessage(1);
//            }
//        } else if ((replyMsgInfos.size()) > 0 && (isTrue == 1)) {
//            isTrue = 1;
//            if (index < replyMsgInfos.size()) {
//                String groupId = replyMsgInfos.get(index).getMsgid();
//                String groupName = replyMsgInfos.get(index).getMsgName();
//
//                LogUtils.logInfo("   operator.isDataExists(database, DbConfig.TB_REPLYMSG, DbConfig.C_REPLYMSG_ID, DbConfig.C_REPLYMSG_ID, groupId)  =  "
//                        + operator.isDataExists(database, DBConfig.TB_REPLYMSG, DBConfig.C_REPLYMSG_ID, DBConfig.C_REPLYMSG_ID, groupId));
//
//                if (operator.isDataExists(database, DBConfig.TB_REPLYMSG, DBConfig.C_REPLYMSG_ID, DBConfig.C_REPLYMSG_ID, groupId)) {
//
//                    ReplyMsgInfo replyMsgInfo = operator.queryReplyMsg(database, DBConfig.C_REPLYMSG_ID, groupId);
//                    String replyTime = replyMsgInfo.getMsgTime();
//                    LogUtils.logInfo("  replyTime   =  " + replyTime);
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss ");
//                    long stopTime = 0;
//                    try {
//                        Date date = sdf.parse(replyTime);
//                        stopTime = date.getTime() / 1000;
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    long time = System.currentTimeMillis() / 1000;
////                        String date = sdf.format(new java.util.Date());
//                    LogUtils.logInfo("  stopTime   =  " + stopTime + "    time = " + time);
//                    // 1509592312 1509536645
//                    if ((time - stopTime) > 432000) {
//                        getMassMsgTask(taskEntry, groupId, null, replyMsgInfos.get(index));
//                    } else {
//                        // 包含则执行下一条数据
//                        index++;
//                        getSendMassMsgDB(taskEntry);
//                    }
//                } else {
//                    // 数据库里不存在则进入聊天页面
//                    LogUtils.logInfo("数据库里不存在该数据");
////                    if (Utils.startMsgView(service, groupId, "1")) {
////                        // 是否进入聊天页面
////                        isEnterChat(taskEntry, groupInfos.get(index), null);
////                    } else {
////                        index++;
////                        getSendMsg(taskEntry);
////                    }
//                    getMassMsgTask(taskEntry, groupId, null, replyMsgInfos.get(index));
//                }
//            } else {
//                LogUtils.logInfo("群发消息執行完成");
//                handler.sendEmptyMessage(1);
//            }
//        } else {
//            getMassMsgTask(taskEntry, "", null, null);
//        }
//    }
//
//    /***
//     *  获取群发数据
//     * @param taskEntry
//     * @param groupId
//     */
//    private void getMassMsgTask(final TaskEntry taskEntry, final String groupId, final GroupInfo groupInfo, final ReplyMsgInfo replyMsgInfo) {
//        LogUtils.logInfo(" wx_sign = " + taskEntry.getWx_sign() + "   groupid = " + groupId);
//        HttpHandler.getqunfaMsg(taskEntry.getWx_sign(), groupId, new HttpTask.HttpCallback() {
//            @Override
//            public void onSuccess(String data) {
//                LogUtils.logInfo("data  =" + data);
//                if (!TextUtils.isEmpty(data) && data.length() > 4) {
//                    List<MessageEntity> messageEntities = new ArrayList<MessageEntity>();
//                    try {
//                        org.json.JSONArray jsonArray = new org.json.JSONArray(data);
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            org.json.JSONObject jsonObject = jsonArray.getJSONObject(i);
//                            MessageEntity messageEntity = new MessageEntity();
//                            int type = jsonObject.getInt("type");
//                            String sc_id = jsonObject.getString("sc_id");
//                            String wx_sign = jsonObject.getString("wx_sign");
//                            String text = jsonObject.getString("text");
//                            String imgUrl = jsonObject.getString("imgUrl");
//                            String weburl = jsonObject.getString("weburl");
//
//                            messageEntity.setImgUrl(imgUrl);
//                            messageEntity.setWx_sign(wx_sign);
//                            messageEntity.setSc_id(sc_id);
//                            messageEntity.setText(text);
//                            messageEntity.setType(type);
//                            messageEntity.setWeburl(weburl);
//
//                            messageEntities.add(messageEntity);
//                            messageEntity = null;
//                        }
//                        if (!TextUtils.isEmpty(groupId)) {
//                            // 通过 intent 进入聊天页面
//                            if (Utils.startMsgView(service, groupId, "1")) {
//                                // 是否进入聊天页面
//                                isEnterChat(taskEntry, groupInfo, replyMsgInfo);
//                            } else {
//                                if (operator.isDataExists(database, DBConfig.TB_QQGROUP, DBConfig.C_QQGROUP_ID, DBConfig.C_QQGROUP_ID, groupId)) {
//                                    GroupInfo groupInfo1 = new GroupInfo();
//                                    groupInfo1.setGroupId(groupId);
//                                    groupInfo1.setGroupIsTrue("1");
//                                    groupInfo1.setGroupType("success");
//                                    groupInfo1.setGroupName(groupInfo.getGroupName());
//
//                                    operator.updateGroupData(database, groupInfo1);
//                                }
//                                index++;
//                                getSendMassMsgDB(taskEntry);
//                            }
//                        } else {
//                            // 通过点击群昵称进入页面  307015335
//                            setGroupName(messageEntities, taskEntry);
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    HttpHandler.reportError(taskEntry.getWx_sign(), "获取群发數據为空!");
//                    handler.sendEmptyMessage(1);
//                }
//            }
//
//            @Override
//            public void onFailure(String errMsg) {
//                LogUtils.logInfo("err  =" + errMsg);
//                HttpHandler.reportError(taskEntry.getWx_sign(), "获取群发數據失败!");
//                handler.sendEmptyMessage(1);
//            }
//
//            @Override
//            public void onFinished() {
//            }
//        });
//    }
//
//    private void setGroupName(List<MessageEntity> messageEntities, TaskEntry taskEntry) {
//        AccessibilityNodeInfo contacts = Utils.findViewByTextMatch(service, "联系人");
//        if (contacts == null) {
//            handler.sendEmptyMessage(1);
//            return;
//        }
//        Utils.clickCompone(contacts);
//        Utils.sleep(2000);
//
//        Utils.clickCompone(Utils.findViewByTextMatch(service, "群"));
//        Utils.sleep(4000);
//
//        List<AccessibilityNodeInfo> listText1 = Utils.findViewListById(service, "com.tencent.mobileqq:id/text1");
//        if (listText1 != null && listText1.size() > 0) {
//            LogUtils.logInfo("   listText1  size   = " + listText1.size());
//            if (index < listText1.size()) {
//                clickGroupName(messageEntities, taskEntry, listText1);
//            } else {
//                listText1.clear();
//                LogUtils.logInfo("群发消息執行完成");
//                handler.sendEmptyMessage(1);
//            }
//        } else {
//            LogUtils.logInfo("群发消息執行完成");
//            handler.sendEmptyMessage(1);
//        }
//    }
//
//    private void clickGroupName(List<MessageEntity> messageEntities, TaskEntry taskEntry, List<AccessibilityNodeInfo> listText) {
//        Utils.sleep(4000);
//        if (Utils.findViewByTextMatch(service, listText.get(index).getText().toString()) == null) {
//            Utils.swipeUp("333 960 338 464");
//            Utils.sleep(3000L);
//        }
//
//        LogUtils.logInfo(" listText1 size = " + listText.size() + "   index = " + index + "    " + listText.get(index).getText().toString().trim());
//        Utils.clickCompone(listText.get(index));
//        Utils.sleep(5000);
//
//        AccessibilityNodeInfo gv = Utils.findViewByType(service, GridView.class.getName());
//        if (gv != null) {
//            Utils.pressBack(service);
//            Utils.sleep(2000);
//        }
//
//        AccessibilityNodeInfo groupNotice = Utils.findViewByText(service, "群公告");
//        if (groupNotice != null) {
//            Utils.clickCompone(Utils.findViewByText(service, "我知道了"));
//            Utils.sleep(3000);
//        }
//        Utils.sleep(2000);
//        AccessibilityNodeInfo groupChat = Utils.findViewByDesc(service, "群资料卡");
//        if (groupChat != null) {
//            AccessibilityNodeInfo jinyan = Utils.findViewById(service, "com.tencent.mobileqq:id/inputBar");
//            if (jinyan != null) {
//                if (jinyan.getChild(0).getText().toString().trim().equals("全员禁言中")) {
//                    Utils.pressBack(service);
//                    Utils.sleep(2000);
//                    index++;
//                    getSendMassMsgDB(taskEntry);
//                } else {
//                    LogUtils.logInfo("没有  全员禁言中");
//                    int msgLength = 0;
//                    SendMsgType(messageEntities, taskEntry, msgLength);
//                }
//            }
//        }
//
//    }
//
//    private void SendMsgType(List<MessageEntity> messageEntities, TaskEntry taskEntry, int msgLength) {
//        if (messageEntities.size() > 0) {
//            if (msgLength < messageEntities.size()) {
//                MessageEntity messageEntity = messageEntities.get(msgLength);
//                AccessibilityNodeInfo title = Utils.findViewById(service, "com.tencent.mobileqq:id/title");
//                String groupName = title.getText().toString().trim();
//                switch (messageEntity.getType()) {
//                    case SEND_PHOTO:
//                        LogUtils.logInfo("發送圖片  url=" + messageEntity.getImgUrl());
//                        sendAndDownloadImg(taskEntry, taskEntry.getWx_sign(), messageEntities, messageEntity, groupName);
//                        break;
//
//                    case SEND_TEXT:
//                        LogUtils.logInfo("發送文本");
//                        List<AccessibilityNodeInfo> list = Utils.findViewListByType(service, RelativeLayout.class.getName());
//                        LogUtils.logInfo("group   %%%%%%%%------%%%%%%%" + list.get(list.size() - 2).getChild(list.get(list.size() - 2).getChildCount() - 1).toString());
//
//                        Utils.sleep(2000);
//                        sendTextMsg(taskEntry, taskEntry.getWx_sign(), messageEntity.getSc_id(), 1, groupName, messageEntity.getText());
////                                    massGroupMsg(messageEntities, taskEntry);
//                        break;
//                }
//                SendMsgType(messageEntities, taskEntry, (msgLength + 1));
//            }
//        }
//    }
//
//    public void getSendMsg(TaskEntry taskEntry) {
//        helper = DBHelper.getDBInstance(service, "/sdcard" + File.separator + Constants.CACHE_PATH_NAME + "/databases/" + taskEntry.getWx_sign() + ".db", null, DBConfig.DB_NEW_VERSION);
//        database = helper.getWritableDatabase();
//        AccessibilityNodeInfo contacts = Utils.findViewByTextMatch(service, "联系人");
//        if (contacts == null) {
//            handler.sendEmptyMessage(1);
//            return;
//        }
//        Utils.clickCompone(contacts);
//        Utils.sleep(2000);
//
//        Utils.clickCompone(Utils.findViewByTextMatch(service, "群"));
//        Utils.sleep(5000);
//
//        List<GroupInfo> groupInfos = operator.queryAllGroup(database);
//        List<ReplyMsgInfo> replyMsgInfos = operator.queryAllReplyMsg(database);
//        if (groupInfos.size() > 0 && (isTrue == 0)) {
//            if (index < groupInfos.size()) {
//                String groupId = groupInfos.get(index).getGroupId();
//                String groupName = groupInfos.get(index).getGroupName();
//                String groupType = groupInfos.get(index).getGroupType();
//                if (groupType.equals("success")) {
//                    LogUtils.logInfo(" groupId = " + groupId + " groupName=  " + groupName + "   groupType  =" + groupType);
//                    LogUtils.logInfo("   operator.isDataExists(database, DbConfig.TB_REPLYMSG, DbConfig.C_REPLYMSG_ID, DbConfig.C_REPLYMSG_ID, groupId)  =  "
//                            + operator.isDataExists(database, DBConfig.TB_REPLYMSG, DBConfig.C_REPLYMSG_ID, DBConfig.C_REPLYMSG_ID, groupId));
//                    if (operator.isDataExists(database, DBConfig.TB_REPLYMSG, DBConfig.C_REPLYMSG_ID, DBConfig.C_REPLYMSG_ID, groupId)) {
//                        ReplyMsgInfo replyMsgInfo = operator.queryReplyMsg(database, DBConfig.C_REPLYMSG_ID, groupId);
//                        String replyTime = replyMsgInfo.getMsgTime();
////                        LogUtils.logInfo("  replyTime   =  " + replyTime);
//                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss ");
//                        long stopTime = 0;
//                        try {
//                            Date date = sdf.parse(replyTime);
//                            stopTime = date.getTime() / 1000;
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                        long time = System.currentTimeMillis() / 1000;
////                        String date = sdf.format(new java.util.Date());
//                        LogUtils.logInfo("  stopTime   =  " + stopTime + "    time = " + time);
//                        // 1509592312 1509536645
//                        if ((time - stopTime) > 43200) {
//                            // 如果不包含进入聊天页面
//                            if (Utils.startMsgView(service, groupId, "1")) {
//                                // 是否进入聊天页面
//                                isEnterChat(taskEntry, groupInfos.get(index), null);
//                            } else {
//                                index++;
//                                getSendMsg(taskEntry);
//                            }
//                        } else {
//                            // 包含则执行下一条数据
//                            index++;
//                            getSendMsg(taskEntry);
//                        }
//                    } else {
//                        // 数据库里不存在则进入聊天页面
//                        LogUtils.logInfo("数据库里不存在该数据");
//                        if (Utils.startMsgView(service, groupId, "1")) {
//                            // 是否进入聊天页面
//                            isEnterChat(taskEntry, groupInfos.get(index), null);
//                        } else {
//                            index++;
//                            getSendMsg(taskEntry);
//                        }
//
//                        // 是否进入聊天页面
////                        isEnterChat(taskEntry, groupInfos.get(index), null);
//                        //下载图片  and  发送消息
//                    }
//                } else {
//                    index++;
//                    getSendMsg(taskEntry);
//                }
//            } else {
//                LogUtils.logInfo("群发消息執行完成");
//                handler.sendEmptyMessage(1);
//            }
//        } else if ((replyMsgInfos.size()) > 0 && (isTrue == 1)) {
//            isTrue = 1;
//            if (index < replyMsgInfos.size()) {
//                String groupId = replyMsgInfos.get(index).getMsgid();
//                String groupName = replyMsgInfos.get(index).getMsgName();
//
//                LogUtils.logInfo("   operator.isDataExists(database, DbConfig.TB_REPLYMSG, DbConfig.C_REPLYMSG_ID, DbConfig.C_REPLYMSG_ID, groupId)  =  "
//                        + operator.isDataExists(database, DBConfig.TB_REPLYMSG, DBConfig.C_REPLYMSG_ID, DBConfig.C_REPLYMSG_ID, groupId));
//
//                if (operator.isDataExists(database, DBConfig.TB_REPLYMSG, DBConfig.C_REPLYMSG_ID, DBConfig.C_REPLYMSG_ID, groupId)) {
//
//                    ReplyMsgInfo replyMsgInfo = operator.queryReplyMsg(database, DBConfig.C_REPLYMSG_ID, groupId);
//                    String replyTime = replyMsgInfo.getMsgTime();
//                    LogUtils.logInfo("  replyTime   =  " + replyTime);
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss ");
//                    long stopTime = 0;
//                    try {
//                        Date date = sdf.parse(replyTime);
//                        stopTime = date.getTime() / 1000;
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    long time = System.currentTimeMillis() / 1000;
////                        String date = sdf.format(new java.util.Date());
//                    LogUtils.logInfo("  stopTime   =  " + stopTime + "    time = " + time);
//                    // 1509592312 1509536645
//                    if ((time - stopTime) > 432000) {
//                        // 如果不包含进入聊天页面
//                        if (Utils.startMsgView(service, groupId, "1")) {
//                            // 是否进入聊天页面
//                            isEnterChat(taskEntry, groupInfos.get(index), null);
//                        } else {
//                            index++;
//                            getSendMsg(taskEntry);
//                        }
//                    } else {
//                        // 包含则执行下一条数据
//                        index++;
//                        getSendMsg(taskEntry);
//                    }
//                } else {
//                    // 数据库里不存在则进入聊天页面
//                    LogUtils.logInfo("数据库里不存在该数据");
//                    if (Utils.startMsgView(service, groupId, "1")) {
//                        // 是否进入聊天页面
//                        isEnterChat(taskEntry, groupInfos.get(index), null);
//                    } else {
//                        index++;
//                        getSendMsg(taskEntry);
//                    }
//                }
//            } else {
//                LogUtils.logInfo("群发消息執行完成");
//                handler.sendEmptyMessage(1);
//            }
//        } else {
//            List<AccessibilityNodeInfo> listText1 = Utils.findViewListById(service, "com.tencent.mobileqq:id/text1");
//            if (listText1 != null && listText1.size() > 0) {
//                LogUtils.logInfo("   listText1  size   = " + listText1.size());
//                if (index < listText1.size()) {
//                    getSendMsg(taskEntry, listText1);
//                } else {
//                    listText1.clear();
//                    LogUtils.logInfo("群发消息執行完成");
//                    handler.sendEmptyMessage(1);
//                }
//            } else {
//                LogUtils.logInfo("群发消息執行完成");
//                handler.sendEmptyMessage(1);
//            }
//        }
//    }
//
//    private void getSendMsg(TaskEntry taskEntry, List<AccessibilityNodeInfo> listText1) {
//        if (!TextUtils.isEmpty(listText1.get(index).getText().toString().trim())) {
//            String strname = listText1.get(index).getText().toString().trim();
////            if (operator.isDataExists(database, DbConfig.TB_REPLYMSG, DbConfig.C_REPLYMSG_NAME, DbConfig.C_REPLYMSG_NAME, strname)) {
////                ReplyMsgInfo replyMsgInfo = operator.queryReplyMsg(database, DbConfig.C_REPLYMSG_NAME, strname);
//////                String replyTime = replyMsgInfo.getMsgTime();
//////                LogUtils.logInfo("  replyTime   =  " + replyTime);
//////                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
//////                String date = sdf.format(new java.util.Date());
//////                LogUtils.logInfo("  date   =  " + date);
//////                if (!replyTime.contains(date)) {
//////                    getMsgObject(messageEntityList, taskEntry, listText1);
//////                } else {
//////                    index++;
//////                    massGroupMsg(messageEntityList, taskEntry);
//////                }
////                String groupId = "";
////                if (replyMsgInfo.getMsgid() != null) {
////                    groupId = replyMsgInfo.getMsgid();
////                } else {
////                    if (operator.isDataExists(database, DbConfig.TB_QQGROUP, DbConfig.C_QQGROUP_NAME, DbConfig.C_QQGROUP_NAME, strname)) {
////                        GroupInfo groupInfo = operator.queryGroup(database, strname);
////                        groupId = groupInfo.getGroupId();
////                    }
////                }
////                String replyTime = replyMsgInfo.getMsgTime();
////                LogUtils.logInfo("  replyTime   =  " + replyTime);
////                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss ");
////                long stopTime = 0;
////                try {
////                    Date date = sdf.parse(replyTime);
////                    stopTime = date.getTime() / 1000;
////                } catch (ParseException e) {
////                    e.printStackTrace();
////                }
////                long time = System.currentTimeMillis() / 1000;
//////                        String date = sdf.format(new java.util.Date());
////                LogUtils.logInfo("  stopTime   =  " + stopTime + "    time = " + time);
////                // 1509592312 1509536645
////                if ((time - stopTime) > 43200) {
////                    // 如果不包含进入聊天页面
////                    if (Utils.startMsgView(service, groupId, "1")) {
////                        // 是否进入聊天页面
////                        isEnterChat(taskEntry, groupInfos.get(index), null);
////                    } else {
////                        index++;
////                        getSendMsg(taskEntry);
////                    }
////                } else {
////                    // 包含则执行下一条数据
////                    index++;
////                    getSendMsg(taskEntry);
////                }
////            } else {
////                getMsgObject(messageEntityList, taskEntry, listText1);
////            }
//
//            getMsgObject(taskEntry, listText1);
//
//        }
//    }
//
//    private void getMsgObject(TaskEntry taskEntry, List<AccessibilityNodeInfo> listText1) {
//        Utils.sleep(4000);
//        if (Utils.findViewByTextMatch(service, listText1.get(index).getText().toString()) == null) {
//            Utils.swipeUp("333 960 338 464");
//            Utils.sleep(3000L);
//        }
//
//        LogUtils.logInfo(" listText1 size = " + listText1.size() + "   index = " + index + "    " + listText1.get(index).getText().toString().trim());
//        Utils.clickCompone(listText1.get(index));
//        Utils.sleep(5000);
//
//        AccessibilityNodeInfo gv = Utils.findViewByType(service, GridView.class.getName());
//        if (gv != null) {
//            Utils.pressBack(service);
//            Utils.sleep(2000);
//        }
//
//        AccessibilityNodeInfo groupNotice = Utils.findViewByText(service, "群公告");
//        if (groupNotice != null) {
//            Utils.clickCompone(Utils.findViewByText(service, "我知道了"));
//            Utils.sleep(3000);
//        }
//        Utils.sleep(2000);
//        AccessibilityNodeInfo groupChat = Utils.findViewByDesc(service, "群资料卡");
//        if (groupChat != null) {
//            AccessibilityNodeInfo jinyan = Utils.findViewById(service, "com.tencent.mobileqq:id/inputBar");
//            if (jinyan != null) {
//                if (jinyan.getChild(0).getText().toString().trim().equals("全员禁言中")) {
//                    Utils.pressBack(service);
//                    Utils.sleep(2000);
//                    index++;
//                    getSendMsg(taskEntry);
//                } else {
//                    LogUtils.logInfo("没有  全员禁言中");
//                    int msgLength = 0;
//                    getSendMsgInfo(taskEntry, "");// 获取聊天内容
////                    if (messageEntityList.size() > 0) {
////                        MessageEntity messageEntity = messageEntityList.get(msgLength);
////                        AccessibilityNodeInfo title = Utils.findViewById(service, "com.tencent.mobileqq:id/title");
////                        String groupName = title.getText().toString().trim();
////                        switch (messageEntity.getType()) {
////                            case SEND_PHOTO:
////                                LogUtils.logInfo("發送圖片  url=" + messageEntity.getImgUrl());
////                                sendAndDownloadImg(taskEntry.getWx_sign(), messageEntityList, messageEntity, taskEntry, groupName);
////                                break;
////
////                            case SEND_TEXT:
////                                LogUtils.logInfo("發送文本");
////                                List<AccessibilityNodeInfo> list = Utils.findViewListByType(service, RelativeLayout.class.getName());
////                                LogUtils.logInfo("group   %%%%%%%%------%%%%%%%" + list.get(list.size() - 2).getChild(list.get(list.size() - 2).getChildCount() - 1).toString());
////
////                                Utils.sleep(2000);
////                                sendTextMsg(taskEntry.getWx_sign(), messageEntity.getSc_id(), 1, groupName, messageEntity.getText());
////                                massGroupMsg(messageEntityList, taskEntry);
////                                break;
////                        }
////                    }
//                }
//            }
//        }
//    }
//
//    private void isEnterChat(TaskEntry taskEntry, GroupInfo groupInfo, ReplyMsgInfo msgInfo) {
//        Utils.sleep(4000L);
//        AccessibilityNodeInfo groupNotice = Utils.findViewByText(service, "群公告");
//        if (groupNotice != null) {
//            Utils.clickCompone(Utils.findViewByText(service, "我知道了"));
//            Utils.sleep(3000);
//        }
//        Utils.sleep(2000);
//        AccessibilityNodeInfo groupChat = Utils.findViewByDesc(service, "群资料卡");
//        if (groupChat != null) {
//            AccessibilityNodeInfo jinyan = Utils.findViewById(service, "com.tencent.mobileqq:id/inputBar");
//            if (jinyan != null) {
//                if (jinyan.getChild(0).getText().toString().trim().equals("全员禁言中")) {
//                    Utils.pressBack(service);
//                    Utils.sleep(2000);
//                    index++;
//                } else {
//                    LogUtils.logInfo("没有  全员禁言中");
//                    int msgLength = 0;
//                    if (groupInfo != null) {
//                        getSendMsgInfo(taskEntry, groupInfo.getGroupId());// 获取聊天内容
//                    } else if (msgInfo != null) {
//                        getSendMsgInfo(taskEntry, msgInfo.getMsgid());
//                    }
////                    if (messageEntityList.size() > 0) {
////                        MessageEntity messageEntity = messageEntityList.get(msgLength);
////                        AccessibilityNodeInfo title = Utils.findViewById(service, "com.tencent.mobileqq:id/title");
////                        String groupName = title.getText().toString().trim();
////                        switch (messageEntity.getType()) {
////                            case SEND_PHOTO:
////                                LogUtils.logInfo("發送圖片  url=" + messageEntity.getImgUrl());
////                                sendAndDownloadImg(taskEntry.getWx_sign(), messageEntityList, messageEntity, taskEntry, groupName);
////                                break;
////
////                            case SEND_TEXT:
////                                LogUtils.logInfo("發送文本");
////                                List<AccessibilityNodeInfo> list = Utils.findViewListByType(service, RelativeLayout.class.getName());
////                                LogUtils.logInfo("group   %%%%%%%%------%%%%%%%" + list.get(list.size() - 2).getChild(list.get(list.size() - 2).getChildCount() - 1).toString());
////
////                                Utils.sleep(2000);
////                                sendTextMsg(taskEntry.getWx_sign(), messageEntity.getSc_id(), 1, groupName, messageEntity.getText());
////                                massGroupMsg(messageEntityList, taskEntry);
////                                break;
////                        }
////                    }
//                }
//            }
//        } else {
//            index++;
//            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss ");
//            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
//            String date = formatter.format(curDate);
//            GroupInfo groupInfo1 = new GroupInfo();
//            ReplyMsgInfo replyMsgInfo = new ReplyMsgInfo();
//            String groupName = "";
//            if (groupInfo == null) {
//                groupName = msgInfo.getMsgName();
//
//                groupInfo1.setGroupId(msgInfo.getMsgid());
//                groupInfo1.setGroupName(groupName);
////                groupInfo1.setGroupType(msgInfo..getGroupType());
////                groupInfo1.setId(groupInfo.getId());
//                groupInfo1.setGroupIsTrue("1");
//
//                replyMsgInfo.setMsgName(groupName);
//                replyMsgInfo.setMsgTime(date);
//                replyMsgInfo.setMsgType(1);
//                replyMsgInfo.setIsTrue("1");
//                replyMsgInfo.setMsgid(msgInfo.getMsgid());
//
//            } else if (msgInfo == null) {
//                groupName = groupInfo.getGroupName();
//                groupInfo1.setGroupId(groupInfo.getGroupId());
//                groupInfo1.setGroupName(groupName);
//                groupInfo1.setGroupType(groupInfo.getGroupType());
////                groupInfo1.setId(groupInfo.getId());
//                groupInfo1.setGroupIsTrue("1");
//
//                replyMsgInfo.setMsgName(groupName);
//                replyMsgInfo.setMsgTime(date);
//                replyMsgInfo.setMsgType(1);
//                replyMsgInfo.setMsgid(groupInfo.getGroupId());
//                replyMsgInfo.setIsTrue("1");
//            }
//            database = helper.getWritableDatabase();
//
//            if (operator.isDataExists(database, DBConfig.TB_QQGROUP, DBConfig.C_QQGROUP_NAME, DBConfig.C_QQGROUP_NAME, groupName)) {
//                operator.updateGroupData(database, groupInfo1);
//            } else {
//                operator.addGroupData(database, groupInfo1);
//            }
//
//            if (operator.isDataExists(database, DBConfig.TB_REPLYMSG, DBConfig.C_REPLYMSG_NAME, DBConfig.C_REPLYMSG_NAME, groupName)) {
//                operator.updateReplyMsgData(database, replyMsgInfo);
//            } else {
//                operator.addReplyMsgData(database, replyMsgInfo);
//            }
//        }
//    }
//
//    int size = 0;
//
//    private void sendPhotoMsg(TaskEntry taskEntry, String sign, String sc_id, int type, String name, List<String> datas) {
//        AccessibilityNodeInfo imges = Utils.findViewById(service, "com.tencent.mobileqq:id/photo_list_gv");
//        if (imges != null) {
//            LogUtils.logInfo("size = " + imges.getChildCount() + "    ,   ");
//            List<AccessibilityNodeInfo> checkes = Utils.findViewListByType(service, "android.widget.CheckBox");
//            if (checkes != null && checkes.size() > 0) {
//                LogUtils.logInfo("CheckBox size = " + checkes.size() + "    ,   ");
//                if (checkes.size() == 1) {
//                    Utils.clickCompone(checkes.get(0));
//                    Utils.sleep(2000);
//                } else {
//                    int length = 0;
//                    if (size > 0) {
//                        length = size;
//                    } else {
//                        length = checkes.size() - 2;
//                    }
//                    for (int i = 0; i < length; i++) {
//                        Utils.clickCompone(checkes.get(i));
//                        Utils.sleep(2000);
//                    }
//                }
//                Utils.clickCompone(Utils.findViewByText(service, "发送"));
//                Utils.sleep(1500);
//            }
//            if (Utils.findViewByTextMatch(service, "无法上传") != null) {
//                Utils.findViewByText(service, "我知道了");
//                Utils.sleep(2000L);
//
//                Utils.clickCompone(Utils.findViewByText(service, "取消"));//com.tencent.mobileqq:id/ivTitleBtnRightText
//                Utils.sleep(2000);
//
//                Utils.pressBack(service);
//                Utils.sleep(2000);
//            } else {
//                getGroupInfo(taskEntry, sign, sc_id, type, name, datas);
//            }
//        }
//    }
//
//    private void getGroupInfo(TaskEntry taskEntry, String sign, String sc_id, int type, String name, List<String> datas) {
//        String groupName = null;
//        String groupId = null;
//        AccessibilityNodeInfo groupChat = Utils.findViewByDesc(service, "群资料卡");
//        if (groupChat != null) {
//            LogUtils.logInfo(" 與群聊天 ");
//            Utils.clickCompone(groupChat);
//            Utils.sleep(2000);
//
//            List<AccessibilityNodeInfo> list = Utils.findViewListByType(service, TextView.class.getName());
//            if (list.size() > 1) {
//                if ((!TextUtils.isEmpty(list.get(0).getText())) && (!TextUtils.isEmpty(list.get(1).getText()))) {
//                    groupName = list.get(0).getText().toString();
//                    groupId = list.get(1).getText().toString();
//                }
//            }
//
//            List<AccessibilityNodeInfo> comBtns = Utils.findViewListByType(service, CompoundButton.class.getName());
//            if (comBtns.size() > 0) {
//                AccessibilityNodeInfo comBtn = comBtns.get(0);
//                if (!comBtn.isChecked()) {
//                    Rect rect = new Rect();
//                    comBtn.getBoundsInScreen(rect);
//                    LogUtils.logError("x1:" + rect.left + "y1:" + rect.top + "x2:" + rect.right + "y2:" + rect.bottom);
//                    int x = (rect.left + rect.right) / 2;
//                    int y = (rect.top + rect.bottom) / 2;
//                    Utils.tapScreenXY(x + " " + y);
//                    Utils.sleep(4000L);
//                }
//                AccessibilityNodeInfo textBtn = null;
//                if ((textBtn = Utils.findViewByTextMatch(service, "群消息提示设置")) != null) {
//                    Utils.clickCompone(textBtn);
//                    Utils.sleep(2000L);
//
//                    if (Utils.findViewById(service, "com.tencent.mobileqq:id/action_sheet_actionView") != null) {
//                        Utils.sleep(2000);
//                        if ((textBtn = Utils.findViewByText(service, "屏蔽群消息")) != null) {
//                            Utils.clickCompone(textBtn);
//                            Utils.sleep(3000L);
//                        }
//                    }
//                }
//            }
//            //android.widget.CompoundButton
//        }
//        FileUtils.deleteFiles(datas);
//        LogUtils.logInfo("返回到消息页");
//        Utils.pressBack(service);
//        Utils.sleep(3000);
//
//        if (Utils.isTragetActivity(Constants.QQ_CHATSETTING)) {
//            Utils.pressBack(service);
//            Utils.sleep(3000);
//        }
//
//        if (Utils.findViewByDesc(service, "群资料卡") != null) {
//            Utils.clickCompone(Utils.findViewByDesc(service, "返回消息"));
//            Utils.sleep(3000L);
//        }
//        if (!TextUtils.isEmpty(groupName)) {
//            name = groupName;
//        }
//        addDataToDB(type, name, groupId);
//        HttpHandler.qunfaFinish(sign, sc_id, groupId);
//        index++;
//        getSendMassMsgDB(taskEntry);
//    }
//
//    /***
//     *  输入文本并发送
//     * @param sendmsg
//     */
//    private void sendTextMsg(TaskEntry taskEntry, String sign, String sc_id, int type, String name, String sendmsg) {
//        if (!TextUtils.isEmpty(sendmsg)) {
//            AccessibilityNodeInfo editText = Utils.findViewByType(service, EditText.class.getName());
//            if (editText != null) {
//                Utils.componeFocus(editText);
//                Utils.sleep(2000L);
//                Utils.selectAllText(editText);
//                Utils.sleep(2000L);
//                Utils.inputText(service, editText, sendmsg);
//                Utils.sleep(2000L);
//                Utils.clickCompone(Utils.findViewById(service, "com.tencent.mobileqq:id/fun_btn"));
//                Utils.sleep(3000L);
//                Utils.pressBack(service);
//                Utils.sleep(2000L);
//                getGroupInfo(taskEntry, sign, sc_id, type, name, null);
//            } else {
//                Utils.pressBack(service);
//                Utils.sleep(2000);
//            }
//        } else {
//            Utils.pressBack(service);
//            Utils.sleep(2000);
//
//        }
//    }
//
//    private void addDataToDB(int type, String groupName, String groupId) {
//        String name = Utils.sqliteEscape(groupName);
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss ");
//        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
//        String date = formatter.format(curDate);
//        LogUtils.logInfo("存入数据库  " + groupName + "    " + groupId + "   " + date);
//        List<ReplyMsgInfo> lists = operator.queryAllReplyMsg(database);
//        ReplyMsgInfo replyMsgInfo = new ReplyMsgInfo();
//        replyMsgInfo.setMsgName(name);
//        replyMsgInfo.setMsgTime(date);
//        replyMsgInfo.setMsgType(type);
//        replyMsgInfo.setMsgid(groupId);
//
//        List<GroupInfo> Grouplists = operator.queryAllGroup(database);
//        GroupInfo groupInfo = new GroupInfo();
//        groupInfo.setGroupName(name);
//        groupInfo.setGroupType("success");
//        groupInfo.setGroupId(groupId);
//        groupInfo.setGroupIsTrue("0");
//
//        database = helper.getWritableDatabase();
//        if (operator.isDataExists(database, DBConfig.TB_REPLYMSG, DBConfig.C_REPLYMSG_NAME, DBConfig.C_REPLYMSG_NAME, name)) {
//            operator.updateReplyMsgData(database, replyMsgInfo);
//        } else {
//            replyMsgInfo.setId((lists.size() + 1));
//            operator.addReplyMsgData(database, replyMsgInfo);
//        }
//
//        if (operator.isDataExists(database, DBConfig.TB_QQGROUP, DBConfig.C_QQGROUP_NAME, DBConfig.C_QQGROUP_NAME, groupName)) {
//            operator.updateGroupData(database, groupInfo);
//        } else {
//            groupInfo.setId((Grouplists.size() + 1));
//            operator.addGroupData(database, groupInfo);
//        }
//    }
//
//    public void getSendMsgInfo(final TaskEntry taskEntry, String groupId) {
//        LogUtils.logInfo("   groupId  = " + groupId);
//        HttpHandler.getqunfaMsg(taskEntry.getWx_sign(), groupId, new HttpTask.HttpCallback() {
//            @Override
//            public void onSuccess(String data) {
//                LogUtils.logInfo("data  =" + data);
//                if (!TextUtils.isEmpty(data) && data.length() > 4) {
//                    List<MessageEntity> messageEntities = new ArrayList<MessageEntity>();
//                    try {
//                        org.json.JSONArray jsonArray = new org.json.JSONArray(data);
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            org.json.JSONObject jsonObject = jsonArray.getJSONObject(i);
//                            MessageEntity messageEntity = new MessageEntity();
//                            int type = jsonObject.getInt("type");
//                            String sc_id = jsonObject.getString("sc_id");
//                            String wx_sign = jsonObject.getString("wx_sign");
//                            String text = jsonObject.getString("text");
//                            String imgUrl = jsonObject.getString("imgUrl");
//                            String weburl = jsonObject.getString("weburl");
//
//                            messageEntity.setImgUrl(imgUrl);
//                            messageEntity.setWx_sign(wx_sign);
//                            messageEntity.setSc_id(sc_id);
//                            messageEntity.setText(text);
//                            messageEntity.setType(type);
//                            messageEntity.setWeburl(weburl);
//
//                            messageEntities.add(messageEntity);
//                            messageEntity = null;
//                        }
////                            replyMsg(messageEntities, taskEntry);
////                        massGroupMsg(messageEntities, taskEntry);
//
//                        getSendAndDownladImg(taskEntry, taskEntry.getWx_id(), messageEntities);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    HttpHandler.reportError(taskEntry.getWx_sign(), "获取群发數據为空!");
//
//                    handler.sendEmptyMessage(1);
//                }
//            }
//
//            @Override
//            public void onFailure(String errMsg) {
//                LogUtils.logInfo("err  =" + errMsg);
//                HttpHandler.reportError(taskEntry.getWx_sign(), "获取群发數據失败!");
//                handler.sendEmptyMessage(1);
//            }
//
//            @Override
//            public void onFinished() {
//            }
//        });
//    }
//
//    int msgLength = 0;
//
//    private void getSendAndDownladImg(TaskEntry taskEntry, String wx_sign, List<MessageEntity> messageEntityList) {
//
//        if (messageEntityList.size() > 0) {
//            if (msgLength < messageEntityList.size()) {
//                MessageEntity messageEntity = messageEntityList.get(msgLength);
//                AccessibilityNodeInfo title = Utils.findViewById(service, "com.tencent.mobileqq:id/title");
//                String groupName = title.getText().toString().trim();
//                switch (messageEntity.getType()) {
//                    case SEND_PHOTO:
//                        LogUtils.logInfo("發送圖片  url=" + messageEntity.getImgUrl());
//                        sendAndDownloadImg(taskEntry, wx_sign, messageEntityList, messageEntity, groupName);
//                        break;
//
//                    case SEND_TEXT:
//                        LogUtils.logInfo("發送文本");
//                        List<AccessibilityNodeInfo> list = Utils.findViewListByType(service, RelativeLayout.class.getName());
//                        LogUtils.logInfo("group   %%%%%%%%------%%%%%%%" + list.get(list.size() - 2).getChild(list.get(list.size() - 2).getChildCount() - 1).toString());
//
//                        Utils.sleep(2000);
//                        sendTextMsg(taskEntry, wx_sign, messageEntity.getSc_id(), 1, groupName, messageEntity.getText());
////                        massGroupMsg(messageEntityList, taskEntry);
//                        break;
//                }
//                msgLength++;
//                getSendAndDownladImg(taskEntry, wx_sign, messageEntityList);
//            }
//        }
//    }
//
//    private void sendAndDownloadImg(TaskEntry taskEntry, String wx_sign, List<MessageEntity> messageEntityList, MessageEntity messageEntity, String group) {
//        if (!TextUtils.isEmpty(messageEntity.getImgUrl())) {
//            List<String> datas = new ArrayList<>();
//            String[] imgUrls = messageEntity.getImgUrl().split("@@@");
//            size = imgUrls.length;
//            LogUtils.logInfo("   imgUrls.length= " + imgUrls.length);
//            List<String> urlList = Arrays.asList(imgUrls);
//            Utils.sleep(2000);
//            downloadImage(taskEntry, wx_sign, messageEntityList, messageEntity, datas, urlList, group, 0);
//        }
//    }
//
//    private void downloadImage(final TaskEntry taskEntry, final String wx_sign, final List<MessageEntity> messageEntityList, final MessageEntity messageEntity, final List<String> datas, final List<String> urlList, final String group, final int exponent) {
//        if (exponent < urlList.size()) {
//            String url = urlList.get(exponent);
//            Utils.delImageToPhoto();
//            LogUtils.logInfo("  下载图片 ");
//            HttpTask.getInstance().download(url, new HttpTask.HttpCallback() {
//                @Override
//                public void onSuccess(String data) {
//                    if (!TextUtils.isEmpty(data)) {
//                        LogUtils.logInfo("  yu   data     =" + data);
//                        File file = new File(data);
//                        Utils.saveImageToPhoto(service, file);
//                        datas.add(data);
//                    }
//                }
//
//                @Override
//                public void onFailure(String errMsg) {
//                    LogUtils.logInfo("  err = " + errMsg);
//                }
//
//                @Override
//                public void onFinished() {
//                    downloadImage(taskEntry, wx_sign, messageEntityList, messageEntity, datas, urlList, group, (exponent + 1));
//                }
//            });
//        } else {
//            sendPhoto(taskEntry, messageEntityList, wx_sign, messageEntity.getSc_id(), group, datas);
//            Utils.sleep(2000);
//
//        }
//    }
//
//    private void sendPhoto(TaskEntry taskEntry, List<MessageEntity> messageEntityList, String wx_sign, String sc_id, String group, List<String> datas) {
//        LogUtils.logInfo("     发送图片     datas= " + datas.size());
//        if (datas.size() > 0) {
//            String phonetype = Utils.getSystemModel();
//            AccessibilityNodeInfo groupChat = Utils.findViewByDesc(service, "群资料卡");
//            if (groupChat != null) {
//                boolean isfind = true;
//                int yi = 0;
//                while (isfind) {
//                    AccessibilityNodeInfo photos = Utils.findViewByText(service, Button.class.getName(), "相册");
//                    LogUtils.logInfo("点击相册按钮");
//                    if (photos != null) {
//                        Utils.clickCompone(photos);
//                        Utils.sleep(3000);
//                        isfind = false;
//                    } else {
//                        switch (phonetype) {
//                            // 點擊發送圖片圖標
//                            case Constants.HONOR_PHONE_MODEL:
//                                Utils.tapScreenXY("154 1185");
//                                Utils.sleep(3000);
//                                break;
//                            case Constants.RAMOS_PHONE_MODEL:
//                                Utils.tapScreenXY("225 1750");
//                                Utils.sleep(3000);
//                                break;
//                        }
//                    }
//                    yi++;
//                    if (yi == 4) {
//                        isfind = false;
//                        getSendMsg(taskEntry);
////                        massGroupMsg(messageEntities, taskEntry);
//                    }
//                }
//            }
//            AccessibilityNodeInfo camera = Utils.findViewByText(service, "Camera");
//            AccessibilityNodeInfo photo = Utils.findViewByText(service, "QQPhoto");
//            if (camera != null) {
//                Utils.clickComponeByXY(camera);
//                Utils.sleep(4000);
//                sendPhotoMsg(taskEntry, wx_sign, sc_id, 0, group, datas);
//            } else if (photo != null) {
//                Utils.clickComponeByXY(photo);
//                Utils.sleep(4000);
//                sendPhotoMsg(taskEntry, wx_sign, sc_id, 0, group, datas);
//            } else {
//                Utils.clickCompone(Utils.findViewByText(service, "取消"));//com.tencent.mobileqq:id/ivTitleBtnRightText
//                Utils.sleep(2000);
//
//                Utils.pressBack(service);
//                Utils.sleep(2000);
//            }
//        }
//    }
//
//}
