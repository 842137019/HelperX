package com.cc.task.helperx.task;

import android.content.ContentValues;
import android.graphics.Rect;
import android.os.Handler;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.cc.task.helperx.entity.GroupInfo;
import com.cc.task.helperx.entity.MessageEntity;
import com.cc.task.helperx.entity.ReplyMsgInfo;
import com.cc.task.helperx.entity.TaskEntry;
import com.cc.task.helperx.http.HttpHandler;
import com.cc.task.helperx.http.HttpTask;
import com.cc.task.helperx.service.HelperQQService;
import com.cc.task.helperx.utils.Constants;
import com.cc.task.helperx.utils.DateUtils;
import com.cc.task.helperx.utils.FileUtils;
import com.cc.task.helperx.utils.LogUtils;
import com.cc.task.helperx.utils.Utils;

import org.json.JSONException;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 发群消息
 * Created by fangying on 2017/9/29.
 */

public class MassMessageTask {
    private HelperQQService service;
    private Handler handler;
    private static final int SEND_PHOTO = 1;
    private static final int SEND_TEXT = 2;
    private int index = 0;
    private int sendNum = 0;

    public MassMessageTask(HelperQQService service, Handler handler) {
        this.handler = handler;
        this.service = service;
    }

    /***
     * 获取回复数据
     * @param taskEntry
     */
    public void sendTypeMsg(final TaskEntry taskEntry) {
        if (taskEntry != null) {
            LogUtils.logInfo("      请求获取回复数据       ");
            HttpHandler.getqunfaMsg(taskEntry.getWx_sign(), "", new HttpTask.HttpCallback() {
                @Override
                public void onSuccess(String data) {
                    LogUtils.logInfo("data  =" + data);
                    if (!TextUtils.isEmpty(data) && data.length() > 4) {
                        List<MessageEntity> messageEntities = new ArrayList<MessageEntity>();
                        try {
                            org.json.JSONArray jsonArray = new org.json.JSONArray(data);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                org.json.JSONObject jsonObject = jsonArray.getJSONObject(i);
                                MessageEntity messageEntity = new MessageEntity();
                                int type = jsonObject.getInt("type");
                                String sc_id = jsonObject.getString("sc_id");
                                String wx_sign = jsonObject.getString("wx_sign");
                                String text = jsonObject.getString("text");
                                String imgUrl = jsonObject.getString("imgUrl");
                                String weburl = jsonObject.getString("weburl");

                                messageEntity.setImgUrl(imgUrl);
                                messageEntity.setWx_sign(wx_sign);
                                messageEntity.setSc_id(sc_id);
                                messageEntity.setText(text);
                                messageEntity.setType(type);
                                messageEntity.setWeburl(weburl);

                                messageEntities.add(messageEntity);
                                messageEntity = null;
                            }
//                            replyMsg(messageEntities, taskEntry);
                            massGroupMsg(messageEntities, taskEntry.getWx_sign());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        HttpHandler.reportError(taskEntry.getWx_sign(), "获取群发數據为空!");
                        FileUtils.writeFileToSDCard("\t \t 获取群发數據为空!" + DateUtils.format(System.currentTimeMillis(), DateUtils.DEFAULT_DATE_FORMAT) + "]", "RunTime", "runTimeLog", true, false);
                        handler.sendEmptyMessage(1);
                    }
                }

                @Override
                public void onFailure(String errMsg) {
                    LogUtils.logInfo("err  =" + errMsg);
                    HttpHandler.reportError(taskEntry.getWx_sign(), "获取群发數據失败!");
                    FileUtils.writeFileToSDCard("\t \t 获取群发數據失败!" + DateUtils.format(System.currentTimeMillis(), DateUtils.DEFAULT_DATE_FORMAT) + "]", "RunTime", "runTimeLog", true, false);
                    handler.sendEmptyMessage(1);
                }

                @Override
                public void onFinished() {
                }
            });
        }
    }

