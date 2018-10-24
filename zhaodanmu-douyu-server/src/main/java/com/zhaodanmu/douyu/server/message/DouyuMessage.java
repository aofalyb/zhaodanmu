package com.zhaodanmu.douyu.server.message;

import com.zhaodanmu.core.common.Log;
import com.zhaodanmu.core.message.BaseMessage;
import com.zhaodanmu.core.netty.Connection;
import com.zhaodanmu.douyu.server.protocol.DouyuPacket;

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
            } catch (UnsupportedEncodingException e) {
                Log.defLogger.error(e);
            }
            Log.defLogger.debug(originMsg);
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
