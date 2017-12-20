package com.cc.task.helperx.task;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.cc.task.helperx.entity.GroupInfo;
import com.cc.task.helperx.entity.GroupMembersInfo;
import com.cc.task.helperx.entity.MembersInfo;
import com.cc.task.helperx.entity.TaskEntry;
import com.cc.task.helperx.service.HelperQQService;
import com.cc.task.helperx.utils.Constants;
import com.cc.task.helperx.utils.FileUtils;
import com.cc.task.helperx.utils.LogUtils;
import com.cc.task.helperx.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import org.litepal.crud.callback.SaveCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 点击群列表
 * Created by fangying on 2017/10/10.
 */

public class RegisterTask {

    private HelperQQService service;
    private Handler handler;

    int index = 0;
    int swipIndex = 0;

    public RegisterTask(HelperQQService service, Handler handler) {
        this.service = service;
        this.handler = handler;
    }

    /**
     * 查找本地群
     *
     * @param taskEntry
     */
    public void findALocalGroup(TaskEntry taskEntry) {
        if (!Utils.isTragetActivity(Constants.QQ_HOME_ACTIVITY)) {
            Utils.startPage(Constants.QQ_HOME_ACTIVITY);
            Utils.sleep(5000L);
        }
        String path = "/sdcard" + File.separator + Constants.CACHE_PATH_NAME + File.separator + "HookDemo" + File.separator + "getIntent2";
        File file1 = new File(path);
        if (file1.exists()) {
            FileUtils.deleteFile(path);
        }
        AccessibilityNodeInfo contacts = Utils.findViewByTextMatch(service, "联系人");
        if (contacts == null) {
            handler.sendEmptyMessage(1);
            return;
        }
        Utils.clickCompone(contacts);
        Utils.sleep(2000);

        Utils.clickCompone(Utils.findViewByTextMatch(service, "群"));
        Utils.sleep(2000);

        if (swipIndex < 3) {
            List<AccessibilityNodeInfo> listText = Utils.findViewListById(service, "com.tencent.mobileqq:id/text1");
            if (listText != null && listText.size() > 0) {
                LogUtils.i("群数量:" + listText.size());
                if (index < listText.size()) {
                    String groupName = listText.get(index).getText() + "";
                    LogUtils.i(" 第" + index + "个群名字=" + groupName);
                    String troopuin = isfindStringObj(listText.get(index));
                    AccessibilityNodeInfo groupChat = Utils.findViewByDesc(service, "群资料卡");
                    if (groupChat != null) {
                        Utils.clickCompone(groupChat);
                        Utils.sleep(4000L);
                        LogUtils.logInfo("群详情页面 并判断是否存在有申请加群按钮 ");
                        AccessibilityNodeInfo butt = Utils.findViewByType(service, Button.class.getName());
                        if (butt != null) {
                            GroupInfo groupInfo = new GroupInfo();
                            groupInfo.setWxsign(taskEntry.getWx_sign());
                            groupInfo.setGroupName(groupName);
                            groupInfo.setGroupNameBase64(Utils.getBASE64(groupName));
                            groupInfo.setGroupId(groupName);
                            groupInfo.setGroupType("fail");
                            groupInfo.saveThrows();
                            Utils.pressBack(service);
                            Utils.sleep(1500L);
                            index++;
                            findALocalGroup(taskEntry);
                        } else {
                            List<AccessibilityNodeInfo> txts = Utils.findViewListByType(service, TextView.class.getName());
                            String groupId = "";
                            if (txts != null && txts.size() > 0) {
                                groupId = txts.get(1).getText() + "";
                                LogUtils.logInfo("  groupName = " + groupName + "  groupId = " + groupId);
                                settingGroupMsg();
                                int memberNum = 0;
                                AccessibilityNodeInfo memberNumstr = Utils.findViewByText(service, "名成员");
                                if (memberNumstr != null) {
                                    String numstr = memberNumstr.getText() + "";
                                    LogUtils.logInfo("获取成员数量 numstr= " + numstr);
                                    memberNum = Integer.parseInt(Utils.getNumbers(numstr));
//                                    Utils.clickCompone(memberNumstr);
                                }
                                addDataToDB(taskEntry, groupName, groupId, memberNum, troopuin);
                            } else {
                                Utils.pressBack(service);
                                Utils.sleep(1500L);
                                Utils.pressBack(service);
                                Utils.sleep(1500L);
                                findALocalGroup(taskEntry);
                            }
                        }
                    } else {
                        index = 0;
                        LogUtils.i(" 群资料卡没找到:" + groupName);
                        handler.sendEmptyMessage(1);
                    }
                } else {
                    index = 0;
                    swipIndex++;
                    Utils.pressSmallScrollDown();
                    Utils.sleep(2000L);
                    findALocalGroup(taskEntry);
//                LogUtils.i(" 群数量下标越界:" + index);
//                handler.sendEmptyMessage(1);
                }
            } else {
                index = 0;
                LogUtils.i("本地没有群");
                handler.sendEmptyMessage(1);
            }
        } else {
            index = 0;
            swipIndex = 0;
            handler.sendEmptyMessage(1);
        }
    }

