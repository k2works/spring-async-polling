package com.example.controllers;

import com.example.Task;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("api/tasks")
public class TaskRestController {
    final TaskService service;
    final HeavyJob heavyJob;

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

    @PutMapping
    public @ResponseBody Map<String, String> exec() {
        log.info("before heavyJob.execute()");
        heavyJob.execute(10);
        log.info("after heavyJob.execute()");


        Map<String, String> response = new HashMap<>();
        response.put("status", "Success");
        response.put("message", "完了");
        return response;
    }
}

