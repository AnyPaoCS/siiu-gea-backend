package com.umss.siiu.bpmn.repository;

import com.umss.siiu.bpmn.model.EmployeeTask;
import com.umss.siiu.core.model.Employee;
import com.umss.siiu.core.repository.GenericRepository;

import java.util.Set;

public interface EmployeeTaskRepository extends GenericRepository<EmployeeTask> {
    Set<EmployeeTask> findByEmployee(Employee employee);
}
