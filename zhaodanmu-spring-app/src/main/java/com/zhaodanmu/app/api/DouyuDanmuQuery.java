package com.zhaodanmu.app.api;

public class DouyuDanmuQuery implements Query {

    private Long uid;

    private String type;

    private String nn;

    private String txt;//弹幕内容

    public DouyuDanmuQuery() {
    }

    public DouyuDanmuQuery(Long uid, String type, String nn, String txt) {
        this.uid = uid;
        this.type = type;
        this.nn = nn;
        this.txt = txt;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    @Override
    public String toString() {
        return "DouyuDanmuQuery{" +
                "uid=" + uid +
                ", type='" + type + '\'' +
                ", nn='" + nn + '\'' +
                ", txt='" + txt + '\'' +
                '}';
    }


}
