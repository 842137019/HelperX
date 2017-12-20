package com.cc.task.helperx.entity;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * 群组信息
 * Created by fangying on 2017/9/5.
 */
public class GroupInfo extends DataSupport {
    //uinname, troop_code, troopUin, troop_name;
    private Long id;
    private String groupId;
    private String groupName;
    private String troop_uin; // 群id的标识
//    private String uinname;
//    private String troop_code;
//    private String troop_name;
    private String groupNameBase64;
    private String groupType;// 加群状态
    private String groupIsTrue; // 是否删除 是1/否0
    private String wxsign;
    private String membersNum; // 成员数量
    private String member_sentNum; // 已发送的成员数量
    private String member_notsent_num; // 未发送的成员数量
    private List<GroupMembersInfo> groupMembersInfos = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getTroop_uin() {
        return troop_uin;
    }

    public void setTroop_uin(String troop_uin) {
        this.troop_uin = troop_uin;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getGroupIsTrue() {
        return groupIsTrue;
    }

    public void setGroupIsTrue(String groupIsTrue) {
        this.groupIsTrue = groupIsTrue;
    }

    public String getWxsign() {
        return wxsign;
    }

    public void setWxsign(String wxsign) {
        this.wxsign = wxsign;
    }

    public List<GroupMembersInfo> getGroupMembersInfos() {
        return DataSupport.where("groupinfo_id = ?", String.valueOf(id)).find(GroupMembersInfo.class);
    }

    public void setGroupMembersInfos(List<GroupMembersInfo> groupMembersInfos) {
        this.groupMembersInfos = groupMembersInfos;
    }

//    public String getUinname() {
//        return uinname;
//    }

//    public void setUinname(String uinname) {
//        this.uinname = uinname;
//    }
//
//    public String getTroop_code() {
//        return troop_code;
//    }
//
//    public void setTroop_code(String troop_code) {
//        this.troop_code = troop_code;
//    }
//
//    public String getTroop_name() {
//        return troop_name;
//    }
//
//    public void setTroop_name(String troop_name) {
//        this.troop_name = troop_name;
//    }

    public String getMembersNum() {
        return membersNum;
    }

    public void setMembersNum(String membersNum) {
        this.membersNum = membersNum;
    }

    public String getMember_sentNum() {
        return member_sentNum;
    }

    public void setMember_sentNum(String member_sentNum) {
        this.member_sentNum = member_sentNum;
    }

    public String getMember_notsent_num() {
        return member_notsent_num;
    }

    public void setMember_notsent_num(String member_notsent_num) {
        this.member_notsent_num = member_notsent_num;
    }

    public String getGroupNameBase64() {
        return groupNameBase64;
    }

    public void setGroupNameBase64(String groupNameBase64) {
        this.groupNameBase64 = groupNameBase64;
    }
}
