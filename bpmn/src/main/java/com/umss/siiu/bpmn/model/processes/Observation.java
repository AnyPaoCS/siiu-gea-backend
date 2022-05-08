package com.umss.siiu.bpmn.model.processes;

import com.umss.siiu.bpmn.dto.ObservationDto;
import com.umss.siiu.core.model.ModelBase;

import javax.persistence.*;

@Entity
public class Observation extends ModelBase<ObservationDto> {

    private String content;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_OBSERVATION_TASK_INSTANCE"))
    private TaskInstance taskInstance;

    public Observation(){}

    public Observation(String content, TaskInstance taskInstance) {
        this.content = content;
        this.taskInstance = taskInstance;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public TaskInstance getTaskInstance() {
        return taskInstance;
    }

    public void setTaskInstance(TaskInstance taskInstance) {
        this.taskInstance = taskInstance;
    }

}
