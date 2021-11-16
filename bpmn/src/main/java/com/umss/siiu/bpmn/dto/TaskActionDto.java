package com.umss.siiu.bpmn.dto;

import com.umss.siiu.bpmn.model.processes.TaskAction;
import com.umss.siiu.core.dto.DtoBase;

public class TaskActionDto extends DtoBase<TaskAction> {

    private TaskDto task;
    private TaskDto nextTask;

    public TaskActionDto() {
        super();
    }

    public TaskDto getTask() {
        return task;
    }

    public void setTask(TaskDto task) {
        this.task = task;
    }

    public TaskDto getNextTask() {
        return nextTask;
    }

    public void setNextTask(TaskDto nextTask) {
        this.nextTask = nextTask;
    }

}
