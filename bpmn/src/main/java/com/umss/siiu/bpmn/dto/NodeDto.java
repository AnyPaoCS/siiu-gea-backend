package com.umss.siiu.bpmn.dto;

import com.umss.siiu.bpmn.model.processes.ActionFlowType;
import com.umss.siiu.bpmn.model.processes.Task;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

public class NodeDto {
    private TaskDto task;
    private TaskDto nextTask;

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

    public List<NodeDto> toNodeList(Task task, ModelMapper mapper){
        List<NodeDto> taskList = new ArrayList<>();
        getNode(taskList, task, mapper);
        return taskList;
    }

    private void getNode(List<NodeDto> list, Task task, ModelMapper mapper) {
        task.getTaskActions().forEach(taskAction -> {
            NodeDto node = new NodeDto();
            node.setTask(new TaskDto().toDto(taskAction.getTask(), mapper));
            node.setNextTask(new TaskDto().toDto(taskAction.getNextTask(), mapper));
            list.add(node);
            if (taskAction.getNextTask().getCode() != task.getCode() && taskAction.getActionFlowType() != ActionFlowType.FORCE_GATE_ENTRY ) {
                getNode(list, taskAction.getNextTask(), mapper);
            }
        });
    }
}
