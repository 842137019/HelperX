package com.cc.task.helperx.entity;

import org.litepal.crud.DataSupport;

/**
 * 已发送的人员
 * Created by fangying on 2017/11/6.
 */

public class MembersInfo extends DataSupport {
    private long id;
    private String membersName;// 成员昵称
    private String memberId; // 成员id
    private String groupId; // 所在群id
    private String memberSendTime; // 对成员发送消息最后时间
    private String membersSex;
    private String groupCard; // 群名片
    private String groupName;
    private String wxsign;
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

    public String getMemberSendTime() {
        return memberSendTime;
    }

    public void setMemberSendTime(String memberSendTime) {
        this.memberSendTime = memberSendTime;
    }

    public String getMembersSex() {
        return membersSex;
    }

    public void setMembersSex(String membersSex) {
        this.membersSex = membersSex;
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

    @Override
    public String toString() {
        return "MembersInfo{" +
                "id=" + id +
                ", membersName='" + membersName + '\'' +
                ", memberId='" + memberId + '\'' +
                ", groupId='" + groupId + '\'' +
                ", memberSendTime='" + memberSendTime + '\'' +
                ", membersSex='" + membersSex + '\'' +
                ", groupCard='" + groupCard + '\'' +
                ", groupName='" + groupName + '\'' +
                ", wxsign='" + wxsign + '\'' +
                '}';
    }
}
