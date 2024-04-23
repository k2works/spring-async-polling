package com.example.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class HeavyJob {
    @Async
    public void execute(long timeout) {
        log.info("before heavy task1");
        try {
            TimeUnit.SECONDS.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("after heavy task1");
    }

    @Async
    public CompletableFuture<String> execute2(long timeout) {
        log.info("before heavy task2");
        try {
            TimeUnit.SECONDS.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("after heavy task2");

        return CompletableFuture.completedFuture("完了");
    }
}
