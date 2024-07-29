package com.lchproject3.project3.global.discord;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import lombok.Setter;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Setter
@Component
public class DiscordAppender extends AppenderBase<ILoggingEvent> {

    private static final Logger logger = LoggerFactory.getLogger(DiscordAppender.class);

    private String webhookUrl;

    @Override
    protected void append(ILoggingEvent event) {
        if (webhookUrl == null) {
            logger.error("Discord webhook URL is not set.");
            return;
        }

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String payload = createPayload(event);
            HttpPost httpPost = createHttpPost(webhookUrl, payload);

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                int statusCode = response.getCode();
                if (statusCode != 204) {
                    logger.error("Failed to send log to Discord. HTTP status code: " + statusCode);
                }
            }
        } catch (IOException e) {
            logger.error("Failed to send log to Discord", e);
        }
    }

    private HttpPost createHttpPost(String webhookUrl, String payload) {
        HttpPost httpPost = new HttpPost(webhookUrl);
        httpPost.setEntity(new StringEntity(payload, ContentType.APPLICATION_JSON));
        return httpPost;
    }

    private String createPayload(ILoggingEvent event) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String level = event.getLevel().toString();
        String loggerName = getLastThreeParts(event.getLoggerName());
        String message = event.getFormattedMessage();
        return String.format("{\"content\":\"**[%s] [%s] [%s]** - %s\"}", timestamp, level, loggerName, message);
    }

    //라이브러리를 뒤에서부터 3단어만 자름
    private String getLastThreeParts(String loggerName) {
        String[] parts = loggerName.split("\\.");
        int length = parts.length;
        if (length <= 3) {
            return loggerName;
        }
        return String.join(".", parts[length - 3], parts[length - 2], parts[length - 1]);
    }
}
