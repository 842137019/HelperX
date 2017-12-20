package com.cc.task.helperx.entity;

import java.io.Serializable;

/**
 * Created by fangying on 2017/9/21.
 */

public class TaskEntry implements Serializable {

    private String task_id;

    private String tel_sign;

    private String wx_sign;

    private String wx_id;

    private String do_task;

    private String wx_pass;

    /**
     * 任务的间隔时间
     */
    private long runtime;

    private String gpsloction;

    private String imei;

    private String mac;

    private String sid;

    private String sim;

    private String APIkey;

    private long current_time;

    private int tel_task_status;

    public TaskEntry() {
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getTel_sign() {
        return tel_sign;
    }

    public void setTel_sign(String tel_sign) {
        this.tel_sign = tel_sign;
    }

    public String getWx_sign() {
        return wx_sign;
    }

    public void setWx_sign(String wx_sign) {
        this.wx_sign = wx_sign;
    }

    public String getWx_id() {
        return wx_id;
    }

    public void setWx_id(String wx_id) {
        this.wx_id = wx_id;
    }

    public String getDo_task() {
        return do_task;
    }

    public void setDo_task(String do_task) {
        this.do_task = do_task;
    }

    public String getWx_pass() {
        return wx_pass;
    }

    public void setWx_pass(String wx_pass) {
        this.wx_pass = wx_pass;
    }

    public long getRuntime() {
        return runtime;
    }

    public void setRuntime(long runtime) {
        this.runtime = runtime;
    }

    public String getGpsloction() {
        return gpsloction;
    }

    public void setGpsloction(String gpsloction) {
        this.gpsloction = gpsloction;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSim() {
        return sim;
    }

    public void setSim(String sim) {
        this.sim = sim;
    }

    public int getTel_task_status() {
        return tel_task_status;
    }

    public void setTel_task_status(int tel_task_status) {
        this.tel_task_status = tel_task_status;
    }

    public String getAPIkey() {
        return APIkey;
    }

    public void setAPIkey(String APIkey) {
        this.APIkey = APIkey;
    }

    public long getCurrent_time() {
        return current_time;
    }

    public void setCurrent_time(long current_time) {
        this.current_time = current_time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskEntry taskEntry = (TaskEntry) o;

        return wx_id != null ? wx_id.equals(taskEntry.wx_id) : taskEntry.wx_id == null;

    }

    @Override
    public int hashCode() {
        return wx_id != null ? wx_id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "TaskEntry{" +
                "task_id='" + task_id + '\'' +
                ", tel_sign='" + tel_sign + '\'' +
                ", wx_sign='" + wx_sign + '\'' +
                ", wx_id='" + wx_id + '\'' +
                ", do_task='" + do_task + '\'' +
                ", wx_pass='" + wx_pass + '\'' +
                ", runtime='" + runtime + '\'' +
                ", gpsloction='" + gpsloction + '\'' +
                ", imei='" + imei + '\'' +
                ", mac='" + mac + '\'' +
                ", sid='" + sid + '\'' +
                ", sim='" + sim + '\'' +
                ", APIkey='" + APIkey + '\'' +
                ", current_time='" + current_time + '\'' +
                ", tel_task_status=" + tel_task_status +
                '}';
    }
}
