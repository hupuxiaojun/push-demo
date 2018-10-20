package com.example.demo.config;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.ServiceHelper;
import cn.jiguang.common.connection.ApacheHttpClient;
import cn.jpush.api.JPushClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.example.demo.util.Constants.APP_KEY;
import static com.example.demo.util.Constants.MASTER_SECRET;

/**
 * TODO
 *
 * @author : xiaojun
 * @since 16:45 2018/10/20
 */
@Configuration
public class JPushConfig {

//    所在地为北京的应用，调用方服务器也位于北京的话，使用 bjapi.jiguang.cn 作为调用地址，可以提升 api 响应速度。
//    请对照使用各功能的 bjapi：
//    New Push API & Schdule API:
//    https://bjapi.push.jiguang.cn/v3/push
//    https://bjapi.push.jiguang.cn/v3/push/validate
//    https://bjapi.push.jiguang.cn/v3/push/schedules
//    https://bjapi.push.jiguang.cn/v3/push/cid
//    https://bjapi.push.jiguang.cn/v3/push/grouppush
//    New Report API:
//    https://bjapi.push.jiguang.cn/v3/report/received
//    https://bjapi.push.jiguang.cn/v3/report/status/message
//    https://bjapi.push.jiguang.cn/v3/report/users
//    https://bjapi.push.jiguang.cn/v3/report/messages
//    New Device API：
//    https://bjapi.push.jiguang.cn/v3/device/(registration_id)
//    https://bjapi.push.jiguang.cn/v3/device/aliases
//    https://bjapi.push.jiguang.cn/v3/device/tags
//    https://bjapi.push.jiguang.cn/v3/device/status

    @Bean
    public JPushClient jPushClient() throws Exception {
        String authCode = ServiceHelper.getBasicAuthorization(APP_KEY, MASTER_SECRET);
        ClientConfig clientConfig = ClientConfig.getInstance();
        clientConfig.setPushHostName("bjapi.jiguang.cn");
        clientConfig.setDeviceHostName("bjapi.jiguang.cn");
        clientConfig.setScheduleHostName("bjapi.jiguang.cn");
        clientConfig.setReportHostName("bjapi.jiguang.cn");
        //实现了IHttpClient
        ApacheHttpClient httpClient = new ApacheHttpClient(authCode, null, clientConfig);
        httpClient.setMaxConnectionCount(3000);
        httpClient.setMaxConnectionPerRoute(1000);
        httpClient.setMaxHostConnection(1000);
        JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY, null, clientConfig);
        //设置pushclient使用httpclient连接池，否则使用的是Native client
        jpushClient.getPushClient().setHttpClient(httpClient);

        return jpushClient;
    }
}
