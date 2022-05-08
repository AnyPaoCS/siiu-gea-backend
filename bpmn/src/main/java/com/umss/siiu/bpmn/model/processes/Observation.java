package com.umss.siiu.bpmn.model.processes;

import com.umss.siiu.bpmn.dto.ObservationDto;
import com.umss.siiu.core.model.ModelBase;

import javax.persistence.*;

@Entity
public class Observation extends ModelBase<ObservationDto> {

    private String remark;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_OBSERVATION_TASK_INSTANCE"))
    private TaskInstance taskInstance;

    public Observation(){}

    public Observation(String remark, TaskInstance taskInstance) {
        this.remark = remark;
        this.taskInstance = taskInstance;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public TaskInstance getTaskInstance() {
        return taskInstance;
    }

    public void setTaskInstance(TaskInstance taskInstance) {
        this.taskInstance = taskInstance;
    }

}
