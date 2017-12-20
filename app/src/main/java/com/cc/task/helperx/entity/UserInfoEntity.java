package com.cc.task.helperx.entity;

import java.io.Serializable;

/**
 * Created by fangying on 2017/9/22.
 */

public class UserInfoEntity implements Serializable {
    private int id;
    private String account;
    private String nickname;
    private String head_url;
    private String signature;
    private String sex;
    private String area;
    private String birthday;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHead_url() {
        return head_url;
    }

    public void setHead_url(String headurl) {
        this.head_url = headurl;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "UserInfoEntity{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", nickname='" + nickname + '\'' +
                ", head_url='" + head_url + '\'' +
                ", signature='" + signature + '\'' +
                ", sex='" + sex + '\'' +
                ", area='" + area + '\'' +
                ", birthday='" + birthday + '\'' +
                '}';
    }
}
