package com.example.controllers;

import com.example.Task;
import com.example.model.HeavyJob;
import com.example.services.TaskService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
        log.info("before heavyJob.execute()");
        heavyJob.execute();
        log.info("after heavyJob.execute()");

        Task task = service.register();
        service.execute(task.getId());
        return task;
    }

}
