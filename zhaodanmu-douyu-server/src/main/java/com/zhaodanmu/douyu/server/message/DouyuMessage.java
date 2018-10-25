package com.zhaodanmu.douyu.server.message;

import com.zhaodanmu.core.common.Log;
import com.zhaodanmu.core.message.BaseMessage;
import com.zhaodanmu.core.netty.Connection;
import com.zhaodanmu.douyu.server.protocol.DouyuPacket;
import com.zhaodanmu.douyu.server.util.DouyuSerializeUtil;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class DouyuMessage extends BaseMessage {

    protected Map<String,String> attributes = new HashMap();


    public DouyuMessage(DouyuPacket packet, Connection connection) {
        super(packet, connection);
    }

    @Override
    public DouyuMessage decodeBody() {
        byte[] douyuPacketBody = packet.getBody();
        if(douyuPacketBody != null){
            String originMsg = null;
            try {
                originMsg = new String(douyuPacketBody,"utf-8");
                attributes = DouyuSerializeUtil.unSerialize(originMsg);
            } catch (Exception e) {
                Log.defLogger.error(e);
            }
        }
        return this;
    }


    @Override
    public void encodeBody() {

    }

    @Override
    public String getMessageType() {
        return attributes.get("type");
    }


    public Map<String, String> getAttributes() {
        return attributes;
    }
}
