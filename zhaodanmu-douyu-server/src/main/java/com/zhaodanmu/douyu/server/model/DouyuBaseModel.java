package com.zhaodanmu.douyu.server.model;

import com.zhaodanmu.persistence.api.Model;

public class DouyuBaseModel implements Model {


    protected Long rid;//房间 id
    protected Long uid;//发送者 uid
    protected int level;//用户等级
    protected String nn;//发送者昵称
    protected String gid;//弹幕组 id
    protected String ic;//用户头像

    protected int nl;//贵族等级

    protected int nc;//贵族弹幕标识,0-非贵族弹幕,1-贵族弹幕,默认值 0

    protected String bnn;//徽章昵称

    protected Integer bl;//徽章等级

    protected Long brid;//徽章房间 id


    public Long getRid() {
        return rid;
    }

    public void setRid(Long rid) {
        this.rid = rid;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getNn() {
        return nn;
    }

    public void setNn(String nn) {
        this.nn = nn;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getIc() {
        return ic;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }

    public int getNl() {
        return nl;
    }

    public void setNl(int nl) {
        this.nl = nl;
    }

    public int getNc() {
        return nc;
    }

    public void setNc(int nc) {
        this.nc = nc;
    }

    public String getBnn() {
        return bnn;
    }

    public void setBnn(String bnn) {
        this.bnn = bnn;
    }

    public Integer getBl() {
        return bl;
    }

    public void setBl(Integer bl) {
        this.bl = bl;
    }

    public Long getBrid() {
        return brid;
    }

    public void setBrid(Long brid) {
        this.brid = brid;
    }
}
