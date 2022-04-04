package com.panxora.gravity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Boot {
    public static void main(String[] args) {
        start(args);
    }

    private static void start(String[] args) {
        SpringApplication.run(Boot.class, args);
    }
}