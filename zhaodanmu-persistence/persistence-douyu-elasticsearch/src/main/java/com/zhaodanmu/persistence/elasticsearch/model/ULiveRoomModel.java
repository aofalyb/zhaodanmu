package com.zhaodanmu.persistence.elasticsearch.model;

import com.zhaodanmu.persistence.api.Model;
import com.zhaodanmu.persistence.api.TypeNameEnmu;

import java.util.Date;

public class ULiveRoomModel implements Model {

    protected Long uid;//发送者 uid
    protected Long rid;//房间 id
    protected int level;//用户等级
    protected String nn;//发送者昵称
    protected String ic;//用户头像

    protected int nl;//贵族等级

    protected String bnn;//徽章昵称

    protected int bl;//徽章等级

    protected Long brid;//徽章房间 id

    private Date t = new Date();

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

    public String getBnn() {
        return bnn;
    }

    public void setBnn(String bnn) {
        this.bnn = bnn;
    }

    public int getBl() {
        return bl;
    }

    public void setBl(int bl) {
        this.bl = bl;
    }

    public Long getBrid() {
        return brid;
    }

    public void setBrid(Long brid) {
        this.brid = brid;
    }

    public Date getT() {
        return t;
    }

    public void setT(Date t) {
        this.t = t;
    }

    @Override
    public String toString() {
        return "ULiveRoomModel{" +
                "uid=" + uid +
                ", rid=" + rid +
                ", level=" + level +
                ", nn='" + nn + '\'' +
                ", ic='" + ic + '\'' +
                ", nl=" + nl +
                ", bnn='" + bnn + '\'' +
                ", bl=" + bl +
                ", brid=" + brid +
                '}';
    }

    @Override
    public String getMType() {
        return TypeNameEnmu.user.name();
    }

    @Override
    public String getPK() {
        return String.valueOf(uid);
    }
}
