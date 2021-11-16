package com.umss.siiu.bpmn.model;

import com.umss.siiu.bpmn.dto.JobBpmDto;
import com.umss.siiu.bpmn.model.processes.ProcessInstance;
import com.umss.siiu.core.model.ModelBase;
import org.modelmapper.ModelMapper;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class JobBpm extends ModelBase<JobBpmDto> {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_JOBBPM_PROCESSINSTANCE"))
    private ProcessInstance processInstance;

    @Column(nullable = false)
    private String priority;

    private String status;

    private LocalDateTime completedTime;

    @OneToOne(mappedBy = "jobBpm", fetch = FetchType.LAZY)
    private Job job;

    public ProcessInstance getProcessInstance() {
        return processInstance;
    }

    public void setProcessInstance(ProcessInstance processInstance) {
        this.processInstance = processInstance;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(LocalDateTime completedTime) {
        this.completedTime = completedTime;
    }

    @Override
    public JobBpm toDomain(JobBpmDto element, ModelMapper mapper) {
        super.toDomain(element, mapper);
        setPriority(element.getPriority());
        return this;
    }
}
