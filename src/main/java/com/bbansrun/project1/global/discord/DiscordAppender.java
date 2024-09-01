package com.bbansrun.project1.global.discord;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Setter
@Component
@Slf4j
@Profile("prod")  // prod 프로파일에서만 활성화
public class DiscordAppender extends AppenderBase<ILoggingEvent> {

    private String webhookUrl;

    /**
     * 로그 이벤트를 Discord로 전송합니다.
     *
     * @param event 로깅 이벤트
     */
    @Override
    protected void append(ILoggingEvent event) {
        if (webhookUrl == null || webhookUrl.isEmpty()) {
            log.error("Discord webhook URL is not set.");
            return;
        }

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String payload = createPayload(event);
            HttpPost httpPost = createHttpPost(webhookUrl, payload);

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                int statusCode = response.getCode();
                if (statusCode != 204) {
                    log.error("Failed to send log to Discord. Status code: {}", statusCode);
                }
            }
        } catch (IOException e) {
            log.error("Failed to send log to Discord", e);
        }
    }

    /**
     * Discord로 전송할 HTTP POST 요청을 생성합니다.
     *
     * @param webhookUrl Discord 웹훅 URL
     * @param payload    전송할 페이로드
     * @return HTTP POST 요청
     */
    private HttpPost createHttpPost(String webhookUrl, String payload) {
        HttpPost httpPost = new HttpPost(webhookUrl);
        httpPost.setEntity(new StringEntity(payload, ContentType.APPLICATION_JSON));
        return httpPost;
    }

    /**
     * Discord로 전송할 페이로드를 생성합니다.
     *
     * @param event 로깅 이벤트
     * @return 페이로드
     */
    private String createPayload(ILoggingEvent event) {
        String timestamp = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String level = event.getLevel().toString();
        String loggerName = getLastThreeParts(event.getLoggerName());
        String message = event.getFormattedMessage();
        String emoji = getEmojiForLevel(event.getLevel());

        String formattedMessage = String.format(
            "**[%s] [%s] [%s] %s**\n- %s\n",
            timestamp, level, loggerName, emoji, message.replace("\"", "\\\"")); //큰따옴표 이스케이프 처리

        return String.format("{\"content\":\"%s\"}", formattedMessage.replace("\n", "\\n"));
    }

    /**
     * 로그 레벨에 따라 이모지를 반환합니다.
     *
     * @param level 로그 레벨
     * @return 이모지
     */
    private String getEmojiForLevel(Level level) {
        return switch (level.toInt()) {
            case Level.ERROR_INT -> "❗";
            case Level.WARN_INT -> "⚠️";
            case Level.INFO_INT -> "ℹ️";
            case Level.DEBUG_INT -> "🐞";
            default -> "🔍";
        };
    }

    /**
     * 로거 이름에서 마지막 세 부분을 가져옵니다.
     *
     * @param loggerName 로거 이름
     * @return 마지막 세 부분
     */
    private String getLastThreeParts(String loggerName) {
        String[] parts = loggerName.split("\\.");
        int length = parts.length;
        if (length <= 3) {
            return loggerName;
        }
        return String.join(".", parts[length - 3], parts[length - 2], parts[length - 1]);
    }
}
