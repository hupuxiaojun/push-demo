package com.example.demo.util;

import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.notification.InterfaceAdapter;
import cn.jpush.api.push.model.notification.PlatformNotification;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JPushGsonUtil {
    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(PlatformNotification.class, new InterfaceAdapter<PlatformNotification>())
            .disableHtmlEscaping()
            .create();

    public static PushPayload fromJson(String payloadString) {
        return gson.fromJson(payloadString, PushPayload.class);
    }

    public static String toJson(PushPayload payload) {
        return gson.toJson(payload);
    }


}
