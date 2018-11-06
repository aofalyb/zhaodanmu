package com.zhaodanmu.persistence.elasticsearch.model;

import com.zhaodanmu.persistence.api.Model;
import com.zhaodanmu.persistence.api.TypeNameEnmu;

import java.util.Date;

/**
 * Created by Administrator on 2018/11/4.
 */
public class GiveGiftModel implements Model {

    private Long gfid;//礼物 id

    private int gfcnt = 1;//礼物个数：默认值 1（表示 1 个礼物）

    private int hits = 1;//		礼物连击次数：默认值 1（表示 1 连击）

    private long uid;

    private long rid;

    private Date t = new Date();



    public Long getGfid() {
        return gfid;
    }

    public void setGfid(Long gfid) {
        this.gfid = gfid;
    }

    public int getGfcnt() {
        return gfcnt;
    }

    public void setGfcnt(int gfcnt) {
        this.gfcnt = gfcnt;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
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
    public String type() {
        return TypeNameEnmu.gift.name();
    }

    @Override
    public String pK() {
        return null;
    }

    @Override
    public String toString() {
        return "GiveGiftModel{" +
                "gfid=" + gfid +
                ", gfcnt=" + gfcnt +
                ", hits=" + hits +
                ", uid=" + uid +
                ", rid=" + rid +
                '}';
    }
}
