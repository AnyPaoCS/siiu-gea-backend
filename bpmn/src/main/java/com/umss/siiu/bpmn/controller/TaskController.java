package com.umss.siiu.bpmn.controller;

import com.umss.siiu.bpmn.dto.TaskDto;
import com.umss.siiu.bpmn.model.processes.Task;
import com.umss.siiu.bpmn.service.TaskService;
import com.umss.siiu.core.controller.GenericController;
import com.umss.siiu.core.service.GenericService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/tasks")
public class TaskController extends GenericController<Task, TaskDto> {

    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("associatedTasks/{taskCode}")
    public List<TaskDto> findAssociatedTasksByTaskCode(@PathVariable String taskCode) {
        List<Task> tasks = taskService.findAssociatedTasksByTaskCode(taskCode);
        return super.toDto(tasks);
    }

    @Override
    protected GenericService<Task> getService() {
        return taskService;
    }
}
