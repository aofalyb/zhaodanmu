package com.zhaodanmu.douyu.server.model;

public class GiveGiftsModel extends DouyuBaseModel {


    private String type = "dgb";//表示为“赠送礼物”消息，固定为 dgb

    private Long gfid;//礼物 id

    private int gfcnt = 1;//礼物个数：默认值 1（表示 1 个礼物）

    private int hits = 1;//		礼物连击次数：默认值 1（表示 1 连击）


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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

    @Override
    public String toString() {
        return "GiveGiftsModel{" +
                "type='" + type + '\'' +
                ", gfid=" + gfid +
                ", gfcnt=" + gfcnt +
                ", hits=" + hits +
                ", rid=" + rid +
                ", uid=" + uid +
                ", level=" + level +
                ", nn='" + nn + '\'' +
                ", gid='" + gid + '\'' +
                ", ic='" + ic + '\'' +
                ", nl=" + nl +
                ", nc=" + nc +
                ", bnn='" + bnn + '\'' +
                ", bl=" + bl +
                ", brid=" + brid +
                '}';
    }
}
