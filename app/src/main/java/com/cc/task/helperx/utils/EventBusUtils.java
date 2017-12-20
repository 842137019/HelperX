package com.cc.task.helperx.utils;


import com.cc.task.helperx.entity.TaskEntry;

import org.simple.eventbus.EventBus;

public class EventBusUtils {

    private EventBus bus;

    private static EventBusUtils instance;

    private EventBusUtils() {
        bus = EventBus.getDefault();
    }

    public static EventBusUtils getInstance() {
        if (instance == null) {
            instance = new EventBusUtils();
        }
        return instance;
    }

    public void register(Object tagert) {
        if (tagert != null) {
            bus.register(tagert);
        }
    }

    public void unRegister(Object tagert) {
        if (tagert != null) {
            bus.unregister(tagert);
        }
    }

    public void sendMessage(Object msg) {
        bus.post(msg);
    }

    public void sendMessage(String tag) {
        bus.post(null,tag);
    }

    public void sendMessage(String tag, Object msg) {
        bus.post(msg,tag);
    }

    /**
     * 调试使用
     */
    public static class DebugMode {
        public final String type;
        public final TaskEntry info;
        public DebugMode(String type, TaskEntry info) {
            this.type = type;
            this.info = info;
        }
    }
}
