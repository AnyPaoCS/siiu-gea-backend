package com.umss.siiu.bpmn.model.processes;

import com.umss.siiu.bpmn.dto.ObservationDto;
import com.umss.siiu.core.model.ModelBase;

import javax.persistence.*;

@Entity
public class Observation extends ModelBase<ObservationDto> {

    private String observation;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_OBSERVATION_TASK_INSTANCE"))
    private TaskInstance taskInstance;

    public Observation(){}

    public Observation(String observation, TaskInstance taskInstance) {
        this.observation = observation;
        this.taskInstance = taskInstance;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public TaskInstance getTaskInstance() {
        return taskInstance;
    }

    public void setTaskInstance(TaskInstance taskInstance) {
        this.taskInstance = taskInstance;
    }

}
