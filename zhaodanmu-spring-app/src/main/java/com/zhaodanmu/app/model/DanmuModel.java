package com.zhaodanmu.app.model;

public class DanmuModel implements Model {

    private long uid;

    /**
     * 头像
     */
    private String ic;

    private String nn;

    private int level;

    private long t;

    private long rid;

    private String type;

    private String txt;//弹幕内容

    private int cnt = 1;//赠送数量

    private int lev;//酬勤等级

    private Long gfid;//礼物 id

    private int gfcnt = 1;//礼物个数：默认值 1（表示 1 个礼物）

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getIc() {
        return ic;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }

    public String getNn() {
        return nn;
    }

    public void setNn(String nn) {
        this.nn = nn;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getT() {
        return t;
    }

    public void setT(long t) {
        this.t = t;
    }

    public long getRid() {
        return rid;
    }

    public void setRid(long rid) {
        this.rid = rid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public int getLev() {
        return lev;
    }

    public void setLev(int lev) {
        this.lev = lev;
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

    @Override
    public String toString() {
        return "DanmuModel{" +
                "uid=" + uid +
                ", ic='" + ic + '\'' +
                ", nn='" + nn + '\'' +
                ", level=" + level +
                ", t=" + t +
                ", rid=" + rid +
                ", type='" + type + '\'' +
                ", txt='" + txt + '\'' +
                ", cnt=" + cnt +
                ", lev=" + lev +
                ", gfid=" + gfid +
                ", gfcnt=" + gfcnt +
                '}';
    }
}
