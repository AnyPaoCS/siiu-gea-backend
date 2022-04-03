package com.umss.siiu.bpmn.repository;

import java.util.List;

import com.umss.siiu.bpmn.model.processes.ProcessInstance;
import com.umss.siiu.core.repository.GenericRepository;

public interface ProcessInstanceRepository extends GenericRepository<ProcessInstance> {

    ProcessInstance findByJobBpmJobId(Long id);
    List<ProcessInstance> findByUserEmail(String email);
}
