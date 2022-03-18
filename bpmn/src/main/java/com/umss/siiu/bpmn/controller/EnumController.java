package com.umss.siiu.bpmn.controller;

import com.umss.siiu.bpmn.model.JobStatus;
import com.umss.siiu.bpmn.model.processes.TaskStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/enums")
public class EnumController {

    @GetMapping("/taskStatus")
    public List<TaskStatus> getTaskStatus() {
        return Arrays.asList(TaskStatus.values());
    }

    @GetMapping(value = "/status")
    public List<String> getStatus() {
        return Arrays.stream(JobStatus.values()).map(JobStatus::toString).collect(Collectors.toList());
    }
}
