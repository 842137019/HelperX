package com.cc.task.helperx.entity;

import java.io.Serializable;

/**
 *
 * Created by fangying on 2017/9/22.
 */

public class AddGroupEntity implements Serializable{
    private String greeting_way;
    private String group_account;
    private String word;// 驗證消息

    public String getGreeting_way() {
        return greeting_way;
    }

    public void setGreeting_way(String greeting_way) {
        this.greeting_way = greeting_way;
    }

    public String getGroup_account() {
        return group_account;
    }

    public void setGroup_account(String group_account) {
        this.group_account = group_account;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public String toString() {
        return "AddGroupEntity{" +
                "greeting_way='" + greeting_way + '\'' +
                ", group_account='" + group_account + '\'' +
                ", word='" + word + '\'' +
                '}';
    }
}
