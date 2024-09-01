package com.bbansrun.project1.domain.users.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
@RequestMapping("/api/discord")
public class UserController {

    @GetMapping("/test")
    public String test() {
        log.info("Test endpoint /test called");
        log.debug("Test endpoint /test called");
        log.error("Test endpoint /test called");
        log.warn("Test endpoint /test called");
        return "Hello, Discord!";
    }
}
