//package com.lchproject3.project3.global.discord;
//
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.Map;
//
//@Component
//public class DiscordWebHook {
//
//    @Value("#{${discord.webhooks}}")
//    private Map<String, String> webhookUrls;
//
//    public void sendLog(String channel, String message) throws IOException {
//        String url = webhookUrls.get(channel);
//        if (url == null) {
//            throw new IllegalArgumentException("Invalid channel: " + channel);
//        }
//
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        try {
//            HttpPost httpPost = new HttpPost(url);
//            String json = "{\"content\": \"" + message + "\"}";
//            StringEntity entity = new StringEntity(json);
//            httpPost.setEntity(entity);
//            httpPost.setHeader("Content-Type", "application/json");
//
//            CloseableHttpResponse response = httpClient.execute(httpPost);
//            response.close();
//        } finally {
//            httpClient.close();
//        }
//    }
//}
