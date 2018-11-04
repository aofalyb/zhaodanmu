package com.zhaodanmu.persistence.elasticsearch.model;

import com.zhaodanmu.persistence.api.Model;
import com.zhaodanmu.persistence.api.TypeNameEnmu;

import java.util.Date;

/**
 * Created by Administrator on 2018/11/4.
 */
public class ChatMessageModel implements Model {

    private Long uid;//发送者 uid

    private String txt;//弹幕文本内容

    private Long rid;//房间 id

    private Date t = new Date();

    private String cid;//弹幕唯一ID


    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public Long getRid() {
        return rid;
    }

    public void setRid(Long rid) {
        this.rid = rid;
    }

    public Date getT() {
        return t;
    }

    public void setT(Date t) {
        this.t = t;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    @Override
    public String getMType() {
        return TypeNameEnmu.danmu.name();
    }

    @Override
    public String getPK() {
        return cid;
    }
}
