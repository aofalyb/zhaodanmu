package com.zhaodanmu.douyu.server.cache;

import java.util.Date;

/**
 * Created by Administrator on 2018/11/4.
 */
public class UserEventCache {
    private long uid;
    private String text;
    private long rid;
    private Date t;

    public UserEventCache(long uid, String text, long rid, Date t) {
        this.uid = uid;
        this.text = text;
        this.rid = rid;
        this.t = t;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getRid() {
        return rid;
    }

    public void setRid(long rid) {
        this.rid = rid;
    }

    public Date getT() {
        return t;
    }

    public void setT(Date t) {
        this.t = t;
    }

    @Override
    public String toString() {
        return "UserEventCache{" +
                "uid=" + uid +
                ", text='" + text + '\'' +
                ", rid=" + rid +
                '}';
    }
}
