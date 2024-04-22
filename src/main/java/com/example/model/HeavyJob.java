package com.example.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Async;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class HeavyJob {
    @Async
    public void execute() {
        log.info("before heavy task");
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("after heavy task");
    }
}
