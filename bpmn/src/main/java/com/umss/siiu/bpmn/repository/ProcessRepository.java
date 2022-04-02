package com.umss.siiu.bpmn.repository;

import com.umss.siiu.bpmn.model.processes.Process;
import com.umss.siiu.core.repository.GenericRepository;

public interface ProcessRepository extends GenericRepository<Process> {
    Process findByName(String name);
    Process findByCode(String code);
}
