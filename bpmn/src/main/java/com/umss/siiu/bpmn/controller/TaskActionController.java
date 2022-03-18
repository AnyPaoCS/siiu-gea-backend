package com.umss.siiu.bpmn.controller;

import com.umss.siiu.bpmn.dto.TaskActionDto;
import com.umss.siiu.bpmn.model.processes.TaskAction;
import com.umss.siiu.bpmn.service.TaskActionService;
import com.umss.siiu.core.controller.GenericController;
import com.umss.siiu.core.service.GenericService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/taskActions")
public class TaskActionController extends GenericController<TaskAction, TaskActionDto> {

    private TaskActionService taskActionService;

    public TaskActionController(TaskActionService taskActionService) {
        this.taskActionService = taskActionService;
    }

    @Override
    protected GenericService<TaskAction> getService() {
        return taskActionService;
    }
}
