package com.example.demo.controller;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.device.TagAliasResult;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

import static cn.jpush.api.push.model.notification.PlatformNotification.ALERT;
import static com.example.demo.util.Constants.REG_ID;

/**
 * TODO
 *
 * @author : xiaojun
 * @since 19:57 2018/10/19
 */
@RestController
public class MyController {

    private static final Logger LOG = LoggerFactory.getLogger(MyController.class);

    @Autowired
    JPushClient jPushClient;


    @RequestMapping("/alias/{alias}")
    public Object updateAlias(@PathVariable("alias") String alias) {

        try {
            TagAliasResult tagAliasResult = jPushClient.getDeviceTagAlias(REG_ID);
            System.out.println("old alias:" + tagAliasResult.alias);
            System.out.println("old tags:" + StringUtils.collectionToCommaDelimitedString(tagAliasResult.tags));
            Set<String> tagsToAdd = new HashSet<>();
//            tagsToAdd.add("程序员");
            for (int i = 0; i < 99; i++) {
                tagsToAdd.add(String.valueOf(i));
            }
            Set<String> tagsToRemove = new HashSet<>();
            tagsToRemove.add("程序员");
            //更新tag、别名接口,1次最多支持打100个tag,对应的rest api的"设置设备的别名与标签"
            jPushClient.updateDeviceTagAlias(REG_ID, alias, tagsToAdd, tagsToRemove);
            //清理tag,别名接口
//            jPushClient.updateDeviceTagAlias(REG_ID,false,true);
            //给一批reg id打tag,删标签,对应的rest api的“更新标签”接口
//            jPushClient.addRemoveDevicesFromTag()
            tagAliasResult = jPushClient.getDeviceTagAlias(REG_ID);
            System.out.println("new alias:" + tagAliasResult.alias);
            System.out.println("new tags:" + StringUtils.collectionToCommaDelimitedString(tagAliasResult.tags));
        } catch (APIConnectionException e) {
            // Connection error, should retry later
            LOG.error("Connection error, should retry later", e);
            return "error";
        } catch (APIRequestException e) {
            // Should review the error, and fix the request
            LOG.error("Should review the error, and fix the request", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Code: " + e.getErrorCode());
            LOG.info("Error Message: " + e.getErrorMessage());
            return "error";
        }
        return "success";
    }

    @RequestMapping("/test")
    public Object test(@RequestParam(required = false, name = "alias") String alias) {


        // For push, all you need do is to build PushPayload object.
        PushPayload payload;
        if (alias != null) {
            payload = buildPushObject_all_alias_alert(alias);
        } else {
            payload = buildPushObject_all_all_alert();
        }

        try {
            //推送校验 API,该 API 只用于验证推送调用是否能够成功，与推送 API 的区别在于：不向用户发送任何消息。
            //建议push之前先自校验一下？
//            jPushClient.sendPushValidate(payload);

            PushResult result = jPushClient.sendPush(payload);
            LOG.info("Got result - " + result);
            return "success";
        } catch (APIConnectionException e) {
            // Connection error, should retry later
            LOG.error("Connection error, should retry later", e);
            return "error";
        } catch (APIRequestException e) {
            // Should review the error, and fix the request
            LOG.error("Should review the error, and fix the request", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Code: " + e.getErrorCode());
            LOG.info("Error Message: " + e.getErrorMessage());
            return "error";
        }
    }

    public static PushPayload buildPushObject_all_all_alert() {
        return PushPayload.alertAll(ALERT);
    }

    public static PushPayload buildPushObject_all_alias_alert(String alias) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(alias))
                .setNotification(Notification.alert(ALERT))
                .build();
    }
}
