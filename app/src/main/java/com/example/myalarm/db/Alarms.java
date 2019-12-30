package com.example.myalarm.db;

import com.orm.SugarRecord;

import java.io.Serializable;

public class Alarms extends SugarRecord implements Serializable {

    String user;

    String name;

    String infos;

    Integer type; //0-生日 1-其他

    Integer notice; //0-静音 1-铃声 2-振动 3-混合

    String ring;

    String time;

    String remark;

    Boolean open;

    public String getRing() {
        return ring;
    }

    public void setRing(String ring) {
        this.ring = ring;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfos() {
        return infos;
    }

    public void setInfos(String infos) {
        this.infos = infos;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getNotice() {
        return notice;
    }

    public void setNotice(Integer notice) {
        this.notice = notice;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
