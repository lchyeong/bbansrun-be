//package com.lchproject3.project3.global.discord;
//
//
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//
//@RequiredArgsConstructor
//@Component
//public class DiscordLogger {
//
//    private static final Logger logger = LoggerFactory.getLogger(DiscordLogger.class);
//
//
//    private final DiscordWebHook discordWebHook;
//
//    public void log(String channel, String message) {
//        try {
//            discordWebHook.sendLog(channel, message);
//            logger.info("Logged to Discord channel [{}]: {}", channel, message);
//        } catch (Exception e) {
//            logger.error("Failed to log to Discord channel [{}]", channel, e);
//        }
//    }
//}