    private void addDataToDB(final TaskEntry taskEntry, String groupName, String groupId, int memberNum, String troopUin) {
        List<GroupInfo> groupInfos = DataSupport.where("wxsign = ? and groupId = ?", taskEntry.getWx_sign(), groupId).find(GroupInfo.class);
        GroupInfo groupInfo = new GroupInfo();
        groupInfo.setWxsign(taskEntry.getWx_sign());
        groupInfo.setGroupName(groupName);
        groupInfo.setGroupNameBase64(Utils.getBASE64(groupName));
        groupInfo.setGroupId(groupId);
        if (groupInfos.size() == 0) {
            groupInfo.setGroupType("success");
        } else {
            groupInfo.setGroupType(groupInfo.getGroupType());
        }
        groupInfo.setMembersNum(memberNum + "");
        groupInfo.setMember_notsent_num(memberNum + "");
        groupInfo.setMember_sentNum(0 + "");
        groupInfo.setGroupIsTrue("0");
//        groupInfo.setGroupMembersInfos();
        groupInfo.setTroop_uin(troopUin);
        LogUtils.logInfo("保存的群：" + groupName + " 群id:" + groupId + " troopUin:" + troopUin);
        groupInfo.saveOrUpdateAsync(" groupId = ? ", groupId).listen(new SaveCallback() {
            @Override
            public void onFinish(boolean success) {
                if (success) {
                    LogUtils.logInfo("保存的群完成");
                    Utils.pressBack(service);
                    Utils.sleep(2000);
                    Utils.pressBack(service);
                    Utils.sleep(2000);
                    index++;
                    findALocalGroup(taskEntry);
                }
            }
        });
    }

    /**
     * 设置屏蔽消息
     */
    private void settingGroupMsg() {
        AccessibilityNodeInfo pc = null;
        pc = Utils.findViewByText(service, "电脑端的消息提醒方式将与手机端保持同步。");
        if (pc != null) {
            Utils.clickCompone(Utils.findViewByText(service, "我知道了"));
            Utils.sleep(2000L);
        }

        Utils.pressSmallScrollDown();
        Utils.sleep(4000L);
        List<AccessibilityNodeInfo> comBtns = Utils.findViewListByType(service, CompoundButton.class.getName());
        if (comBtns.size() > 0) {
            AccessibilityNodeInfo comBtn = comBtns.get(0);
            if (!comBtn.isChecked()) {
                Utils.clickComponeByXY(comBtn);
                Utils.sleep(2000L);
            }
            pc = Utils.findViewByText(service, "电脑端的消息提醒方式将与手机端保持同步。");
            if (pc != null) {
                Utils.clickCompone(Utils.findViewByText(service, "我知道了"));
                Utils.sleep(1500L);
            }
            AccessibilityNodeInfo textBtn = null;
            AccessibilityNodeInfo pbxx = Utils.findViewByTextMatch(service, "屏蔽群消息");
            if ((textBtn = Utils.findViewByTextMatch(service, "群消息提示设置")) != null && pbxx == null) {
                Utils.clickCompone(textBtn);
                Utils.sleep(1500L);

                if (Utils.findViewById(service, "com.tencent.mobileqq:id/action_sheet_actionView") != null) {
                    if ((textBtn = Utils.findViewByText(service, "屏蔽群消息")) != null) {
                        Utils.clickCompone(textBtn);
                        Utils.sleep(1500L);
                    }
                }
            }

            pc = Utils.findViewByText(service, "电脑端的消息提醒方式将与手机端保持同步。");
            if (pc != null) {
                Utils.clickCompone(Utils.findViewByText(service, "我知道了"));
                Utils.sleep(1500L);
            }
        }
    }

    private int getMemberNum() {
        int memberNum = 0;
        AccessibilityNodeInfo memberNumstr = Utils.findViewByText(service, "名成员");
        if (memberNumstr != null) {
            String numstr = memberNumstr.getText() + "";
            LogUtils.logInfo("获取成员数量 numstr= " + numstr);
            memberNum = Integer.parseInt(Utils.getNumbers(numstr));

        }
        return memberNum;
    }

