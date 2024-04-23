package com.example.controllers;

import com.example.Task;
import com.example.model.GreetingMessageSender;
import com.example.model.HeavyJob;
import com.example.services.ExecuteService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("api/tasks")
public class TaskRestController {
    final TaskService service;
    final ExecuteService executeService;
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


    @PutMapping("/heavy3")
    public @ResponseBody List<String> exec3() throws InterruptedException, ExecutionException {
        log.info("before heavyJob.execute3()");
        CompletableFuture<String> light = heavyJob.execute2(10);
        CompletableFuture<String> heavy = heavyJob.execute2(30);

        light.thenAcceptAsync(result ->{
            log.warn("light:" + result);
        });

        heavy.thenAcceptAsync(result ->{
            log.warn("heavy:" + result);
        });

        CompletableFuture.allOf(light, heavy).join();

        List<String> results = new ArrayList<>();
        results.add(light.get());
        results.add(heavy.get());

        log.info("after heavyJob.execute3()");

        return results;
    }

    @PutMapping("/heavy4")
    public @ResponseBody Map<String, String> exec4() throws InterruptedException, ExecutionException {
        log.info("before heavyJob.execute4()");
        Task task = service.start(2);

        CompletableFuture<String> light = heavyJob.execute2(10);
        CompletableFuture<String> heavy = heavyJob.execute2(30);

        light.thenAcceptAsync(result ->{
            task.setDone(task.getDone() + 1);
            log.warn("light:" + result + task.toString());
            service.complete(task);
        });

        heavy.thenAcceptAsync(result ->{
            task.setDone(task.getDone() + 1);
            log.warn("heavy:" + result + task.toString());
            service.complete(task);
        });
        log.info("after heavyJob.execute4()");

        Map<String, String> response = new HashMap<>();
        response.put("status", "Success");
        response.put("message", "非同期処理を開始しました");
        return response;
    }


    @GetMapping("/greeting")
    public SseEmitter greeting() throws IOException, InterruptedException {
        SseEmitter emitter = new SseEmitter();
        greetingMessageSender.send(emitter);
        return emitter;
    }
}


