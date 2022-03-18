package com.umss.siiu.bpmn.repository;

import com.umss.siiu.bpmn.model.processes.Task;
import com.umss.siiu.core.repository.GenericRepository;

public interface TaskRepository extends GenericRepository<Task> {
    Task findByCode(String code);
}
