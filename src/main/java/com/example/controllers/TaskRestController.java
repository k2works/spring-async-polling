package com.example.controllers;

import com.example.Task;
import com.example.model.GreetingMessageSender;
import com.example.model.HeavyJob;
import com.example.services.TaskService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("api/tasks")
public class TaskRestController {
    final TaskService service;
    final HeavyJob heavyJob;
    final GreetingMessageSender greetingMessageSender;

    @GetMapping
    public List<Task> showAll(){
        return service.status();
    }

    @PostMapping
    public Task execute() {
        Task task = service.register();
        service.execute(task.getId());
        return task;
    }

    @PutMapping("/heavy1")
    public @ResponseBody Map<String, String> exec1() {
        log.info("before heavyJob.execute1()");
        heavyJob.execute(10);
        log.info("after heavyJob.execute1()");


        Map<String, String> response = new HashMap<>();
        response.put("status", "Success");
        response.put("message", "完了");
        return response;
    }

    @PutMapping("/heavy2")
    public @ResponseBody CompletableFuture<Map<String, String>> exec2() {
        log.info("before heavyJob.execute2()");
        CompletableFuture<String> result = heavyJob.execute2(30);
        log.info("after heavyJob.execute2()");

        return result.thenApply(value -> {
            Map<String, String> response = new HashMap<>();
            response.put("status", "Success");
            response.put("message", value);
            return response;
        });
    }

    @GetMapping("/greeting")
    public SseEmitter greeting() throws IOException, InterruptedException {
        SseEmitter emitter = new SseEmitter();
        greetingMessageSender.send(emitter);
        return emitter;
    }
}


