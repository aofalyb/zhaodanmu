package com.zhaodanmu.douyu.server.model;

public class DeserveModel extends DouyuBaseModel {

    private String type = "bc_buy_deserve";//表示为“赠送酬勤通知”消息，固定为 bc_buy_deserve

    private int cnt;//赠送数量

    private int hits;//赠送连击次数

    private int lev;//酬勤等级

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public int getLev() {
        return lev;
    }

    public void setLev(int lev) {
        this.lev = lev;
    }

    @Override
    public String toString() {
        return "DeserveModel{" +
                "type='" + type + '\'' +
                ", cnt=" + cnt +
                ", hits=" + hits +
                ", lev=" + lev +
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
