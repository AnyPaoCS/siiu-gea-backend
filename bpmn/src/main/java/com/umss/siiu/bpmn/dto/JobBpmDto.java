package com.umss.siiu.bpmn.dto;

import com.umss.siiu.bpmn.model.JobBpm;
import com.umss.siiu.core.dto.DtoBase;
import org.modelmapper.ModelMapper;

public class JobBpmDto extends DtoBase<JobBpm> {

    private String priority;
    private String status;
    private Long jobId;
    private Long processInstanceId;

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(Long processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    @Override
    protected void afterConversion(JobBpm jobBpm, ModelMapper mapper) {
        setPriority(jobBpm.getPriority());
        setJobId(jobBpm.getJob().getId());
        if (null != jobBpm.getProcessInstance()) {
            setProcessInstanceId(jobBpm.getProcessInstance().getId());
        }
    }
}
