package com.zhaodanmu.core.event;

import com.google.common.eventbus.Subscribe;
import org.junit.Test;

import static org.junit.Assert.*;

public class EventBusTest {

    @Test
    public void create() {
    }

    @Test
    public void post() {
        EventBus.create();
        Demo demo1 = new Demo();
        EventBus.register(demo1);
        Demo demo = new Demo();
        EventBus.register(demo);
        //EventBus.post("1111");
    }

    @Test
    public void register() {
    }

    @Test
    public void unregister() {
    }


    public class Demo {
        @Subscribe
        public void on(String s) {
            System.out.println(s);
        }
    }
}