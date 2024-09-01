package com.bbansrun.project1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class test {
    private static final Logger logger = LoggerFactory.getLogger(test.class);

    @GetMapping("/test-log")
    public String testLog() {
        logger.info("This is an info message");
        logger.warn("This is a warning message");
        logger.error("This is an error message");
        return "Log messages sent to Discord";
    }
}
