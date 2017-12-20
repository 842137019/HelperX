package com.cc.task.helperx.entity;

import java.io.Serializable;

/**
 * Created by fangying on 2017/9/4.
 */

public class TaskInfo implements Serializable{

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getMm_account() {
        return mm_account;
    }

    public void setMm_account(String mm_account) {
        this.mm_account = mm_account;
    }

    public String getMm_tel() {
        return mm_tel;
    }

    public void setMm_tel(String mm_tel) {
        this.mm_tel = mm_tel;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getDo_task() {
        return do_task;
    }

    public void setDo_task(String do_task) {
        this.do_task = do_task;
    }

    public String getGpsloction() {
        return gpsloction;
    }

    public void setGpsloction(String gpsloction) {
        this.gpsloction = gpsloction;
    }

    public int getTel_task_status() {
        return tel_task_status;
    }

    public void setTel_task_status(int tel_task_status) {
        this.tel_task_status = tel_task_status;
    }

    private String task_id;
    private String mm_account;
    private String mm_tel;
    private String mac;
    private String imei;
    private String do_task;
    private String gpsloction;
    private int tel_task_status;


    @Override
    public String toString() {
        return "TaskInfo{" +
                ", task_id='" + task_id + '\'' +
                ", mm_account='" + mm_account + '\'' +
                ", mm_tel='" + mm_tel + '\'' +
                ", mac='" + mac + '\'' +
                ", imei='" + imei + '\'' +
                ", do_task='" + do_task + '\'' +
                ", gpsloction='" + gpsloction + '\'' +
                ", tel_task_status=" + tel_task_status +
                '}';
    }
}
