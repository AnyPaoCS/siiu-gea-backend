package com.umss.siiu.bpmn.dto;

import com.umss.siiu.bpmn.model.Job;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class JobDetailDto extends JobDto {

    private List<String> assignees;
    private String status;
    private LocalDateTime dueTime;
    private LocalDateTime completedTime;

    public List<String> getAssignees() {
        return assignees;
    }

    public void setAssignees(List<String> assignees) {
        this.assignees = assignees;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDueTime() {
        return dueTime;
    }

    public void setDueTime(LocalDateTime dueTime) {
        this.dueTime = dueTime;
    }

    public LocalDateTime getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(LocalDateTime completedTime) {
        this.completedTime = completedTime;
    }

    @Override
    protected void afterConversion(Job job, ModelMapper mapper) {
        setAssignees(Collections.emptyList());
        if (job.getJobBpm() != null) {
            setCompletedTime(job.getJobBpm().getCompletedTime());
            setStatus(job.getJobBpm().getStatus());
        }
    }
}
