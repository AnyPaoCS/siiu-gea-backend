/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.bpmn.model.processes;

import com.umss.siiu.bpmn.dto.TaskInstanceDto;
import com.umss.siiu.core.model.ModelBase;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TaskInstance extends ModelBase<TaskInstanceDto> {
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_TASKINSTANCE_PROCESSINSTANCE"), updatable = false)
    private ProcessInstance processInstance;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_TASKINSTANCE_TASK"), updatable = false)
    private Task task;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "taskInstance", fetch = FetchType.EAGER)
    private List<ResourceInstance> resourceInstances;

    private LocalDateTime startTime;
    private LocalDateTime completionTime;
    private Long workedTime;

    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(LocalDateTime completionTime) {
        this.completionTime = completionTime;
    }

    public Long getWorkedTime() {
        return workedTime;
    }

    public void setWorkedTime(Long workedTime) {
        this.workedTime = workedTime;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public ProcessInstance getProcessInstance() {
        return processInstance;
    }

    public void setProcessInstance(ProcessInstance processInstance) {
        this.processInstance = processInstance;
    }

    public List<ResourceInstance> getResourceInstances() {
        if (resourceInstances == null) {
            resourceInstances = new ArrayList<>();
        }
        return resourceInstances;
    }

    public void setResourceInstances(List<ResourceInstance> resourceInstances) {
        this.resourceInstances = resourceInstances;
    }

    public TaskInstance complete() {
        setTaskStatus(TaskStatus.DONE);
        setCompletionTime(LocalDateTime.now());
        return this;
    }
}
