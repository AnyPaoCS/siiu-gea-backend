/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.bpmn.model.processes;

import com.umss.siiu.bpmn.dto.ProcessInstanceDto;
import com.umss.siiu.bpmn.model.JobBpm;
import com.umss.siiu.core.model.ModelBase;

import javax.persistence.*;
import java.util.List;

@Entity
public class ProcessInstance extends ModelBase<ProcessInstanceDto> {
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_PROCESS_INSTANCE_PROCESS"))
    private Process process;

    @OneToOne(mappedBy = "processInstance")
    private JobBpm jobBpm;

    @OneToMany(mappedBy = "processInstance")
    private List<TaskInstance> taskInstances;

    //todo this should be encapsulated in a object representing all the process properties it could be a map if we
    // need more properties
    private boolean manual = false;
    private boolean paused = false;

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public JobBpm getJobBpm() {
        return jobBpm;
    }

    public void setJobBpm(JobBpm jobBpm) {
        this.jobBpm = jobBpm;
    }

    public List<TaskInstance> getTaskInstances() {
        return taskInstances;
    }

    public void setTaskInstances(List<TaskInstance> taskInstances) {
        this.taskInstances = taskInstances;
    }

    public boolean isManual() {
        return manual;
    }

    public void setManual(boolean manual) {
        this.manual = manual;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }
}
