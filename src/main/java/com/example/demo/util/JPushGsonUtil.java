package com.example.demo.util;

import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.notification.InterfaceAdapter;
import cn.jpush.api.push.model.notification.PlatformNotification;
import cn.jpush.api.schedule.model.TriggerPayload;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JPushGsonUtil {
    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(PlatformNotification.class, new InterfaceAdapter<PlatformNotification>())
            .disableHtmlEscaping()
            .create();

    public static PushPayload fromPushPayloadJson(String payloadString) {
        return gson.fromJson(payloadString, PushPayload.class);
    }

    public static String toPushPayloadJson(PushPayload payload) {
        return gson.toJson(payload);
    }

    public static String toTriggerPayloadJson(TriggerPayload trigger) {

        return gson.toJson(trigger);
    }

    public static TriggerPayload fromTriggerPayloadJson(String payloadString) {
        return gson.fromJson(payloadString, TriggerPayload.class);
    }


}