    private String isfindStringObj(AccessibilityNodeInfo nodeInfo) {
        String uin = "";
        if (nodeInfo != null) {
            Utils.clickCompone(nodeInfo);
            Utils.sleep(2000);
            LogUtils.i("群聊天页面");
            AccessibilityNodeInfo groupNotice = Utils.findViewByText(service, "群公告");
            if (groupNotice != null) {
                Utils.clickCompone(Utils.findViewByText(service, "我知道了"));
                Utils.sleep(1500);
            }

            String troop_code = null;
            String troop_name = null;
            String path = "/sdcard" + File.separator + Constants.CACHE_PATH_NAME + File.separator + "HookDemo" + File.separator + "getIntent2";
            String intentStr = FileUtils.readStringToFile(path);
            if (!TextUtils.isEmpty(intentStr)) {
                String[] strs = intentStr.split("@@@@@");
//                groupName = strs[0];
                troop_code = strs[1];
                String troopUin = strs[2];
                troop_name = strs[3];
                if (!TextUtils.isEmpty(troopUin) || !troopUin.equals("null")) {
                    uin = troopUin;
                }
            }

            // com.tencent.mobileqq:id/dialogText
            AccessibilityNodeInfo info = Utils.findViewByText(service, "该群因涉及违反相关条例，已被永久封停，不能使用。系统将会自动解散该群。");
            if (info != null) {
                // com.tencent.mobileqq:id/dialogRightBtn
                Utils.clickCompone(Utils.findViewByText(service, "我知道了"));
                Utils.sleep(2000L);
            }
        }
        return uin;
    }

    /***
     *  获取并保存群成员数据
     * @param taskEntry
     * @param groupName
     */
    private void getReadFileGroupMembers(int memberNum, TaskEntry taskEntry, final String groupName, final String groupId) {
        LogUtils.logInfo("获取群成员数据");
        List<GroupInfo> groupInfoList = DataSupport.where(" wxsign = ? and groupId = ? ", taskEntry.getWx_sign(), groupId).find(GroupInfo.class);
        if (groupInfoList.size() == 1) {
            GroupInfo groupInfo = groupInfoList.get(0);
            String str2 = WhileStringContains(taskEntry.getWx_sign());
            final List<GroupMembersInfo> groupMembersInfos = new ArrayList<>();
            try {
                JSONArray array = new JSONArray(str2);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = array.getJSONObject(i);
                    String friendnick = jsonObject.getString("friendnick");
                    String memberuin = jsonObject.getString("memberuin");
                    String troopuin = jsonObject.getString("troopuin");
                    String troopnick = jsonObject.getString("troopnick");
                    String sex = jsonObject.getString("sex");

                    GroupMembersInfo membersInfo = new GroupMembersInfo();
                    membersInfo.setMemberId(memberuin);
                    membersInfo.setMembersNameBase64(friendnick);
                    membersInfo.setGroupId(troopuin);
                    membersInfo.setMembersSex(sex);
                    membersInfo.setGroupCardBase64(troopnick);
                    membersInfo.setSendOrNot("否");
                    membersInfo.setMembersName(Utils.getFromBASE64(friendnick));
                    membersInfo.setGroupInfo(groupInfo);
                    membersInfo.setGroupName(groupName);
                    membersInfo.setGroupNameBase64(Utils.getBASE64(groupName));
                    membersInfo.setGroupCard(Utils.getFromBASE64(troopnick));
                    membersInfo.setIsAddMultiplayer("否");
                    membersInfo.setWxsign(taskEntry.getWx_sign());
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
                            Utils.pressBack(service);
                            Utils.sleep(1500L);
                            Utils.pressBack(service);
                            Utils.sleep(1500L);
                            Utils.pressBack(service);
                            Utils.sleep(1500L);
                        }
                    }
                });
            } else {
                LogUtils.logInfo("未获取到成员数据");
                index++;
                Utils.pressBack(service);
                Utils.sleep(1500L);
                Utils.pressBack(service);
                Utils.sleep(1500L);
                Utils.pressBack(service);
                Utils.sleep(1500L);
            }
        } else {
            LogUtils.logInfo("未获取到数据");
            index++;
            Utils.pressBack(service);
            Utils.sleep(1500L);
            Utils.pressBack(service);
            Utils.sleep(1500L);
            Utils.pressBack(service);
            Utils.sleep(1500L);
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
        }
        return str2;
    }
}
