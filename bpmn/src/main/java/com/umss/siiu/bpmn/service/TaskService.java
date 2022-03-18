package com.umss.siiu.bpmn.service;

import com.umss.siiu.bpmn.model.processes.Task;
import com.umss.siiu.core.service.GenericService;

import java.util.List;

public interface TaskService extends GenericService<Task> {
    Task findByCode(String code);

    List<Task> findAssociatedTasksByTaskCode(String taskCode);

}