    public void massGroupMsg(List<MessageEntity> messageEntityList, String wxsign) {
        if (!Utils.isTragetActivity(Constants.QQ_HOME_ACTIVITY)) {
            Utils.startPage(Constants.QQ_HOME_ACTIVITY);
            Utils.sleep(5000L);
        }
        AccessibilityNodeInfo contacts = Utils.findViewByTextMatch(service, "联系人");
        if (contacts == null) {
            FileUtils.writeFileToSDCard("\t \t 联系人空间未找到!" + DateUtils.format(System.currentTimeMillis(), DateUtils.DEFAULT_DATE_FORMAT) + "]", "RunTime", "runTimeLog", true, false);
            handler.sendEmptyMessage(1);
            return;
        }
        Utils.clickCompone(contacts);
        Utils.sleep(2000);

        Utils.clickCompone(Utils.findViewByTextMatch(service, "群"));
        Utils.sleep(4000);
        List<GroupInfo> groupInfos = DataSupport.where(" wxsign = ? and groupType = ? ", wxsign, "success").find(GroupInfo.class);
        LogUtils.logInfo("当前wxsign 的加群个数 = " + groupInfos.size());
        getStartView(messageEntityList, wxsign, groupInfos);
    }

    private void getStartView(List<MessageEntity> messageEntityList, String wxsign, List<GroupInfo> groupInfos) {
        if (groupInfos.size() > 0) {
            if (sendNum < 5 && index < groupInfos.size()) {
                GroupInfo groupInfo = groupInfos.get(index);
//                if (groupInfo.getGroupType().equals("success")) {
                String groupId = groupInfo.getGroupId();
                LogUtils.logInfo("GroupId=" + groupId);
                List<ReplyMsgInfo> replyMsgInfos = DataSupport.where("wxsign= ? and groupId = ?", wxsign, groupId).find(ReplyMsgInfo.class);
                if (replyMsgInfos.size() > 0) {
                    ReplyMsgInfo replyMsgInfo = replyMsgInfos.get(0);
                    String replyTime = replyMsgInfo.getSendmsgTime();
                    LogUtils.logInfo("  replyTime   =  " + replyTime);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
                    String date = sdf.format(new Date());
                    LogUtils.logInfo("  date   =  " + date);
                    if (!replyTime.contains(date)) {
                        if (Utils.startMsgView(service, groupId, "1")) { // 是否打开群
                            LogUtils.logInfo("从数据库进入  " + groupId);
                            getMsgObject(messageEntityList, wxsign, groupInfos, groupId, 0);
                        } else {
                            // 换下一个群id
                            index++;
                            getStartView(messageEntityList, wxsign, groupInfos);
                        }
                    } else {
                        index++;
                        massGroupMsg(messageEntityList, wxsign);
                    }
                } else {
                    if (Utils.startMsgView(service, groupId, "1")) { // 是否打开群
                        LogUtils.logInfo("从数据库进入  " + groupId);
                        getMsgObject(messageEntityList, wxsign, groupInfos, groupId, 0);
                    } else {
                        // 换下一个群id
                        index++;
                        getStartView(messageEntityList, wxsign, groupInfos);
                    }
                }
//                }
            } else {
                FileUtils.writeFileToSDCard("\t \t 加群个数<0!" + DateUtils.format(System.currentTimeMillis(), DateUtils.DEFAULT_DATE_FORMAT) + "]", "RunTime", "runTimeLog", true, false);
                handler.sendEmptyMessage(1);
            }
        } else {
            FileUtils.writeFileToSDCard("\t \t 加群个数已满" + DateUtils.format(System.currentTimeMillis(), DateUtils.DEFAULT_DATE_FORMAT) + "]", "RunTime", "runTimeLog", true, false);
            handler.sendEmptyMessage(1);
        }
    }

