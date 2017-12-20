package com.cc.task.helperx.task;

import android.content.ContentValues;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.cc.task.helperx.entity.GroupInfo;
import com.cc.task.helperx.entity.GroupMembersInfo;
import com.cc.task.helperx.entity.MembersInfo;
import com.cc.task.helperx.entity.MessageEntity;
import com.cc.task.helperx.entity.TaskEntry;
import com.cc.task.helperx.http.HttpHandler;
import com.cc.task.helperx.http.HttpTask;
import com.cc.task.helperx.service.HelperQQService;
import com.cc.task.helperx.utils.Constants;
import com.cc.task.helperx.utils.FileUtils;
import com.cc.task.helperx.utils.LogUtils;
import com.cc.task.helperx.utils.QQError;
import com.cc.task.helperx.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import org.litepal.crud.callback.SaveCallback;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by fangying on 2017/11/6.
 */

public class GroupMembersSendMessagesTask {

    private HelperQQService service;
    private Handler handler;
    private static final int SEND_PHOTO = 1;
    private static final int SEND_TEXT = 2;
    private int degree = 0;// 发送消息次数

    private int memberIndex = 0;// 取第几的个成员

    private int degree_item_num = 5;// 每个群发的条数
    private int item_num = 0; //临时条数计数

    private int group_send_num = 2;// 要发几个群
    private int groupNum = 0; // 临时取第几的个群
    private int sendOkNum = 0;

    private List<GroupInfo> groupInfos = null;
    private boolean runtrue = false;//判断是否要加群(群不够的时候)
    private TaskEntry taskEntry;

    public GroupMembersSendMessagesTask(HelperQQService service, Handler handler) {
        this.service = service;
        this.handler = handler;
    }

    /***
     * 获取回复数据
     * @param taskEntry
     */
    public void sendTypeMsg(final TaskEntry taskEntry) {
        LogUtils.i("sendTypeMsg");
        if (taskEntry != null) {
            this.taskEntry = taskEntry;
            String groupmembers = "/sdcard" + File.separator + Constants.CACHE_PATH_NAME + File.separator + "GroupMembers" + File.separator + taskEntry.getWx_sign() + ".txt";
            File file = new File(groupmembers);
            if (file.exists()) {
                FileUtils.deleteFile(groupmembers);
            }
            String path = "/sdcard" + File.separator + Constants.CACHE_PATH_NAME + File.separator + "HookDemo" + File.separator + "getIntent2";
            File file1 = new File(path);
            if (file1.exists()) {
                FileUtils.deleteFile(path);
            }
            final String wxsign = taskEntry.getWx_sign();
            HttpHandler.getqunfaMsg(taskEntry.getWx_sign(), "", new HttpTask.HttpCallback() {
                @Override
                public void onSuccess(String data) {
                    LogUtils.logInfo("获取回复数据成功 :" + data);
                    if (!TextUtils.isEmpty(data) && (data.length() > 10)) {
                        List<MessageEntity> messageEntities = new ArrayList<MessageEntity>();
                        try {
                            JSONArray jsonArray = new JSONArray(data);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
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
                                getInputGroupId(messageEntities, wxsign);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            HttpHandler.reportError(taskEntry.getWx_sign(), "回复消息数据解析失败!");
                            handler.sendEmptyMessage(1);
                        }

                    } else {
                        HttpHandler.reportError(taskEntry.getWx_sign(), "获取回复數據为空!");
                        Utils.sleep(1000L);
                        handler.sendEmptyMessage(1);
                    }
                }

                @Override
                public void onFailure(String errMsg) {
                    LogUtils.logInfo("获取回复数据失败err :" + errMsg);
                    HttpHandler.reportError(taskEntry.getWx_sign(), "获取回复數據失败!");
                    handler.sendEmptyMessage(1);
                }

                @Override
                public void onFinished() {
                }
            });
        } else {
            LogUtils.logInfo("taskEntry为空");
            handler.sendEmptyMessage(1);
        }

    }


    private void getInputGroupId(List<MessageEntity> messageEntities, String wxsign) {
        String groupmembers = "/sdcard" + File.separator + Constants.CACHE_PATH_NAME + File.separator + "GroupMembers" + File.separator + wxsign + ".txt";
        File file = new File(groupmembers);
        if (file.exists()) {
            FileUtils.deleteFile(groupmembers);
        }
        if (!Utils.isTragetActivity(Constants.QQ_HOME_ACTIVITY)) {
            Utils.startPage(Constants.QQ_HOME_ACTIVITY);
            Utils.sleep(5000L);
        }
//        if (Utils.findViewByDesc(service, "帐户及设置") == null) {
//            Utils.pressBack(service);
//            Utils.sleep(1500L);
//        }
        if (group_send_num > 0 && degree_item_num > 0) {
            groupInfos = DataSupport.where("wxsign = ? and groupType = ? ", wxsign, "success").find(GroupInfo.class);
            if (group_send_num > groupInfos.size()) {
                group_send_num = groupInfos.size();
                runtrue = true;
            }
            forSendMsg(groupInfos, messageEntities, wxsign);
        } else {
            LogUtils.logInfo("执行完成  group_send_num =   " + group_send_num + "   degree_item_num = " + degree_item_num);
            messageEntities.clear();
            memberIndex = 0;
            degree = 0;
            groupNum = 0;
            handler.sendEmptyMessage(1);
        }
    }

