package com.bbansrun.project1.global.discord;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Setter
@Component
@Slf4j
@Profile("prod")
public class DiscordAppender extends AppenderBase<ILoggingEvent> {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DISCORD_PAYLOAD_TEMPLATE = "{\"content\":\"%s\"}";
    private static final String LOG_MESSAGE_TEMPLATE = "**[%s] [%s] [%s] %s**\\n- %s\\n";
    private static final String ESCAPED_QUOTE = "\\\"";
    private static final String LINE_BREAK = "\\n";

    private static final Map<Integer, String> LEVEL_EMOJIS = Map.of(
            Level.ERROR_INT, "❗",
            Level.WARN_INT, "⚠️",
            Level.INFO_INT, "ℹ️",
            Level.DEBUG_INT, "🐞"
    );

    private String webhookUrl;

    @Override
    protected void append(ILoggingEvent event) {
        if (!isWebhookUrlValid()) {
            log.error("Discord webhook URL is not set.");
            return;
        }

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String payload = createPayload(event);
            HttpPost httpPost = createHttpPost(payload);
            executeRequest(httpClient, httpPost);
        } catch (IOException e) {
            log.error("Failed to send log to Discord", e);
        }
    }

    private boolean isWebhookUrlValid() {
        return webhookUrl != null && !webhookUrl.isEmpty();
    }

    private HttpPost createHttpPost(String payload) {
        HttpPost httpPost = new HttpPost(webhookUrl);
        httpPost.setEntity(new StringEntity(payload, ContentType.APPLICATION_JSON));
        return httpPost;
    }

    private void executeRequest(CloseableHttpClient httpClient, HttpPost httpPost) throws IOException {
        HttpClientResponseHandler<Void> responseHandler = createResponseHandler();
        httpClient.execute(httpPost, responseHandler);
    }

    private HttpClientResponseHandler<Void> createResponseHandler() {
        return response -> {
            int statusCode = response.getCode();
            if (statusCode != 204) {
                log.error("Failed to send log to Discord. Status code: {}", statusCode);
            }
            return null;
        };
    }

    private String createPayload(ILoggingEvent event) {
        String timestamp = getCurrentTimestamp();
        String level = event.getLevel().toString();
        String loggerName = getLastThreeParts(event.getLoggerName());
        String message = escapeMessage(event.getFormattedMessage());
        String emoji = getEmojiForLevel(event.getLevel());

        return String.format(DISCORD_PAYLOAD_TEMPLATE,
                String.format(LOG_MESSAGE_TEMPLATE, timestamp, level, loggerName, emoji, message)
                        .replace("\n", LINE_BREAK)
        );
    }

    private String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
    }

    private String escapeMessage(String message) {
        return message.replace("\"", ESCAPED_QUOTE);
    }

    private String getEmojiForLevel(Level level) {
        return LEVEL_EMOJIS.getOrDefault(level.toInt(), "🔍");
    }

    private String getLastThreeParts(String loggerName) {
        String[] parts = loggerName.split("\\.");
        int length = parts.length;
        return (length <= 3) ? loggerName : String.join(".", parts[length - 3], parts[length - 2], parts[length - 1]);
    }
}
