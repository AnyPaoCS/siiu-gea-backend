/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.bpmn.model.processes;

import com.umss.siiu.bpmn.dto.TaskActionDto;
import com.umss.siiu.core.model.ModelBase;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class TaskAction extends ModelBase<TaskActionDto> {
    @Column(nullable = false)
    @NotNull
    private String name;

    @Enumerated(EnumType.STRING)
    private ActionFlowType actionFlowType;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_TASKACTION_TASK"))
    private Task task;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_TASKACTION_NEXTTASK"))
    private Task nextTask;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Task getNextTask() {
        return nextTask;
    }

    public void setNextTask(Task nextTask) {
        this.nextTask = nextTask;
    }

    public ActionFlowType getActionFlowType() {
        return actionFlowType;
    }

    public void setActionFlowType(ActionFlowType actionFlowType) {
        this.actionFlowType = actionFlowType;
    }
}
