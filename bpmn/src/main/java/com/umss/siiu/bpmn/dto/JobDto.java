package com.umss.siiu.bpmn.dto;

import com.umss.siiu.bpmn.model.Job;
import com.umss.siiu.core.dto.DtoBase;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

public class JobDto extends DtoBase<Job> {

    private Long processId;
    private Long appraisalId;
    private Long jobInformationId;
    private Long topEmployerId;
    private String name;
    private LocalDateTime dateEntered;
    private String priority;
    private String generalNotes;
    private String type;

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public Long getAppraisalId() {
        return appraisalId;
    }

    public void setAppraisalId(Long appraisalId) {
        this.appraisalId = appraisalId;
    }

    public Long getJobInformationId() {
        return jobInformationId;
    }

    public void setJobInformationId(Long jobInformationId) {
        this.jobInformationId = jobInformationId;
    }

    public Long getTopEmployerId() {
        return topEmployerId;
    }

    public void setTopEmployerId(Long topEmployerId) {
        this.topEmployerId = topEmployerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDateEntered() {
        return dateEntered;
    }

    public void setDateEntered(LocalDateTime dateEntered) {
        this.dateEntered = dateEntered;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getGeneralNotes() {
        return generalNotes;
    }

    public void setGeneralNotes(String generalNotes) {
        this.generalNotes = generalNotes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    protected void afterConversion(Job job, ModelMapper mapper) {
        setProcessId(job.getJobBpm().getProcessInstance().getId());
    }

}
