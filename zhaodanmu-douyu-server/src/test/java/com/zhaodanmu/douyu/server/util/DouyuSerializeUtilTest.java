package com.zhaodanmu.douyu.server.util;

import org.junit.Test;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.Assert.*;

public class DouyuSerializeUtilTest {

    @Test
    public void unSerialize() {
        String douyu = "type@=chatmsg/rid@=99999/ct@=1/uid@=219638351/nn@=晚温存c/txt@=66666/cid@=52f159829d974be4ad9d0e0000000000/ic@=avatar_v3@S201809@S50c9a86cf58a6c2b6f41865c944b8de1/level@=12/sahf@=0/cst@=1540447257431/bnn@=大马猴/bl@=8/brid@=99999/hc@=7094bdb067efbb89706bf894ceb8e67c/cbid@=22802/el@=/lk@=/fl@=8/";
        Map map = DouyuSerializeUtil.unSerialize(douyu);
        System.out.println(map.keySet());
    }

    @Test
    public void blockqueen() {
        LinkedBlockingQueue queue = new LinkedBlockingQueue();

        queue.add(1);
        queue.add(2);
        queue.add(3);
        queue.add(4);

        List list = new LinkedList();

        queue.drainTo(list, 2);
        queue.drainTo(list, 3);

        System.out.println();

    }
}