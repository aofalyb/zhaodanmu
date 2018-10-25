package com.zhaodanmu.persistence.elasticsearch.model;

import java.util.Date;

public class DouyuESModel {

    private Date now = new Date();

    private Long rid;//房间 id
    private Long uid;//发送者 uid
    private int level;//用户等级
    private String nn;//发送者昵称
    private Long gid;//弹幕组 id
    private String ic;//用户头像

    private int nl;//贵族等级

    private int nc;//贵族弹幕标识,0-非贵族弹幕,1-贵族弹幕,默认值 0

    private String bnn;//徽章昵称

    private Integer bl;//徽章等级

    private Long brid;//徽章房间 id


    private String type;

    private String txt;//弹幕文本内容

    private String cid;//弹幕唯一ID

    private int col;//颜色：默认值 0（表示默认颜色弹幕）

    private int rev;//	是否反向弹幕标记: 0-普通弹幕，1-反向弹幕, 默认值 0

    private int hl;//	是否高亮弹幕标记: 0-普通，1-高亮, 默认值 0

    private int ifs;//	是否粉丝弹幕标记: 0-非粉丝弹幕，1-粉丝弹幕, 默认值 0


    private int cnt;//赠送数量

    private int lev;//酬勤等级

    private Long gfid;//礼物 id

    private int gfcnt = 1;//礼物个数：默认值 1（表示 1 个礼物）

    private int hits = 1;//		礼物连击次数：默认值 1（表示 1 连击）

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

    public Long getGid() {
        return gid;
    }

    public void setGid(Long gid) {
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

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRev() {
        return rev;
    }

    public void setRev(int rev) {
        this.rev = rev;
    }

    public int getHl() {
        return hl;
    }

    public void setHl(int hl) {
        this.hl = hl;
    }

    public int getIfs() {
        return ifs;
    }

    public void setIfs(int ifs) {
        this.ifs = ifs;
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

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public Date getNow() {
        return now;
    }

    public void setNow(Date now) {
        this.now = now;
    }

    @Override
    public String toString() {
        return "DouyuESModel{" +
                "rid=" + rid +
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
                ", type='" + type + '\'' +
                ", txt='" + txt + '\'' +
                ", cid='" + cid + '\'' +
                ", col=" + col +
                ", rev=" + rev +
                ", hl=" + hl +
                ", ifs=" + ifs +
                ", cnt=" + cnt +
                ", lev=" + lev +
                ", gfid=" + gfid +
                ", gfcnt=" + gfcnt +
                ", hits=" + hits +
                '}';
    }
}