    private void forSendMsg(List<GroupInfo> groupInfos, List<MessageEntity> messageEntities, String wxsign) {
        QQError.validate(service, taskEntry);
        LogUtils.logInfo("degre = " + degree + "   groupNum = " + groupNum + " group_send_num=" + group_send_num + "  degree_item_num=" + degree_item_num + "  groupInfos size = " + groupInfos.size());
        if (degree < (2 * degree_item_num)) {// 2=group_send_num  要发几个群
            if (groupNum < groupInfos.size()) {
                if (item_num > degree_item_num) { //临时发送的条数大于指定需要发送的条数
                    groupNum++; //换群
                    item_num = 0;//初始化0下标
                    forSendMsg(groupInfos, messageEntities, wxsign);
                } else {
                    GroupInfo groupInfo = groupInfos.get(groupNum);
                    String groupId = groupInfo.getGroupId();
                    String groupName = groupInfo.getGroupName();
                    long group_id = groupInfo.getId();
                    String troopUin = groupInfo.getTroop_uin();
                    List<GroupMembersInfo> groupMembers = groupInfo.getGroupMembersInfos();
                    LogUtils.logInfo(" groupId=" + groupId + "  groupName=" + groupName + "  group_id = " + group_id + "  troopUin = " + troopUin + "   groupMembers size = " + groupMembers.size());
                    Utils.sleep(3000L);

                    if (TextUtils.isEmpty(troopUin) || troopUin.equals("null")) {
                        LogUtils.logInfo(" troopUin 不存在 ");
                        AccessibilityNodeInfo editGroupIdInfo = Utils.findViewByDesc(service, "搜索");
                        if (editGroupIdInfo != null) {
                            Utils.clickCompone(editGroupIdInfo);
                            Utils.sleep(2000L);
                            if (Utils.isTragetActivity(Constants.QQ_UNITE_SEARCH_ACTIVITY)) {
                                AccessibilityNodeInfo editGroupId = Utils.findViewByType(service, EditText.class.getName());
                                if (editGroupId != null) {
                                    Utils.clickCompone(editGroupId);
                                    Utils.inputText(groupId);
                                    Utils.sleep(4000L);
                                    AccessibilityNodeInfo title = Utils.findViewByTextMatch(service, "来自:群");
                                    if (title != null) {
                                        Utils.clickComponeByXY(title);
                                        Utils.sleep(3000L);
                                        String path = "/sdcard" + File.separator + Constants.CACHE_PATH_NAME + File.separator + "HookDemo" + File.separator + "getIntent2";
                                        String intentStr = FileUtils.readStringToFile(path);
                                        LogUtils.logInfo(" intentStr = " + intentStr + "  groupId = " + groupId);
                                        if (!TextUtils.isEmpty(intentStr)) {
                                            String[] strs = intentStr.split("@@@@@");
                                            groupName = strs[0];
                                            troopUin = strs[2];
                                            if (!TextUtils.isEmpty(troopUin) || !troopUin.equals("null")) {
                                                GroupInfo info = new GroupInfo();
                                                info.setTroop_uin(troopUin);
                                                info.setGroupName(groupName);
                                                info.update(group_id);
//                                            Utils.pressBack(service);
//                                            Utils.sleep(1500L);
                                                if (groupMembers.size() == 0) {
                                                    LogUtils.i(" 成员为空 ");
                                                    getGropMember(messageEntities, wxsign, groupId, groupInfos, groupMembers, groupName, String.valueOf(group_id));
                                                } else {
                                                    Utils.pressBack(service);
                                                    Utils.sleep(1500L);
                                                    Utils.pressBack(service);
                                                    Utils.sleep(1500L);
                                                    // 跳入成员聊天页面
                                                    List<GroupMembersInfo> membersInfoLists = new ArrayList<>();
                                                    for (GroupMembersInfo membersInfo : groupMembers) {
                                                        if (membersInfo.getSendOrNot().equals("否")) {
                                                            membersInfoLists.add(membersInfo);
                                                        }
                                                    }
                                                    LogUtils.logInfo(" 未发送的数据 = " + membersInfoLists.size());
                                                    if (memberIndex < membersInfoLists.size()) {
                                                        String memberId = membersInfoLists.get(memberIndex).getMemberId();
                                                        String memberName = Utils.getFromBASE64(membersInfoLists.get(memberIndex).getMembersNameBase64());
                                                        long member_id = membersInfoLists.get(memberIndex).getId();
                                                        List<MembersInfo> membersInfoList = DataSupport.where("wxsign = ? and groupId= ?", wxsign, groupId).find(MembersInfo.class);
                                                        int memNum = 0;
                                                        if (membersInfoList.size() != 0) {
                                                            memNum = membersInfoList.size();
                                                        }
                                                        LogUtils.logInfo("已发送条数 = " + memNum);
                                                        // 判断是否发送过
                                                        theTypeofJudgmentAndWhetherItHasBeenSent(messageEntities, wxsign, groupName,
                                                                groupId, memberName, memberId, troopUin, member_id, membersInfoLists.size(), memNum, String.valueOf(group_id));
                                                    } else {
                                                        groupNum++;
                                                        item_num = 0;
                                                        getInputGroupId(messageEntities, wxsign);
                                                    }
                                                }
                                            } else {
                                                LogUtils.i("troop uin 为空  重新获取 ");
                                                Utils.pressBack(service);
                                                Utils.sleep(1500L);
                                                Utils.pressBack(service);
                                                Utils.sleep(1500L);
//                                          getInputGroupId(messageEntities, wxsign);
                                                forSendMsg(groupInfos, messageEntities, wxsign);
                                            }
                                        } else {
                                            // 换下一个群
                                            LogUtils.logInfo("troopUin 为空");
                                            Utils.pressBack(service);
                                            Utils.sleep(1500L);
                                            Utils.pressBack(service);
                                            Utils.sleep(1500L);
                                            item_num = 0;
                                            groupNum++;
                                            AccessibilityNodeInfo clearInfo = Utils.findViewById(service, "com.tencent.mobileqq:id/ib_clear_text");
                                            if (clearInfo != null) {
                                                Utils.clickCompone(clearInfo);
                                                Utils.sleep(1500L);
                                            }
//                                            getInputGroupId(messageEntities, wxsign);
                                            forSendMsg(groupInfos, messageEntities, wxsign);
                                        }
                                    } else {
                                        Utils.pressBack(service);
                                        Utils.sleep(1000L);
                                        Utils.pressBack(service);
                                        Utils.sleep(1500L);
                                        groupInfo.setGroupType("fail");
                                        groupInfo.update(group_id);
                                        groupNum++;
                                        item_num = 0;
                                        forSendMsg(groupInfos, messageEntities, wxsign);
                                    }
                                }
                            }
                        }
                    } else {
                        //troopUin 存在
                        // 判断成员是否为空  不存在进入获取成员
                        if (groupMembers.size() <= 0) {
                            AccessibilityNodeInfo editGroupIdInfo = Utils.findViewByType(service, EditText.class.getName());
                            if (editGroupIdInfo != null) {
                                Utils.clickCompone(editGroupIdInfo);
                                Utils.sleep(2000L);
                                AccessibilityNodeInfo editGroupId = Utils.findViewByType(service, EditText.class.getName());
                                if (editGroupId != null) {
                                    Utils.clickCompone(editGroupId);
                                    Utils.inputText(groupId);
                                    Utils.sleep(4000L);
                                    AccessibilityNodeInfo title = Utils.findViewByTextMatch(service, "来自:群");
                                    if (title != null) {
                                        Utils.clickComponeByXY(title);
                                        Utils.sleep(3000L);
                                        getGropMember(messageEntities, wxsign, groupId, groupInfos, groupMembers, groupName, String.valueOf(group_id));
                                    } else {
                                        Utils.pressBack(service);
                                        Utils.sleep(1000L);
                                        groupInfo.setGroupType("fail");
                                        groupInfo.update(group_id);
                                        groupNum++;
                                        item_num = 0;
                                        forSendMsg(groupInfos, messageEntities, wxsign);
                                    }
                                } else {
                                    Utils.pressBack(service);
                                    Utils.sleep(1000L);
                                    groupNum++;
                                    forSendMsg(groupInfos, messageEntities, wxsign);
                                }
                            } else {
//                                Utils.pressBack(service);
//                                Utils.sleep(1000L);
                                groupNum++;
                                forSendMsg(groupInfos, messageEntities, wxsign);
                            }
                        } else {
                            // 跳入成员聊天页面
                            List<GroupMembersInfo> membersInfoLists = new ArrayList<>();
                            for (GroupMembersInfo membersInfo : groupMembers) {
                                if (membersInfo.getSendOrNot().equals("否")) {
                                    membersInfoLists.add(membersInfo);
                                }
                            }
                            if (membersInfoLists.size() == 0) {
                                groupNum++;
                                item_num = 0;
                                memberIndex = 0;
                                forSendMsg(groupInfos, messageEntities, wxsign);
                            } else {
                                if (memberIndex < membersInfoLists.size()) {
                                    String memberId = membersInfoLists.get(memberIndex).getMemberId();
                                    String memberName = membersInfoLists.get(memberIndex).getMembersName();
                                    long member_id = membersInfoLists.get(memberIndex).getId();
                                    List<MembersInfo> membersInfoList = DataSupport.where("wxsign = ? and groupId= ?", wxsign, groupId).find(MembersInfo.class);
                                    int memNum = 0;
                                    if (membersInfoList.size() != 0) {
                                        memNum = membersInfoList.size();
                                    }
                                    LogUtils.logInfo("已发送条数 = " + memNum);
                                    // 判断是否发送过
                                    theTypeofJudgmentAndWhetherItHasBeenSent(messageEntities, wxsign, groupName, groupId, memberName, memberId, troopUin, member_id, membersInfoLists.size(), memNum, String.valueOf(group_id));
                                } else {
                                    groupNum++;
                                    item_num = 0;
//                                    getInputGroupId(messageEntities, wxsign);
                                    forSendMsg(groupInfos, messageEntities, wxsign);
                                }
                            }
                        }
                    }
                }
            } else {
                messageEntities.clear();
                LogUtils.i("groupNum  " + groupNum + " 大于 " + group_send_num);
                completeSend();
            }
        } else {
            messageEntities.clear();
            LogUtils.i(degree + "  <<<<<< (group_send_num * degree_item_num)");
            completeSend();
        }
    }

