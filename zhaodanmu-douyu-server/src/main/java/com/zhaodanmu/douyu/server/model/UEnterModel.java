package com.zhaodanmu.douyu.server.model;

public class UEnterModel extends DouyuBaseModel {

    private String type = "uenter";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "UEnterModel{" +
                "type='" + type + '\'' +
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
