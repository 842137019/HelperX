package com.cc.task.helperx.entity;

import org.litepal.crud.DataSupport;

/**
 * 群成员信息
 * Created by fangying on 2017/11/16.
 */

public class GroupMembersInfo extends DataSupport{

    private long id;
    private String membersName;// 成员昵称
    private String memberId; // 成员id
    private String membersNameBase64;// 成员昵称
    private String memberAge; // 成员年龄
    private String membersSex;// 成员性别
    private String groupId; // 所在群id
    private String groupCard; // 群名片
    private String groupCardBase64; // 群名片
    private String groupName;
    private String groupNameBase64;
    private String wxsign;
    private String sendOrNot; // 是否已发送过
    private String isAddMultiplayer; // 是否已添加过讨论组

    private GroupInfo groupInfo;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMembersName() {
        return membersName;
    }

    public void setMembersName(String membersName) {
        this.membersName = membersName;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getMembersSex() {
        return membersSex;
    }

    public void setMembersSex(String membersSex) {
        this.membersSex = membersSex;
    }

    public String getMemberAge() {
        return memberAge;
    }

    public void setMemberAge(String memberAge) {
        this.memberAge = memberAge;
    }

    public String getGroupCard() {
        return groupCard;
    }

    public void setGroupCard(String groupCard) {
        this.groupCard = groupCard;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getWxsign() {
        return wxsign;
    }

    public void setWxsign(String wxsign) {
        this.wxsign = wxsign;
    }

    public GroupInfo getGroupInfo() {
        return groupInfo;
    }

    public void setGroupInfo(GroupInfo groupInfo) {
        this.groupInfo = groupInfo;
    }

    public String getSendOrNot() {
        return sendOrNot;
    }

    public void setSendOrNot(String sendOrNot) {
        this.sendOrNot = sendOrNot;
    }

    public String getIsAddMultiplayer() {
        return isAddMultiplayer;
    }

    public void setIsAddMultiplayer(String isAddMultiplayer) {
        this.isAddMultiplayer = isAddMultiplayer;
    }

    public String getMembersNameBase64() {
        return membersNameBase64;
    }

    public void setMembersNameBase64(String membersNameBase64) {
        this.membersNameBase64 = membersNameBase64;
    }

    public String getGroupCardBase64() {
        return groupCardBase64;
    }

    public void setGroupCardBase64(String groupCardBase64) {
        this.groupCardBase64 = groupCardBase64;
    }

    public String getGroupNameBase64() {
        return groupNameBase64;
    }

    public void setGroupNameBase64(String groupNameBase64) {
        this.groupNameBase64 = groupNameBase64;
    }
}
