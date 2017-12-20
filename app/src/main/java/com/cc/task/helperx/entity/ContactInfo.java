package com.cc.task.helperx.entity;

import org.litepal.crud.DataSupport;

/**
 *
 * Created by fangying on 2017/9/5.
 */

public class ContactInfo extends DataSupport {

    private long id;
    private String contactId;
    private String contactName;
    private String contactSex;
    private String wxsign;

    public ContactInfo(int id, String contactId, String contactName, String contactSex) {
        this.id = id;
        this.contactId = contactId;
        this.contactName = contactName;
        this.contactSex = contactSex;
    }

    public ContactInfo() {
    }

    public String getWxsign() {
        return wxsign;
    }

    public void setWxsign(String wxsign) {
        this.wxsign = wxsign;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactSex() {
        return contactSex;
    }

    public void setContactSex(String contactSex) {
        this.contactSex = contactSex;
    }

    @Override
    public String toString() {
        return "ContactInfo{" +
                "id=" + id +
                ", contactId='" + contactId + '\'' +
                ", contactName='" + contactName + '\'' +
                ", contactSex='" + contactSex + '\'' +
                '}';
    }
}
