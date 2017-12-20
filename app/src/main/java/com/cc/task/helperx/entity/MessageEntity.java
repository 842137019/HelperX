package com.cc.task.helperx.entity;

import java.io.Serializable;

/**
 * 发送的消息
 * Created by fangying on 2017/9/27.
 */

public class MessageEntity implements Serializable{
    private int type;
    private String sc_id;
    private String wx_sign;
    private String weburl;
    private String text;
    private String imgUrl;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSc_id() {
        return sc_id;
    }

    public void setSc_id(String sc_id) {
        this.sc_id = sc_id;
    }

    public String getWx_sign() {
        return wx_sign;
    }

    public void setWx_sign(String wx_sign) {
        this.wx_sign = wx_sign;
    }

    public String getWeburl() {
        return weburl;
    }

    public void setWeburl(String weburl) {
        this.weburl = weburl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public MessageEntity() {
    }

    @Override
    public String toString() {
        return "MessageEntity{" +
                "type='" + type + '\'' +
                ", sc_id='" + sc_id + '\'' +
                ", wx_sign='" + wx_sign + '\'' +
                ", weburl='" + weburl + '\'' +
                ", text='" + text + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
