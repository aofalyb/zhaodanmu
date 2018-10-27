package com.zhaodanmu.persistence.elasticsearch.model;

import com.zhaodanmu.persistence.api.Model;
import com.zhaodanmu.persistence.elasticsearch.TypeNameEnmu;

import java.util.Date;

/**
 * es数据模型
 */
public class DouyuESModel implements Model{

    private Date t = new Date();

    private String type;

    private Long rid;//房间 id
    private Long uid;//发送者 uid
    private int level;//用户等级
    private String nn;//发送者昵称
    //private Long gid;//弹幕组 id
    //private String ic;//用户头像

    //private int nl;//贵族等级

    //private int nc;//贵族弹幕标识,0-非贵族弹幕,1-贵族弹幕,默认值 0

    //private String bnn;//徽章昵称

    //private Integer bl;//徽章等级

    //private Long brid;//徽章房间 id


    private String txt;//弹幕文本内容

    //private String cid;//弹幕唯一ID

    //private int col;//颜色：默认值 0（表示默认颜色弹幕）

    //private int rev;//	是否反向弹幕标记: 0-普通弹幕，1-反向弹幕, 默认值 0

    //private int hl;//	是否高亮弹幕标记: 0-普通，1-高亮, 默认值 0

    //private int ifs;//	是否粉丝弹幕标记: 0-非粉丝弹幕，1-粉丝弹幕, 默认值 0


    private int cnt;//赠送数量

    private int lev;//酬勤等级

    private Long gfid;//礼物 id

    private int gfcnt = 1;//礼物个数：默认值 1（表示 1 个礼物）

    //private int hits = 1;//		礼物连击次数：默认值 1（表示 1 连击）


    public Date getT() {
        return t;
    }

    public void setT(Date t) {
        this.t = t;
    }

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
    public String getMType() {
        return TypeNameEnmu.danmu.name();
    }

    @Override
    public String getPK() {
        return null;
    }
}
