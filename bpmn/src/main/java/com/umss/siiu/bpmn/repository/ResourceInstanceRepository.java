package com.umss.siiu.bpmn.repository;

import com.umss.siiu.bpmn.model.processes.ResourceInstance;
import com.umss.siiu.core.model.Employee;
import com.umss.siiu.core.repository.GenericRepository;

import java.util.List;

public interface ResourceInstanceRepository extends GenericRepository<ResourceInstance> {
    List<ResourceInstance> findByEmployeeAndActiveTrue(Employee employee);

    List<ResourceInstance> findByTaskInstanceId(Long id);
}
