package com.zhaodanmu.persistence.elasticsearch.model;

import com.zhaodanmu.persistence.api.Model;
import com.zhaodanmu.persistence.api.TypeNameEnmu;

import java.util.Date;

public class NewBlackresModel implements Model {

    private String type = "newblackres";

    private Date t = new Date();
    private Long rid;//房间 id

    private String ret;//错误码

    private Integer otype;//操作者类型，0：普通用户，1：房管：，2：主播，3：超管

    private Long sid;//操作者ID
    private String snic;//操作者昵称

    private Long did;//被操作者id
    private String dnic;//被禁言用户昵称

    private Long endtime;//禁言到期日

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getRid() {
        return rid;
    }

    public void setRid(Long rid) {
        this.rid = rid;
    }

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public Integer getOtype() {
        return otype;
    }

    public void setOtype(Integer otype) {
        this.otype = otype;
    }

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    public String getSnic() {
        return snic;
    }

    public void setSnic(String snic) {
        this.snic = snic;
    }

    public Long getDid() {
        return did;
    }

    public void setDid(Long did) {
        this.did = did;
    }

    public String getDnic() {
        return dnic;
    }

    public void setDnic(String dnic) {
        this.dnic = dnic;
    }

    public Long getEndtime() {
        return endtime;
    }

    public void setEndtime(Long endtime) {
        this.endtime = endtime;
    }

    public Date getT() {
        return t;
    }

    public void setT(Date t) {
        this.t = t;
    }

    @Override
    public String toString() {
        return "NewBlackresModel{" +
                "type='" + type + '\'' +
                ", rid=" + rid +
                ", ret='" + ret + '\'' +
                ", otype=" + otype +
                ", sid=" + sid +
                ", snic='" + snic + '\'' +
                ", did=" + did +
                ", dnic='" + dnic + '\'' +
                ", endtime=" + endtime +
                '}';
    }

    @Override
    public String getMType() {
        return TypeNameEnmu.new_black.name();
    }

    @Override
    public String getPK() {
        return null;
    }
}
