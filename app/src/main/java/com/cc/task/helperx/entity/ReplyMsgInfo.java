package com.cc.task.helperx.entity;

import org.litepal.crud.DataSupport;

/**
 * Created by fangying on 2017/10/25.
 */

public class ReplyMsgInfo extends DataSupport {

    private long id;
    private String groupId;
    private String groupName;
    private int msgType;// 群1/个人2
    private String sendmsgTime;
    private String wxsign;
    private String isTrue;// 删除1/存在0

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getSendmsgTime() {
        return sendmsgTime;
    }

    public void setSendmsgTime(String sendmsgTime) {
        this.sendmsgTime = sendmsgTime;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getWxsign() {
        return wxsign;
    }

    public void setWxsign(String wxsign) {
        this.wxsign = wxsign;
    }

    public String getIsTrue() {
        return isTrue;
    }

    public void setIsTrue(String isTrue) {
        this.isTrue = isTrue;
    }
}


