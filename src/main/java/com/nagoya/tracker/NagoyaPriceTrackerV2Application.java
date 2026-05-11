package com.nagoya.tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling // Spring Scheduler
@SpringBootApplication
public class NagoyaPriceTrackerV2Application {

    public static void main(String[] args) {
        SpringApplication.run(NagoyaPriceTrackerV2Application.class, args);
    }

}