    /**
     * 发送消息完成
     */
    private void completeSend() {
        memberIndex = 0;
        degree = 0;
        groupNum = 0;
        if (runtrue) {
        }

        handler.sendEmptyMessage(1);
    }


    /***
     * 获取成员数量
     * @param messageEntities
     * @param wxsign
     * @param groupId
     * @param groupInfos
     * @param groupMembers
     * @param groupName
     * @param group_id
     */
    private void getGropMember(List<MessageEntity> messageEntities, String wxsign, String groupId, List<GroupInfo> groupInfos, List<GroupMembersInfo> groupMembers, String groupName, String group_id) {
        findGroupDataControlAndGroupNickname(groupInfos, messageEntities, wxsign, group_id);
        if (isfindStringObj()) {
            int memberNum = 0;
            // 设置屏蔽群消息
            AccessibilityNodeInfo memberNumstr = Utils.findViewByText(service, "名成员");
            if (memberNumstr != null) {
                String numstr = memberNumstr.getText() + "";
                LogUtils.logInfo("获取成员数量 numstr= " + numstr);
                memberNum = Integer.parseInt(Utils.getNumbers(numstr));
                Utils.clickCompone(memberNumstr);
                boolean isfind = true;
                int i = 0;
                while (isfind) {
                    if (i == 4) {
                        isfind = false;
                    } else {
                        AccessibilityNodeInfo jiazai = Utils.findViewByType(service, ProgressBar.class.getName());
                        if (jiazai != null) {
                            Utils.sleep(2000L);
                            i++;
                        } else {
                            isfind = false;
                        }
                    }
                }
                Utils.sleep(6000L);
                clickIntoGetGroupMembers(messageEntities, wxsign, groupInfos, groupMembers, groupId, groupName, memberNum, 0, group_id);
            }
        } else {
            Utils.pressBack(service);
            Utils.sleep(1500L);
            GroupInfo groupInfo = new GroupInfo();
            groupInfo.setGroupType("fail");
            groupInfo.update(Long.parseLong(group_id));
            groupNum++;
            item_num = 0;
            memberIndex = 0;
            forSendMsg(groupInfos, messageEntities, wxsign);
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

    /**
     * 查找群资料控件和群昵称
     */
    private void findGroupDataControlAndGroupNickname(List<GroupInfo> groupInfos, List<MessageEntity> messageEntities, String wxsign, String group_id) {
        Utils.sleep(3000L);
        String path = "/sdcard" + File.separator + Constants.CACHE_PATH_NAME + File.separator + "HookDemo" + File.separator + "getIntent2";
        String intentStr = FileUtils.readStringToFile(path);
        if (!TextUtils.isEmpty(intentStr)) {
            if (!TextUtils.isEmpty(intentStr)) {
                String[] strs = intentStr.split("@@@@@");
                String groupName = strs[0];
                String troopUin = strs[2];
                if (!TextUtils.isEmpty(troopUin) || !troopUin.equals("null")) {
                    GroupInfo info = new GroupInfo();
                    info.setTroop_uin(troopUin);
                    info.setGroupName(groupName);
                    info.update(Long.parseLong(group_id));
                }
            }
        }
        AccessibilityNodeInfo groupData = null;
        if ((groupData = Utils.findViewByDesc(service, "群资料卡")) != null) {
            LogUtils.logInfo("点击群资料的描述性文本");
            Utils.clickComponeByXY(groupData);
            Utils.sleep(1500L);
        } else if ((groupData = Utils.findViewById(service, "com.tencent.mobileqq:id/ivTitleBtnRightImage")) != null) {
            LogUtils.logInfo("点击群资料的ID");
            Utils.clickComponeByXY(groupData);
            Utils.sleep(1500L);
        }
        Utils.sleep(4000);
        AccessibilityNodeInfo butt = Utils.findViewByType(service, Button.class.getName());
        if (butt != null) {
            GroupInfo groupInfo = new GroupInfo();
            groupInfo.setGroupType("fail");
            groupInfo.update(Long.parseLong(group_id));
            Utils.pressBack(service);
            Utils.sleep(1500L);
            groupNum++;
            forSendMsg(groupInfos, messageEntities, wxsign);
        }
    }

    /***
     *  点击进入获取群成员
     * @param messageEntities
     * @param wxsign
     * @param groupId
     * @param groupName
     * @param memberNum
     */
    private void clickIntoGetGroupMembers(List<MessageEntity> messageEntities, final String wxsign, List<GroupInfo> groupInfos, final List<GroupMembersInfo> membersInfos,
                                          final String groupId, final String groupName, final int memberNum,
                                          int sentNum, String groupinfo_id) {

        LogUtils.logInfo(" 数据库中保存的群成员数目 = " + membersInfos.size());
        if (membersInfos.size() < 100) {
            getReadFileGroupMembers(wxsign, messageEntities, groupInfos, membersInfos, groupName, groupId, memberNum, sentNum, groupinfo_id);
        }
    }


    /***
     * 判断类型及是否已被发送过
     * @param messageEntities
     * @param wxsign
     * @param groupName
     * @param groupId
     * @param memberName
     * @param memberId
     */
    private void theTypeofJudgmentAndWhetherItHasBeenSent(List<MessageEntity> messageEntities, String wxsign,
                                                          String groupName, String groupId, String memberName, String memberId, String troopUin, long member_id
            , int groupNumber, int sentNum, String groupinfo_id) {
        List<MembersInfo> membersInfoList = DataSupport.where("wxsign = ? and groupId = ? and memberId = ?",
                wxsign, groupId, memberId).find(MembersInfo.class);
        boolean istrue = false;
        if (memberName.length() > 2) {
            String name = memberName.subSequence(0, 1) + "";
            LogUtils.logInfo("第一个字符 name = " + name);
            if (name.equals("a") || name.equals("A")) {
                MembersInfo membersInfo = new MembersInfo();
                membersInfo.setGroupId(groupId);
                membersInfo.setMembersName(memberName);
                membersInfo.setMemberId(memberId);
                membersInfo.setMemberSendTime(" ");
                membersInfo.setGroupName(groupName);
                membersInfo.setWxsign(wxsign);
                membersInfo.saveThrows();

                GroupMembersInfo membersInfo1 = new GroupMembersInfo();
                membersInfo1.setSendOrNot("是");
                membersInfo1.update(member_id);
                ContentValues values = new ContentValues();
                values.put("sendornot", "是");
                DataSupport.update(GroupMembersInfo.class, values, member_id);
                istrue = true;
            }
        }

        if ((membersInfoList.size() == 0) && !istrue) {
            LogUtils.logInfo("可以发送的数据 : " + " memberId = " + memberId + " groupId = " + groupId);
            Utils.startGroupMembersChatView(service, memberId,1000, memberName, groupId, troopUin);
            Utils.sleep(3000L);

            String path = "/sdcard" + File.separator + Constants.CACHE_PATH_NAME + File.separator + "HookDemo" + File.separator + "getIntent2";
            String intentStr = FileUtils.readStringToFile(path);
            LogUtils.logInfo(" wewewe   intentStr = " + intentStr + "  groupId = " + groupId);

            if (!TextUtils.isEmpty(intentStr)) {
                String[] strs = intentStr.split("@@@@@");
                troopUin = strs[2];
                LogUtils.logInfo("length = " + strs.length + " troopUin = " + troopUin);
                if (!TextUtils.isEmpty(troopUin) || !troopUin.equals("null")) {
                    GroupInfo info = new GroupInfo();
                    info.setTroop_uin(troopUin);
                    info.update(Long.parseLong(groupinfo_id));

                    AccessibilityNodeInfo edit = Utils.findViewByType(service, EditText.class.getName());
                    if (edit != null) {
                        LogUtils.logInfo("找到 输入框 ");
                        SendMsgType(messageEntities, wxsign, memberName, memberId, groupName, groupId,
                                0, groupNumber, sentNum, groupinfo_id);
                    }
                } else {
                    groupNum++;
                    forSendMsg(groupInfos, messageEntities, wxsign);
                }
            }
        } else {
            GroupMembersInfo membersInfo1 = new GroupMembersInfo();
            membersInfo1.setSendOrNot("是");
            membersInfo1.update(member_id);

            ContentValues values = new ContentValues();
            values.put("sendornot", "是");
            DataSupport.update(GroupMembersInfo.class, values, member_id);

            memberIndex++;
            forSendMsg(groupInfos, messageEntities, wxsign);
//            getInputGroupId(messageEntities, wxsign);
        }
    }


    /***
     *  获取并保存群成员数据
     * @param wxsign
     * @param groupName
     */
    private void getReadFileGroupMembers(final String wxsign, final List<MessageEntity> messageEntities, final List<GroupInfo> groupInfos, final List<GroupMembersInfo> membersInfos, final String groupName, final String groupId, final int groupNumber, final int sentNum, final String groupinfo_id) {
        List<GroupInfo> groupInfoList = DataSupport.where(" wxsign = ? and groupId = ? ", wxsign, groupId).find(GroupInfo.class);
        LogUtils.logInfo(" groupInfoList size = " + groupInfoList.size() + " " + groupInfoList.get(0).getGroupId());
        if (groupInfoList.size() != 0) {
            GroupInfo groupInfo = groupInfoList.get(0);
            final String troopUin = groupInfo.getTroop_uin();
            String str2 = WhileStringContains(wxsign);
            if (!TextUtils.isEmpty(str2)) {
                final List<GroupMembersInfo> groupMembersInfos = new ArrayList<>();
                try {
                    JSONArray array = new JSONArray(str2);
                    LogUtils.i("array = " + array.length());
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
//                        LogUtils.i("jsonObject = "+ jsonObject.toString());
                        String friendnick = jsonObject.getString("friendnick"); // 成员昵称
                        String memberuin = jsonObject.getString("memberuin");
                        String troopuin = jsonObject.getString("troopuin");
                        String troopnick = jsonObject.getString("troopnick");//群成员备注昵称
                        String sex = jsonObject.getString("sex");
                        String age = jsonObject.getString("age");

                        GroupMembersInfo membersInfo = new GroupMembersInfo();
                        membersInfo.setMemberId(memberuin);
                        membersInfo.setMembersName(Utils.getFromBASE64(friendnick));
                        membersInfo.setMembersNameBase64(friendnick);
                        membersInfo.setGroupId(troopuin);
                        membersInfo.setMembersSex(sex);
                        membersInfo.setMemberAge(age);
                        membersInfo.setSendOrNot("否");
                        membersInfo.setGroupInfo(groupInfo);
                        membersInfo.setGroupName(groupName);
                        membersInfo.setGroupNameBase64(Utils.getBASE64(groupName));
                        membersInfo.setGroupCard(Utils.getFromBASE64(troopnick));
                        membersInfo.setGroupCardBase64(troopnick);
                        membersInfo.setIsAddMultiplayer("否");
                        membersInfo.setWxsign(wxsign);

                        groupMembersInfos.add(membersInfo);
                        membersInfo = null;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                LogUtils.logInfo(" groupMembersInfos size= " + groupMembersInfos.size());
                if (groupMembersInfos.size() > 0) {
                    groupInfo.setGroupMembersInfos(groupMembersInfos);
                    DataSupport.saveAllAsync(groupMembersInfos).listen(new SaveCallback() {

                        @Override
                        public void onFinish(boolean success) {
                            if (success) {
                                groupMembersInfos.clear();
                                LogUtils.logInfo("保存成功");

                                List<GroupMembersInfo> groupMembersInfoList = groupInfos.get(groupNum).getGroupMembersInfos();
                                LogUtils.logInfo("  保存成员数量 groupMembersInfoList" + groupMembersInfoList.size());
                                Utils.pressBack(service);
                                Utils.sleep(1500L);
                                Utils.pressBack(service);
                                Utils.sleep(1500L);
                                Utils.pressBack(service);
                                Utils.sleep(1500L);

                                String memberId = groupMembersInfoList.get(memberIndex).getMemberId();
                                String memberName = groupMembersInfoList.get(memberIndex).getMembersName();
                                long member_id = groupMembersInfoList.get(memberIndex).getId();
                                List<MembersInfo> membersInfoList = DataSupport.where("wxsign = ? and groupId= ?", wxsign, groupId).find(MembersInfo.class);
                                int memNum = 0;
                                if (membersInfoList.size() != 0) {
                                    memNum = membersInfoList.size();
                                }
                                LogUtils.logInfo("已发送条数 = " + memNum);
                                theTypeofJudgmentAndWhetherItHasBeenSent(messageEntities, wxsign, groupName, groupId, memberName, memberId, troopUin, member_id, groupMembersInfoList.size(), memNum, groupinfo_id);
                            }
                        }
                    });
                } else {
                    LogUtils.logInfo("未获取到成员数据");
                    groupNum++;
                    Utils.pressBack(service);
                    Utils.sleep(1500L);
                    Utils.pressBack(service);
                    Utils.sleep(1500L);
                    Utils.pressBack(service);
                    Utils.sleep(1500L);
                    forSendMsg(groupInfos, messageEntities, wxsign);
                }
            } else {
                LogUtils.logInfo("未获取到数据");
                groupNum++;
                Utils.pressBack(service);
                Utils.sleep(1500L);
                Utils.pressBack(service);
                Utils.sleep(1500L);
                Utils.pressBack(service);
                Utils.sleep(1500L);
                forSendMsg(groupInfos, messageEntities, wxsign);
            }
        } else {
            LogUtils.logInfo("未获取到该群");
            groupNum++;
            Utils.pressBack(service);
            Utils.sleep(1500L);
            Utils.pressBack(service);
            Utils.sleep(1500L);
            Utils.pressBack(service);
            Utils.sleep(1500L);
            forSendMsg(groupInfos, messageEntities, wxsign);
        }
    }

    /***
     * 处理保存本地的群成员数据
     * @param wxsign
     * @return
     */
    @NonNull
    private String WhileStringContains(String wxsign) {
        String str2 = "";
        Utils.sleep(10 * 1000L);
        String groupmembers = "/sdcard" + File.separator + Constants.CACHE_PATH_NAME + File.separator + "GroupMembers" + File.separator + wxsign + ".txt";
        String string = FileUtils.readStringToFile(groupmembers);
        if (!TextUtils.isEmpty(string)) {
            String chars = string.substring(0, string.length() - 1);
            StringBuffer buffer = new StringBuffer(chars);
            buffer.insert(0, "[");
            String str1 = buffer.toString();
            buffer.insert(str1.length(), "]");
            str2 = buffer.toString();
            if (str2.contains("]")) {
                return str2;
            }
        }
        return null;
    }

    /***
     * 选择发送素材类别
     * @param messageEntities
     * @param wxsign
     * @param membernick
     * @param membersId
     * @param groupName
     * @param groupId
     * @param msgLength
     */
    private void SendMsgType(List<MessageEntity> messageEntities, String
            wxsign, String membernick,
                             String membersId, String groupName, String groupId, int msgLength, int groupNumber, int sentNum, String groupinfo_id) {
        if (messageEntities.size() > 0) {
            if (msgLength < messageEntities.size()) {
                MessageEntity messageEntity = messageEntities.get(msgLength);
                switch (messageEntity.getType()) {
                    case SEND_PHOTO:
                        LogUtils.logInfo("發送圖片  url=" + messageEntity.getImgUrl());
                        sendAndDownloadImg(wxsign, messageEntities, messageEntity,
                                groupName, groupId, membernick, membersId, groupNumber, sentNum, groupinfo_id);
                        break;

                    case SEND_TEXT:
                        LogUtils.logInfo("發送文本");
                        sendTextMsg(messageEntities, wxsign, messageEntity.getSc_id(), 1,
                                groupName, groupId, membernick, messageEntity.getText(), membersId, groupNumber, sentNum, groupinfo_id);
                        break;
                }
            }
        }
    }

    int size = 0;

    /***
     * 获取图片下载链接Url
     * @param wx_sign
     * @param messageEntityList
     * @param messageEntity
     * @param groupName
     * @param groupId
     * @param memberName
     * @param membersId
     */
    private void sendAndDownloadImg(String
                                            wx_sign, List<MessageEntity> messageEntityList,
                                    MessageEntity messageEntity, String groupName, String groupId, String
                                            memberName, String membersId, int groupNumber, int sentNum, String groupinfo_id) {
        if (!TextUtils.isEmpty(messageEntity.getImgUrl())) {
            List<String> datas = new ArrayList<>();
            String[] imgUrls = messageEntity.getImgUrl().split("@@@");
            size = imgUrls.length;
            LogUtils.logInfo("   imgUrls.length= " + imgUrls.length);
            List<String> urlList = Arrays.asList(imgUrls);
            Utils.sleep(2000);
            downloadImage(wx_sign, messageEntityList, messageEntity, datas, urlList, groupName,
                    groupId, memberName, membersId, 0, groupNumber, sentNum, groupinfo_id);
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
     * @param groupId
     * @param memberName
     * @param membersId
     * @param exponent
     */
    private void downloadImage(final String wx_sign,
                               final List<MessageEntity> messageEntityList,
                               final MessageEntity messageEntity, final List<String> datas, final List<String> urlList,
                               final String groupName, final String groupId, final String memberName,
                               final String membersId, final int exponent,
                               final int groupNumber, final int sentNum, final String groupinfo_id) {
        if (exponent < urlList.size()) {
            String url = urlList.get(exponent);
            String[] names = url.split("/");
            String fileName = names[names.length - 1];
            String parentPath = "/sdcard" + File.separator + Constants.CACHE_PATH_NAME + File.separator + Constants.QQ_PHOTO + File.separator + fileName;
            if (!FileUtils.fileIsExists(parentPath)) {
                LogUtils.logInfo("  下载图片 " + url);
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
                        LogUtils.logInfo("重复执行图片下载");
                        downloadImage(wx_sign, messageEntityList, messageEntity, datas, urlList, groupName,
                                groupId, memberName, membersId, (exponent + 1), groupNumber, sentNum, groupinfo_id);
                    }
                });
            }
        } else {
            sendPhoto(messageEntityList, wx_sign, messageEntity.getSc_id(), groupName,
                    groupId, datas, memberName, membersId, groupNumber, sentNum, groupinfo_id);
        }
    }

    /***
     * 进入选择发送图片
     * @param messageEntityList
     * @param wx_sign
     * @param sc_id
     * @param groupName
     * @param groupId
     * @param datas
     * @param memberName
     * @param membersId
     */
    private void sendPhoto(List<MessageEntity> messageEntityList, String wx_sign, String sc_id,
                           String groupName, String groupId, List<String> datas, String memberName, String
                                   membersId, int groupNumber, int sentNum, String groupinfo_id) {
        LogUtils.logInfo("     发送图片     datas= " + datas.size());
        if (datas.size() > 0) {
            String phonetype = Utils.getSystemModel();
            Utils.sleep(3000L);
            AccessibilityNodeInfo groupChat = Utils.findViewByDesc(service, "聊天设置");
            if (groupChat != null) {

                AccessibilityNodeInfo photos = Utils.findViewByText(service, Button.class.getName(), "相册");
                LogUtils.logInfo("点击相册按钮");
                if (photos != null) {
                    Utils.clickCompone(photos);
                    Utils.sleep(2000);
                } else {
                    switch (phonetype) {
                        // 點擊發送圖片圖標
                        case Constants.HONOR_PHONE_MODEL:
                            Utils.tapScreenXY("154 1185");
                            Utils.sleep(2000);
                            break;
                        case Constants.RAMOS_PHONE_MODEL:
                            Utils.tapScreenXY("225 1750");
                            Utils.sleep(2000);
                            break;
                    }
                }
            }
            AccessibilityNodeInfo camera = Utils.findViewByText(service, "Camera");
            AccessibilityNodeInfo photo = Utils.findViewByText(service, "QQPhoto");
            if (camera != null) {
                Utils.clickComponeByXY(camera);
                Utils.sleep(2000);
                sendPhotoMsg(messageEntityList, wx_sign, sc_id, 0, groupName, groupId, datas, memberName, membersId, groupNumber, sentNum, groupinfo_id);
            } else if (photo != null) {
                Utils.clickComponeByXY(photo);
                Utils.sleep(2000);
                sendPhotoMsg(messageEntityList, wx_sign, sc_id, 0, groupName, groupId, datas, memberName, membersId, groupNumber, sentNum, groupinfo_id);
            } else {
                Utils.clickCompone(Utils.findViewByText(service, "取消"));//com.tencent.mobileqq:id/ivTitleBtnRightText
                Utils.sleep(2000);

                Utils.pressBack(service);
                Utils.sleep(1500);
            }
        }
    }

    /***
     *  点击发送图片
     * @param sign 当前帐号唯一标识
     * @param sc_id  发送的素材id
     * @param type 素材类别
     * @param grouopName
     * @param groupId
     * @param datas 图片素材保存本地路径集合
     * @param memberName
     * @param membersId
     */
    private void sendPhotoMsg(List<MessageEntity> messageEntities, String sign, String sc_id, int type, String
            grouopName, String groupId, List<String> datas, String memberName, String membersId, int groupNumber, int sentNum, String groupinfo_id) {
        AccessibilityNodeInfo imges = Utils.findViewById(service, "com.tencent.mobileqq:id/photo_list_gv");
        if (imges != null) {
            LogUtils.logInfo("size = " + imges.getChildCount() + "    ,   ");
            List<AccessibilityNodeInfo> checkes = Utils.findViewListByType(service, "android.widget.CheckBox");
            if (checkes != null && checkes.size() > 0) {
                LogUtils.logInfo("CheckBox size = " + checkes.size() + "    ,   ");
                if (checkes.size() == 1) {
                    Utils.clickCompone(checkes.get(0));
                    Utils.sleep(1500);
                } else {
                    int length = 0;
                    if (size > 0) {
                        length = size;
                    } else {
                        length = checkes.size() - 2;
                    }
                    for (int i = 0; i < length; i++) {
                        Utils.clickCompone(checkes.get(i));
                        Utils.sleep(1000);
                    }
                }
                Utils.clickCompone(Utils.findViewByText(service, "发送"));
                Utils.sleep(1500);
            }
            AccessibilityNodeInfo failImg = Utils.findViewById(service, "com.tencent.mobileqq:id/chat_item_fail_icon");
            if (failImg != null) {
                strings.add(groupId);
                if (strings.size() > 4) {
                    ContentValues values = new ContentValues();
                    values.put("groupType", "fail");
                    DataSupport.update(GroupInfo.class, values, Long.parseLong(groupinfo_id));
                    groupNum++;
                    memberIndex = 0;
                    item_num = 0;
                    strings.clear();
                    Utils.pressBack(service);
                    Utils.sleep(1500L);
                    Utils.pressBack(service);
                    Utils.sleep(1500L);
//                        getInputGroupId(messageEntities, sign);
                    addDataToDB(0, sign, grouopName, groupId, memberName, membersId, groupNumber, sentNum);
                    forSendMsg(groupInfos, messageEntities, sign);
                } else {
                    memberIndex++;
                    Utils.pressBack(service);
                    Utils.sleep(1500L);
                    Utils.pressBack(service);
                    Utils.sleep(1500L);
//                        getInputGroupId(messageEntities, sign);
                    addDataToDB(0, sign, grouopName, groupId, memberName, membersId, groupNumber, sentNum);
                    forSendMsg(groupInfos, messageEntities, sign);
                }
            } else {
                if (Utils.findViewByTextMatch(service, "无法上传") != null) {
                    Utils.findViewByText(service, "我知道了");
                    Utils.sleep(1500L);

                    Utils.clickCompone(Utils.findViewByText(service, "取消"));//com.tencent.mobileqq:id/ivTitleBtnRightText
                    Utils.sleep(1500L);

                    Utils.pressBack(service);
                    Utils.sleep(1500L);
                } else {
                    HttpHandler.qunfaFinish(sign, sc_id, membersId);
                    addDataToDB(0, sign, grouopName, groupId, memberName, membersId, groupNumber, sentNum);
                    Utils.pressBack(service);
                    Utils.sleep(1500L);
                    Utils.pressBack(service);
                    Utils.sleep(1500L);
                    LogUtils.logInfo("degree = " + degree);
                    item_num++;
                    degree++;
                    //                        getInputGroupId(messageEntities, wxsign);
                    forSendMsg(groupInfos, messageEntities, sign);
                }
            }
        }
    }

    /***
     * 保存已发送成员
     * @param type
     * @param wxsign  当前帐号唯一标识
     * @param groupName 群名称
     * @param groupId 群id
     * @param memberName 成员名称
     * @param membersId 成员id
     */
    private void addDataToDB(int type, String wxsign, String groupName, String groupId, String
            memberName, String membersId, int groupNumber, int sentNum) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss ");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String date = formatter.format(curDate);
        LogUtils.logInfo("存入数据库  groupName=" + groupName + " groupId = " + groupId + "  membersId = " + membersId + "  memberName= " + memberName + "  date= " + date);
        MembersInfo membersInfo = new MembersInfo();
        membersInfo.setGroupId(groupId);
        membersInfo.setMembersName(memberName);
        membersInfo.setMemberId(membersId);
        membersInfo.setMemberSendTime(date);
        membersInfo.setGroupName(groupName);
        membersInfo.setWxsign(wxsign);

        List<GroupMembersInfo> groupMembersInfoList = DataSupport.where("wxsign = ? and groupId= ? and memberId = ? ", wxsign, groupId, membersId).find(GroupMembersInfo.class);
        List<GroupInfo> groupInfoList = DataSupport.where("wxsign = ? and groupId= ? ", wxsign, groupId).find(GroupInfo.class);
        if (groupMembersInfoList.size() > 0) {
            GroupMembersInfo groupMembersInfo = groupMembersInfoList.get(0);
            long id = groupMembersInfo.getId();
            groupMembersInfo.setSendOrNot("是");
            groupMembersInfo.update(id);
            ContentValues values = new ContentValues();
            values.put("sendOrNot", "是");
            DataSupport.update(GroupMembersInfo.class, values, id);
        }

        if (groupInfoList.size() > 0) {
            GroupInfo groupInfo = groupInfoList.get(0);
            long id = groupInfo.getId();
            int notNum = 0;
            if (groupNumber > sentNum) {
                notNum = groupNumber - (sentNum + 1);
            } else {
                notNum = 0;
            }
            ContentValues values = new ContentValues();
            values.put("membersNum", groupNumber);
            values.put("member_sentNum", (sentNum + 1));
            values.put("member_notsent_num", notNum);
            DataSupport.update(GroupInfo.class, values, id);
        }

        List<MembersInfo> membersInfos = DataSupport.where("wxsign = ? and groupId= ? and memberId = ? ", wxsign, groupId, membersId).find(MembersInfo.class);
        if (membersInfos.size() == 0) {
            membersInfo.saveThrows();
        } else {
            ContentValues values = new ContentValues();
            values.put("memberSendTime", date);
            values.put("membersName", memberName);
            values.put("groupName", groupName);
            values.put("wxsign", wxsign);
            DataSupport.updateAll(MembersInfo.class, values, "wxsign = ? and groupId= ? and memberId = ? ", wxsign, groupId, membersId);
        }
    }

    List<String> strings = new ArrayList<>();

    /***
     *  输入文本并发送
     * @param sendmsg
     */
    private void sendTextMsg(List<MessageEntity> messageEntities, String sign, String sc_id, int type, String groupName,
                             String groupId, String membernick, String sendmsg, String membersId, int groupNumber, int sentNum, String groupinfo_id) {
        if (!TextUtils.isEmpty(sendmsg)) {
            AccessibilityNodeInfo set = Utils.findViewByDesc(service, "聊天设置");
            if (set != null) {
                AccessibilityNodeInfo editText = Utils.findViewByType(service, EditText.class.getName());
                if (editText != null) {
                    LogUtils.logInfo(" sendmsg = " + sendmsg);
                    Utils.componeFocus(editText);
                    Utils.sleep(1500L);
                    Utils.selectAllText(editText);
                    Utils.sleep(1500L);
                    Utils.inputText(service, editText, sendmsg);
                    Utils.sleep(2000L);
                    AccessibilityNodeInfo sendBtn = Utils.findViewByText(service, Button.class.getName(), "发送");
                    if (sendBtn != null && sendBtn.isEnabled()) {
                        Utils.clickCompone(sendBtn);
                        Utils.sleep(1500L);
                        AccessibilityNodeInfo failImg = Utils.findViewById(service, "com.tencent.mobileqq:id/chat_item_fail_icon");
                        if (failImg != null) {
                            strings.add(groupId);
                            if (strings.size() > 4) {
                                ContentValues values = new ContentValues();
                                values.put("groupType", "fail");
                                DataSupport.update(GroupInfo.class, values, Long.parseLong(groupinfo_id));
                                groupNum++;
                                memberIndex = 0;
                                item_num = 0;
                                group_send_num++;
                                strings.clear();
                                Utils.pressBack(service);
                                Utils.sleep(1500L);
//                        getInputGroupId(messageEntities, sign);
                                addDataToDB(0, sign, groupName, groupId, membernick, membersId, groupNumber, sentNum);
                                forSendMsg(groupInfos, messageEntities, sign);
                            } else {
                                memberIndex++;
                                Utils.pressBack(service);
                                Utils.sleep(1500L);
//                        getInputGroupId(messageEntities, sign);
                                addDataToDB(0, sign, groupName, groupId, membernick, membersId, groupNumber, sentNum);
                                forSendMsg(groupInfos, messageEntities, sign);
                            }

                        } else {
                            HttpHandler.qunfaFinish(sign, sc_id, membersId);
                            addDataToDB(0, sign, groupName, groupId, membernick, membersId, groupNumber, sentNum);
                            Utils.pressBack(service);
                            Utils.sleep(1500L);
                            item_num++;
                            degree++;
                            //                        getInputGroupId(messageEntities, wxsign);
                            forSendMsg(groupInfos, messageEntities, sign);
                        }
                    }
                } else {
                    Utils.pressBack(service);
                    Utils.sleep(1500L);
                }
            } else {
                Utils.pressBack(service);
                Utils.sleep(1500L);
            }
        } else {
            Utils.pressBack(service);
            Utils.sleep(1500L);
        }
    }

}