    private void getMsgObject(List<MessageEntity> messageEntities, String wxsign, List<GroupInfo> groupInfos, String groupId, int msgLength) {
        if (isfindStringObj()) {
            Utils.sleep(2000);
            AccessibilityNodeInfo groupChat = Utils.findViewByDesc(service, "群资料卡");
            if (groupChat != null) {
                AccessibilityNodeInfo jinyan = Utils.findViewById(service, "com.tencent.mobileqq:id/inputBar");
                if (jinyan != null) {
                    if (jinyan.getChild(0).getText().toString().trim().equals("全员禁言中")) {
                        Utils.pressBack(service);
                        Utils.sleep(2000);
                        index++;
                        getStartView(messageEntities, wxsign, groupInfos);
                    } else {
                        LogUtils.logInfo("没有  全员禁言中");
                        if (messageEntities.size() > 0) {
                            if (msgLength < messageEntities.size()) {
                                MessageEntity messageEntity = messageEntities.get(msgLength);
                                AccessibilityNodeInfo title = Utils.findViewById(service, "com.tencent.mobileqq:id/title");
                                String groupName = title.getText() + "";
                                switch (messageEntity.getType()) {
                                    case SEND_PHOTO:
                                        LogUtils.logInfo("發送圖片  url=" + messageEntity.getImgUrl());
                                        sendAndDownloadImg(wxsign, messageEntities, messageEntity, groupId, groupName);
                                        break;

                                    case SEND_TEXT:
                                        LogUtils.logInfo("發送文本");
                                        Utils.sleep(2000);
                                        sendTextMsg(messageEntities, wxsign, groupInfos, 1, messageEntity.getText(), groupId, groupName);
                                        break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isfindStringObj() {
        Utils.sleep(1000L);
        AccessibilityNodeInfo groupNotice = Utils.findViewByText(service, "群公告");
        if (groupNotice != null) {
            Utils.clickCompone(Utils.findViewByText(service, "我知道了"));
            Utils.sleep(1500);
        }
        // com.tencent.mobileqq:id/dialogText
        AccessibilityNodeInfo info = Utils.findViewByText(service, "该群因涉及违反相关条例，已被永久封停，不能使用。系统将会自动解散该群。");
        if (info != null) {
            // com.tencent.mobileqq:id/dialogRightBtn
            Utils.clickCompone(Utils.findViewByText(service, "我知道了"));
            Utils.sleep(2000L);
            return false;
        }
        return true;
    }

    int size = 0;

    /***
     * 获取下载链接
     * @param wx_sign
     * @param messageEntityList
     * @param messageEntity
     * @param groupName
     */
    private void sendAndDownloadImg(String wx_sign, List<MessageEntity> messageEntityList, MessageEntity messageEntity, String groupId, String groupName) {
        if (!TextUtils.isEmpty(messageEntity.getImgUrl())) {
            List<String> datas = new ArrayList<>();
            String[] imgUrls = messageEntity.getImgUrl().split("@@@");
            size = imgUrls.length;
            LogUtils.logInfo("   imgUrls.length= " + imgUrls.length);
            List<String> urlList = Arrays.asList(imgUrls);
            Utils.sleep(2000);
            downloadImage(wx_sign, messageEntityList, messageEntity, datas, urlList, groupId, groupName, 0);
        }
    }

    /***
     * 下载图片
     * @param wx_sign
     * @param messageEntityList
     * @param messageEntity
     * @param datas
     * @param urlList
     * @param groupName
     * @param exponent
     */
    private void downloadImage(final String wx_sign, final List<MessageEntity> messageEntityList, final MessageEntity messageEntity,
                               final List<String> datas, final List<String> urlList, final String groupId, final String groupName, final int exponent) {
        if (exponent < urlList.size()) {
            String url = urlList.get(exponent);
            Utils.delImageToPhoto();
            LogUtils.logInfo("  下载图片 ");
            HttpTask.getInstance().download(url, new HttpTask.HttpCallback() {
                @Override
                public void onSuccess(String data) {
                    if (!TextUtils.isEmpty(data)) {
                        LogUtils.logInfo("  yu   data     =" + data);
                        File file = new File(data);
                        Utils.saveImageToPhoto(service, file);
                        datas.add(data);
                    }
                }

                @Override
                public void onFailure(String errMsg) {
                    LogUtils.logInfo("  err = " + errMsg);
                }

                @Override
                public void onFinished() {
                    downloadImage(wx_sign, messageEntityList, messageEntity, datas, urlList, groupId, groupName, (exponent + 1));
                }
            });
        } else {
            sendPhoto(messageEntityList, wx_sign, groupId, groupName, 1, datas);
            Utils.sleep(2000);
            sendNum++;
            massGroupMsg(messageEntityList, wx_sign);
        }
    }

    /***
     * 发送图片
     * @param datas
     */
    private void sendPhoto(List<MessageEntity> messageEntities, String wxsign, String groupId, String groupName, int type, List<String> datas) {
        LogUtils.logInfo("     发送图片     datas= " + datas.size());
        if (datas.size() > 0) {
            String phonetype = Utils.getSystemModel();
            AccessibilityNodeInfo groupChat = Utils.findViewByDesc(service, "群资料卡");
            if (groupChat != null) {
                boolean isfind = true;
                int yi = 0;
                while (isfind) {
                    AccessibilityNodeInfo photos = Utils.findViewByText(service, Button.class.getName(), "相册");
                    LogUtils.logInfo("点击相册按钮");
                    if (photos != null) {
                        Utils.clickCompone(photos);
                        Utils.sleep(3000);
                        isfind = false;
                    } else {
                        switch (phonetype) {
                            // 點擊發送圖片圖標
                            case Constants.HONOR_PHONE_MODEL:
                                Utils.tapScreenXY("154 1185");
                                Utils.sleep(3000);
                                break;
                            case Constants.RAMOS_PHONE_MODEL:
                                Utils.tapScreenXY("225 1750");
                                Utils.sleep(3000);
                                break;
                        }
                    }
                    yi++;
                    if (yi == 4) {
                        isfind = false;
                        massGroupMsg(messageEntities, wxsign);
                    }
                }
            }
            AccessibilityNodeInfo camera = Utils.findViewByText(service, "Camera");
            AccessibilityNodeInfo photo = Utils.findViewByText(service, "QQPhoto");
            if (camera != null) {
                Utils.clickComponeByXY(camera);
                Utils.sleep(4000);
                sendPhotoMsg(wxsign, groupId, groupName, type, datas);
            } else if (photo != null) {
                Utils.clickComponeByXY(photo);
                Utils.sleep(4000);
                sendPhotoMsg(wxsign, groupId, groupName, type, datas);
            } else {
                Utils.clickCompone(Utils.findViewByText(service, "取消"));//com.tencent.mobileqq:id/ivTitleBtnRightText
                Utils.sleep(2000);

                Utils.pressBack(service);
                Utils.sleep(2000);
            }
        }
    }

    private void sendPhotoMsg(String sign, String groupId, String groupName, int type, List<String> datas) {
        AccessibilityNodeInfo imges = Utils.findViewById(service, "com.tencent.mobileqq:id/photo_list_gv");
        if (imges != null) {
            LogUtils.logInfo("size = " + imges.getChildCount() + "    ,   ");
            List<AccessibilityNodeInfo> checkes = Utils.findViewListByType(service, "android.widget.CheckBox");
            if (checkes != null && checkes.size() > 0) {
                LogUtils.logInfo("CheckBox size = " + checkes.size() + "    ,   ");
                if (checkes.size() == 1) {
                    Utils.clickCompone(checkes.get(0));
                    Utils.sleep(2000);
                } else {
                    int length = 0;
                    if (size > 0) {
                        length = size;
                    } else {
                        length = checkes.size() - 2;
                    }
                    for (int i = 0; i < length; i++) {
                        Utils.clickCompone(checkes.get(i));
                        Utils.sleep(2000);
                    }
                }
                Utils.clickCompone(Utils.findViewByText(service, "发送"));
                Utils.sleep(1500);

                if (Utils.findViewByTextMatch(service, "无法上传") != null) {
                    Utils.findViewByText(service, "我知道了");
                    Utils.sleep(2000L);

                    Utils.clickCompone(Utils.findViewByText(service, "取消"));//com.tencent.mobileqq:id/ivTitleBtnRightText
                    Utils.sleep(2000);

                    Utils.pressBack(service);
                    Utils.sleep(2000);
                } else {
                    addDataToDB(type, sign, groupId, groupName);
                    getGroupInfo(datas);
                }
            }
        }
    }

    private void getGroupInfo(List<String> datas) {
        AccessibilityNodeInfo groupChat = Utils.findViewByDesc(service, "群资料卡");
        if (groupChat != null) {
            LogUtils.logInfo(" 與群聊天 ");
            Utils.clickCompone(groupChat);
            Utils.sleep(2000);
//
//            List<AccessibilityNodeInfo> list = Utils.findViewListByType(service, TextView.class.getName());
//            if (list.size() > 1) {
//                if ((!TextUtils.isEmpty(list.get(0).getText())) && (!TextUtils.isEmpty(list.get(1).getText()))) {
//                    groupName = list.get(0).getText().toString();
//                    groupId = list.get(1).getText().toString();
//                }
//            }

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
                    Utils.sleep(4000L);
                }
                AccessibilityNodeInfo textBtn = null;
                if ((textBtn = Utils.findViewByTextMatch(service, "群消息提示设置")) != null) {
                    Utils.clickCompone(textBtn);
                    Utils.sleep(2000L);

                    if (Utils.findViewById(service, "com.tencent.mobileqq:id/action_sheet_actionView") != null) {
                        Utils.sleep(2000);
                        if ((textBtn = Utils.findViewByText(service, "屏蔽群消息")) != null) {
                            Utils.clickCompone(textBtn);
                            Utils.sleep(3000L);
                        }
                    }
                }
            }
            //android.widget.CompoundButton
        }
        if (datas != null && datas.size() > 0) {
            FileUtils.deleteFiles(datas);
        }
        LogUtils.logInfo("返回到消息页");
        Utils.pressBack(service);
        Utils.sleep(3000);

//        if (Utils.isTragetActivity(Constants.QQ_GROUP_TROOP)) {
//            Utils.pressBack(service);
//            Utils.sleep(3000);
//        }
//
//        if (Utils.findViewByDesc(service, "群资料卡") != null) {
//            Utils.clickCompone(Utils.findViewByDesc(service, "返回消息"));
//            Utils.sleep(3000L);
//        }

    }

    /***
     *  输入文本并发送
     * @param sendmsg
     */
    private void sendTextMsg(List<MessageEntity> messageEntities, String sign, List<GroupInfo> groupInfos, int type, String sendmsg, String groupId, String groupName) {
        if (!TextUtils.isEmpty(sendmsg)) {
            AccessibilityNodeInfo editText = Utils.findViewByType(service, EditText.class.getName());
            if (editText != null) {
                Utils.componeFocus(editText);
                Utils.sleep(2000L);
                Utils.selectAllText(editText);
                Utils.sleep(2000L);
                Utils.inputText(service, editText, sendmsg);
                Utils.sleep(2000L);
                Utils.clickCompone(Utils.findViewById(service, "com.tencent.mobileqq:id/fun_btn"));
                Utils.sleep(2000L);
                addDataToDB(type, sign, groupId, groupName);
                getGroupInfo(null);
                Utils.pressBack(service);
                Utils.sleep(2000L);
                index++;
                getStartView(messageEntities, sign, groupInfos);
            } else {
                Utils.pressBack(service);
                Utils.sleep(2000);
                index++;
                getStartView(messageEntities, sign, groupInfos);
            }
        } else {
            Utils.pressBack(service);
            Utils.sleep(2000);
        }
    }

    private void addDataToDB(int type, String wxsign, String groupId, String groupName) {
        String name = Utils.getBASE64(groupName);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss ");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String date = formatter.format(curDate);
        LogUtils.logInfo("存入数据库  " + groupName + "    " + groupId + "   " + date);
        ReplyMsgInfo replyMsgInfo = new ReplyMsgInfo();
        replyMsgInfo.setGroupName(name);
        replyMsgInfo.setSendmsgTime(date);
        replyMsgInfo.setMsgType(type);
        replyMsgInfo.setGroupId(groupId);
        replyMsgInfo.setWxsign(wxsign);
        replyMsgInfo.setIsTrue("0");
        List<ReplyMsgInfo> replyMsgInfos = DataSupport.where("wxsign= ? and groupId = ?", wxsign, groupId).find(ReplyMsgInfo.class);
        if (replyMsgInfos.size() != 0) {
            long id = replyMsgInfos.get(0).getId();
            ContentValues values = new ContentValues();
            values.put("sendmsgTime", date);
            values.put("groupName", name);
            DataSupport.update(ReplyMsgInfo.class, values, id);
        } else {
            replyMsgInfo.saveThrows();
        }
    }

}
