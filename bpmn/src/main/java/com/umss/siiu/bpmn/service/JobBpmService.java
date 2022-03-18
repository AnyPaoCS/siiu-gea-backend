package com.umss.siiu.bpmn.service;

import com.umss.siiu.bpmn.dto.TaskInstanceDto;
import com.umss.siiu.bpmn.model.Job;
import com.umss.siiu.bpmn.model.JobBpm;
import com.umss.siiu.bpmn.model.processes.TaskInstance;
import com.umss.siiu.core.service.GenericService;

import java.util.List;

public interface JobBpmService extends GenericService<JobBpm> {

    List<JobBpm> createJobBpms(List<Job> jobs);

    void allocateResources();

    TaskInstance changeStatus(TaskInstanceDto taskInstanceDto, Boolean exceptionTrigger);

    JobBpm findByJobId(long jobId);

    List<String> getAssigneesByProcessInstanceId(Long id);
}
