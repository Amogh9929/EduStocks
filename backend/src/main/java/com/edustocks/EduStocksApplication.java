package com.edustocks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EduStocksApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(EduStocksApplication.class, args);
        Environment env = ctx.getEnvironment();
        String key = env.getProperty("openai.api.key", "");
        int length = key == null ? 0 : key.trim().length();
        // Safe log: only length, never the actual key value
        System.out.println("[Startup] OPENAI_API_KEY resolved (length=" + length + ")");
    }
}



