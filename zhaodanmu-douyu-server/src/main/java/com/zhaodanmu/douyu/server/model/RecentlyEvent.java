package com.zhaodanmu.douyu.server.model;

import com.zhaodanmu.persistence.elasticsearch.model.SimpinleUserModel;

import java.util.Date;

public class RecentlyEvent {

    private SimpinleUserModel who;

    private String type;

    private Date t;

    private String rid;

    private String desc;

    public RecentlyEvent() {
    }

    public RecentlyEvent(SimpinleUserModel who, String type, Date t, String rid, String desc) {
        this.who = who;
        this.type = type;
        this.t = t;
        this.rid = rid;
        this.desc = desc;
    }

    public SimpinleUserModel getWho() {
        return who;
    }

    public void setWho(SimpinleUserModel who) {
        this.who = who;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getT() {
        return t;
    }

    public void setT(Date t) {
        this.t = t;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "RecentlyEvent{" +
                "who=" + who +
                ", type='" + type + '\'' +
                ", t=" + t +
                ", rid='" + rid + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
