package com.zhaodanmu.douyu.server.util;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class DouyuSerializeUtil {

    public static Map unSerialize(String serializedString){
        Map attrs = new HashMap();
        String[] strings = serializedString.split("@=");
        String thisVk = null;
        String nextVk = null;
        for (int i = 0; i < strings.length; i++) {
            thisVk = strings[i];
            if(i != strings.length - 1){
                nextVk = strings[i+1];
            }
            if(!thisVk.contains("/")){
                String[] nextVks = nextVk.split("/");
                if(nextVks.length == 0){
                    //attrs.put(thisVk,"");
                }else if(nextVks.length <= 2){
                    attrs.put(thisVk,nextVks[0]);
                }else if(nextVks.length > 2){
                    //说明是数组
                    attrs.put(thisVk,Arrays.asList(nextVks));
                }
            }else {
                String[] thisVks = thisVk.split("/");
                if(thisVks.length == 0){
                    continue;
                }else if(thisVks.length == 2){
                    String[] nextVks = nextVk.split("/");
                    if(nextVks.length == 0){
                        //attrs.put(thisVks[1],"");
                    }else if(nextVks.length <= 2){
                        attrs.put(thisVks[1],nextVks[0]);
                    }else if(nextVks.length > 2){
                        attrs.put(thisVks[1],Arrays.asList(nextVks));
                    }
                }
            }
        }
        return attrs;
    }
}
