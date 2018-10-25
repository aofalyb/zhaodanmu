package com.zhaodanmu.persistence.elasticsearch.model;

public class ChatMessageModel extends DouyuBaseModel {


    //type 表示为“弹幕”消息，固定为 chatmsg

    private String type = "chatmsg";

    private String txt;//弹幕文本内容

    private String cid;//弹幕唯一ID


    private int col;//颜色：默认值 0（表示默认颜色弹幕）

    private int rev;//	是否反向弹幕标记: 0-普通弹幕，1-反向弹幕, 默认值 0

    private int hl;//	是否高亮弹幕标记: 0-普通，1-高亮, 默认值 0

    private int ifs;//	是否粉丝弹幕标记: 0-非粉丝弹幕，1-粉丝弹幕, 默认值 0

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

    @Override
    public String toString() {
        return "ChatMessageModel{" +
                "type='" + type + '\'' +
                ", txt='" + txt + '\'' +
                ", cid='" + cid + '\'' +
                ", col=" + col +
                ", rev=" + rev +
                ", hl=" + hl +
                ", ifs=" + ifs +
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
