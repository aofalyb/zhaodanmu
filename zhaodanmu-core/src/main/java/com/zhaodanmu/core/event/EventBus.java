package com.zhaodanmu.core.event;

public class EventBus {

    private static com.google.common.eventbus.EventBus eventBus;

    public static void create() {
        eventBus = new com.google.common.eventbus.EventBus();
    }

    public static void post(Event event) {
        eventBus.post(event);
    }

    public static void register(Object bean) {
        eventBus.register(bean);
    }

    public static void unregister(Object bean) {
        eventBus.unregister(bean);
    }

}
