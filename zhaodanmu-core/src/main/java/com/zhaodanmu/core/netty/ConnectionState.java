package com.zhaodanmu.core.netty;

public enum  ConnectionState {

    NEW("新建状态"),
    CONNECTED("连接成功"),
    DISCONNECTED("已断开连接"),
    CLOSED("已关闭");

    ConnectionState(String descZh) {
    }
}
